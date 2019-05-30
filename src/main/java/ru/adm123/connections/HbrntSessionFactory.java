package ru.adm123.connections;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HbrntSessionFactory {

    private static final HbrntSessionFactory instance = new HbrntSessionFactory();
    private static final SessionFactory factory;

    static {
        Configuration configuration = new Configuration().configure("/configuration.cfg.xml");
        factory = configuration.buildSessionFactory();
    }

    private HbrntSessionFactory(){}

    public static HbrntSessionFactory getInstance() {
        return instance;
    }

    public SessionFactory getFactory() {
        return factory;
    }

}
