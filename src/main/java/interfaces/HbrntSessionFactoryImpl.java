package interfaces;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HbrntSessionFactoryImpl implements HbrntSessionFactory {

    private static final HbrntSessionFactory instance = new HbrntSessionFactoryImpl();
    private static final SessionFactory factory;

    static {
        Configuration configuration = new Configuration().configure("/configuration.cfg.xml");
        factory = configuration.buildSessionFactory();
    }

    private HbrntSessionFactoryImpl(){}

    public static HbrntSessionFactory getInstance() {
        return instance;
    }

    @Override
    public SessionFactory getFactory() {
        return factory;
    }

}
