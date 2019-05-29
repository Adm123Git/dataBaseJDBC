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
        //serviceUser.addUser("hiberUser_1", "qwerty_1");

        sessionFactory.close();

    }

}
