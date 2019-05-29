package run;

import domain.ApplicationUser;
import interfaces.JDBCConnectionImpl;
import service.JDBCServiceUser;
import util.UtilString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class JDBCUserAction {

    private static JDBCServiceUser serviceUser;
    private static UtilString utilString = UtilString.getInstance();

    static {
        try {
            serviceUser = JDBCServiceUser.getInstance(JDBCConnectionImpl.getInstance().getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        System.out.println("---------------- START TABLE STATE ------------------");
        serviceUser.printUserList();
        /*
        System.out.println("---------------- INSERT ------------------");
        for (int i =0; i < 10; i++) {
           serviceUser.addUser("user_" + i, "qwerty_" + i);
        }
        serviceUser.printUserList();
        /*System.out.println("---------------- SELECT ------------------");
        System.out.println("User by Id : " + serviceUser.getUser(4));
        System.out.println("User by login : " + serviceUser.getUser("user_6"));
        serviceUser.printUserList();
        System.out.println("---------------- DELETE ------------------");
        serviceUser.delUser("user_3");
        serviceUser.delUser(5);
        System.out.println("After delete :");
        serviceUser.printUserList();
        System.out.println("---------------- UPDATE ------------------");
        serviceUser.updUser("user_1", "user_1_upd", "qwerty_1_upd");
        serviceUser.updUser(8, "user_7_upd", "qwerty_7_upd");
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
