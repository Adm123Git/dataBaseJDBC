package ru.adm123.run;

import ru.adm123.domains.ApplicationUser;
import ru.adm123.connections.HbrntSessionFactory;
import ru.adm123.connections.JDBCConnection;
import ru.adm123.services.ServiceUser;
import org.hibernate.SessionFactory;
import ru.adm123.services.HbrntServiceUser;
import ru.adm123.services.JDBCServiceUser;
import ru.adm123.utils.UtilString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserServiceExecutor {

    private static ServiceUser userService;
    private static SessionFactory sessionFactory = null;
    private static UtilString utilString = UtilString.getInstance();
    //private static final UtilString utilString = new UtilString();

    static {
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("startconfig.properties")))) {
                String ormSetString = reader
                        .lines()
                        .findFirst()
                        .get();
                if (ormSetString.split("=")[0].equalsIgnoreCase("ORM") && ormSetString.split("=")[1].equalsIgnoreCase("hibernate")) {
                    sessionFactory = HbrntSessionFactory.getInstance().getFactory();
                    userService = HbrntServiceUser.getInstance(sessionFactory);
                    //userService = new HbrntServiceUser(sessionFactory);
                } else {
                    userService = JDBCServiceUser.getInstance(JDBCConnection.getInstance().getConnection());
                    //userService = new JDBCServiceUser(JDBCConnection.getInstance().getConnection());
                }
            } catch (Exception e) {
                userService = JDBCServiceUser.getInstance(JDBCConnection.getInstance().getConnection());
                //userService = new JDBCServiceUser(JDBCConnection.getInstance().getConnection());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        System.out.println("---------------- START TABLE STATE ------------------");
        userService.printUserList();

/*
        System.out.println("---------------- INSERT ------------------");
        for (int i =0; i < 10; i++) {
           userService.addUser("user_" + i, "qwerty_" + i);
        }
        userService.printUserList();
        System.out.println("---------------- SELECT ------------------");
        System.out.println("User by Id : " + userService.getUser(4));
        System.out.println("User by login : " + userService.getUser("user_6"));
        System.out.println("User by login & password: " + userService.getUser("user_2", "qwerty_2"));
        userService.printUserList();
        System.out.println("---------------- DELETE ------------------");
        userService.delUser("user_3");
        userService.delUser(5);
        userService.printUserList();
        System.out.println("---------------- UPDATE ------------------");
        userService.updUser("user_1", "user_1_upd", "qwerty_1_upd");
        userService.updUser(8, "user_7_upd", "qwerty_7_upd");
        userService.printUserList();
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

        if (sessionFactory != null) {
            sessionFactory.close();
        }

    }

    private static ApplicationUser userAuth() throws IOException {
        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            String login = getNotEmptyDataFromConsole(consoleReader, "Login: ");
            String password = getNotEmptyDataFromConsole(consoleReader, "Password: ");
            return userService.getUser(login, password);

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
