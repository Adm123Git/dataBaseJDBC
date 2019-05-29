package service;

import domain.ApplicationUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.UtilString;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HbrntServiceUser {

    private static HbrntServiceUser instance = null;
    private static UtilString serviceString = UtilString.getInstance();
    private SessionFactory sessionFactory;

    public enum Result {INPUT_DATA_ERROR, SUCCESS, ERROR}

    private HbrntServiceUser(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public static HbrntServiceUser getInstance(SessionFactory sessionFactory) {
        if (Objects.isNull(instance)) {
            instance = new HbrntServiceUser(sessionFactory);
        }
        return instance;
    }

    public Result addUser(String login, String password) {

        if (serviceString.isContainEmptyString(login, password)) {
            return Result.INPUT_DATA_ERROR;
        }

        ApplicationUser appUser = new ApplicationUser.Builder()
                .login(login)
                .password(password)
                .build();

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(appUser);
            transaction.commit();
            return Result.SUCCESS;
        } catch (Exception e) {
            return Result.ERROR;
        }

    }

    public void printUserList() {

        try (Session session = sessionFactory.openSession()) {
            List<ApplicationUser> resultList = session.createQuery(
                    "from ApplicationUser", ApplicationUser.class)
                    .getResultList();
            if (resultList.size() == 0) {
                System.out.println("Empty table `users`");
            } else {
                resultList.forEach(user -> System.out.println(user.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
