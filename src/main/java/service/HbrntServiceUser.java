package service;

import org.hibernate.SessionFactory;
import util.UtilString;

import java.util.Objects;

public class HbrntServiceUser {

    private static HbrntServiceUser instance = null;
    private static UtilString serviceString = UtilString.getInstance();
    private SessionFactory sessionFactory;

    private HbrntServiceUser(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public static HbrntServiceUser getInstance(SessionFactory sessionFactory) {
        if (Objects.isNull(instance)) {
            instance = new HbrntServiceUser(sessionFactory);
        }
        return instance;
    }

}
