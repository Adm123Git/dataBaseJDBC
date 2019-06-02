package ru.adm123.services;

import ru.adm123.annotations.Overload;
import com.sun.istack.internal.Nullable;
import ru.adm123.domains.ApplicationUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.adm123.utils.UtilString;

import java.util.List;
import java.util.Objects;

public class HbrntServiceUser implements ServiceUser {

    private static HbrntServiceUser instance = null;
    private static UtilString utilString = UtilString.getInstance();
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

    @Override
    @Overload
    public Result addUser(String login, String password) {

        if (utilString.isContainEmptyString(login, password)) {
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

    @Override
    @Overload
    public Result updUser(String login, String newLogin, String newPassword) {

        if (utilString.isContainEmptyString(login, newLogin, newPassword)) {
            return Result.INPUT_DATA_ERROR;
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            List<ApplicationUser> resultList = session.createQuery("from ApplicationUser where userLogin = :login", ApplicationUser.class)
                    .setParameter("login", login)
                    .getResultList();

            if (resultList.size() != 1) {
                return Result.ERROR;
            } else {
                ApplicationUser appUser = resultList.get(0);
                appUser.setUserLogin(newLogin);
                appUser.setPassword(newPassword);
                session.update(appUser);
                transaction.commit();
                return Result.SUCCESS;
            }
        } catch (Exception e) {
            return Result.ERROR;
        }

    }

    @Override
    @Overload
    public Result updUser(int id, String newLogin, String newPassword) {

        if (utilString.isContainEmptyString(newLogin, newPassword) || id < 1) {
            return Result.INPUT_DATA_ERROR;
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            List<ApplicationUser> resultList = session.createQuery("from ApplicationUser where id = :id", ApplicationUser.class)
                    .setParameter("id", id)
                    .getResultList();

            if (resultList.size() != 1) {
                return Result.ERROR;
            } else {
                ApplicationUser appUser = resultList.get(0);
                appUser.setUserLogin(newLogin);
                appUser.setPassword(newPassword);
                session.update(appUser);
                transaction.commit();
                return Result.SUCCESS;
            }
        } catch (Exception e) {
            return Result.ERROR;
        }

    }

    @Override
    @Overload
    public Result delUser(int id) {

        if (id < 1) {
            return Result.INPUT_DATA_ERROR;
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            List<ApplicationUser> resultList = session.createQuery("from ApplicationUser where id = :id", ApplicationUser.class)
                    .setParameter("id", id)
                    .getResultList();

            if (resultList.size() != 1) {
                return Result.ERROR;
            } else {
                ApplicationUser appUser = resultList.get(0);
                session.delete(appUser);
                transaction.commit();
                return Result.SUCCESS;
            }
        } catch (Exception e) {
            return Result.ERROR;
        }

    }

    @Override
    @Overload
    public Result delUser(String login) {

        if (utilString.isEmpty(login)) {
            return Result.INPUT_DATA_ERROR;
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            List<ApplicationUser> resultList = session.createQuery("from ApplicationUser where userLogin = :login", ApplicationUser.class)
                    .setParameter("login", login)
                    .getResultList();

            if (resultList.size() != 1) {
                return Result.ERROR;
            } else {
                ApplicationUser appUser = resultList.get(0);
                session.delete(appUser);
                transaction.commit();
                return Result.SUCCESS;
            }
        } catch (Exception e) {
            return Result.ERROR;
        }

    }

    @Nullable
    @Override
    @Overload
    public ApplicationUser getUser(int id) {

        if (id < 1) {
            return null;
        }

        try (Session session = sessionFactory.openSession()) {
            List<ApplicationUser> resultList = session.createQuery("from ApplicationUser where id = :id", ApplicationUser.class)
                    .setParameter("id", id)
                    .getResultList();
            return resultList.size() == 1 ? resultList.get(0) : null;
        } catch (Exception e) {
            return null;
        }

    }

    @Nullable
    @Override
    @Overload
    public ApplicationUser getUser(String login) {

        if (utilString.isEmpty(login)) {
            return null;
        }

        try (Session session = sessionFactory.openSession()) {
            List<ApplicationUser> resultList = session.createQuery("from ApplicationUser where userLogin = :login", ApplicationUser.class)
                    .setParameter("login", login)
                    .getResultList();
            return resultList.size() == 1 ? resultList.get(0) : null;
        } catch (Exception e) {
            return null;
        }

    }

    @Nullable
    @Override
    @Overload
    public ApplicationUser getUser(String login, String password) {

        if (utilString.isContainEmptyString(login, password)) {
            return null;
        }

        try (Session session = sessionFactory.openSession()) {
            List<ApplicationUser> resultList = session.createQuery("from ApplicationUser where userLogin = :login and password = :password", ApplicationUser.class)
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .getResultList();
            return resultList.size() == 1 ? resultList.get(0) : null;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public void printUserList() {

        try (Session session = sessionFactory.openSession()) {
            List<ApplicationUser> resultList = session.createQuery("from ApplicationUser", ApplicationUser.class)
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
