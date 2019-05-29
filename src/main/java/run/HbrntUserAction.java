package run;

import domain.ApplicationUser;
import interfaces.HbrntSessionFactoryImpl;
import interfaces.JDBCConnectionImpl;
import org.hibernate.SessionFactory;
import service.HbrntServiceUser;
import service.JDBCServiceUser;
import util.UtilString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class HbrntUserAction {

    private static SessionFactory sessionFactory = HbrntSessionFactoryImpl.getInstance().getFactory();
    private static HbrntServiceUser serviceUser;
    private static UtilString utilString = UtilString.getInstance();

    static {
        serviceUser = HbrntServiceUser.getInstance(sessionFactory);
    }

    public static void main(String[] args) {

        System.out.println("---------------- START TABLE STATE ------------------");
        serviceUser.printUserList();
        /*
        System.out.println("---------------- INSERT ------------------");
        for (int i =0; i < 10; i++) {
            serviceUser.addUser("hiberUser_" + i, "qwerty_" + i);
        }
        serviceUser.printUserList();
        System.out.println("---------------- UPDATE ------------------");
        serviceUser.updUser(544, "hiberUser_8_upd", "qwerty_8_upd");
        serviceUser.printUserList();
        System.out.println("---------------- DELETE ------------------");
        serviceUser.delUser("hiberUser_7");
        serviceUser.delUser(531);
        serviceUser.printUserList();
        System.out.println("---------------- SELECT ------------------");
        System.out.println("User by Id : " + serviceUser.getUser(539));
        System.out.println("User by login : " + serviceUser.getUser("hiberUser_6"));
        System.out.println("User by login & password: " + serviceUser.getUser("hiberUser_6", "qwerty_6"));
        serviceUser.printUserList();
        */

        System.out.println("---------------- AUTHORIZATION ------------------");
        ApplicationUser appUser;
        try {
            appUser = userAuth();
            System.out.println(
                    appUser == null
                            ? "Authorization failed. User not found"
                            : "User authorized as " + appUser.toString()
            );
        } catch (IOException e) {
            System.out.println("Authorization failed with error");
        }
        System.out.println("---------------- THE END ------------------");

        sessionFactory.close();

    }

    private static ApplicationUser userAuth() throws IOException {
        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            String login = getNotEmptyDataFromConsole(consoleReader, "Login: ");
            String password = getNotEmptyDataFromConsole(consoleReader, "Password: ");
            return serviceUser.getUser(login, password);

        }
    }

    private static String getNotEmptyDataFromConsole(BufferedReader consoleReader, String textToConsole) throws IOException {
        String result = "";
        while (utilString.isEmpty(result)) {
            System.out.print(textToConsole);
            result = consoleReader.readLine();
        }
        return result;
    }

}
