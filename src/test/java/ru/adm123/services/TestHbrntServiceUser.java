package ru.adm123.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.query.spi.QueryImplementor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import ru.adm123.domains.ApplicationUser;
import ru.adm123.utils.UtilString;

import javax.persistence.TypedQuery;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static ru.adm123.services.ServiceUser.Result.*;

@RunWith(MockitoJUnitRunner.class)
public class TestHbrntServiceUser {

    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Transaction transaction;
    @Mock
    private ApplicationUser user;
    @Mock
    private Query<ApplicationUser> applicationUserQuery;
    @Mock
    private List<ApplicationUser> userList;
    @Spy
    private UtilString utilString;
    @InjectMocks
    private ServiceUser serviceUser = HbrntServiceUser.getInstance(sessionFactory);

    @Before
    public void beforeMethod() {
        Mockito.when(sessionFactory.openSession()).thenReturn(session);
        Mockito.when(session.beginTransaction()).thenReturn(transaction);
        Mockito.when(session.createQuery(anyString(), eq(ApplicationUser.class))).thenReturn(applicationUserQuery);
        Mockito.when(applicationUserQuery.setParameter(anyString(), anyString())).thenReturn(applicationUserQuery);
    }

    @Test
    public void testAddUser_loginIsNull_returnINPUTDATAERROR() {
        returnAddUserINPUTDATAERROR(null, "456");
    }

    @Test
    public void testAddUser_passwordIsNull_returnINPUTDATAERROR() {
        returnAddUserINPUTDATAERROR("123", null);
    }

    @Test
    public void testAddUser_loginIsEmpty_returnINPUTDATAERROR() {
        returnAddUserINPUTDATAERROR("", "456");
    }

    @Test
    public void testAddUser_passwordIsEmpty_returnINPUTDATAERROR() {
        returnAddUserINPUTDATAERROR("123", "");
    }

    @Test
    public void testAddUser_loginIsSpaces_returnINPUTDATAERROR() {
        returnAddUserINPUTDATAERROR("   ", "456");
    }

    @Test
    public void testAddUser_passwordIsSpaces_returnINPUTDATAERROR() {
        returnAddUserINPUTDATAERROR("123", "   ");
    }

    @Test
    public void testAddUser_dataIsNull_returnINPUTDATAERROR() {
        returnAddUserINPUTDATAERROR(null, null);
    }

    @Test
    public void testAddUser_dataIsEmpty_returnINPUTDATAERROR() {
        returnAddUserINPUTDATAERROR("", "");
    }

    @Test
    public void testAddUser_dataIsSpaces_returnINPUTDATAERROR() {
        returnAddUserINPUTDATAERROR("   ", "   ");
    }

    @Test
    public void testAddUser_dataIsNormal_returnERROR() {
        String login = " 1234 ";
        String password = " 5678 ";
        Mockito.when(sessionFactory.openSession()).thenReturn(null);

        ServiceUser.Result result = serviceUser.addUser(login, password);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, password);
        Mockito.verify(sessionFactory, times(1)).openSession();
        Assert.assertSame(ERROR, result);

    }

    @Test
    public void testAddUser_dataIsNormal_returnSUCCESS() {
        String login = " 1234 ";
        String password = " 5678 ";
        user = new ApplicationUser.Builder().build();
        user.setUserLogin(login);
        user.setPassword(password);

        ServiceUser.Result result = serviceUser.addUser(login, password);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, password);
        Mockito.verify(sessionFactory, times(1)).openSession();
        Mockito.verify(session, times(1)).beginTransaction();
        Mockito.verify(session, times(1)).persist(user);
        Mockito.verify(transaction, times(1)).commit();
        Mockito.verify(session, times(1)).close();
        Assert.assertSame(SUCCESS, result);

    }

    private void returnAddUserINPUTDATAERROR(String login, String password) {

        ServiceUser.Result result = serviceUser.addUser(login, password);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, password);
        Mockito.verify(sessionFactory, never()).openSession();
        Assert.assertSame(INPUT_DATA_ERROR, result);

    }

    @Test
    public void testUpdUserByLogin_loginIsNull_returnINPUTDATAERROR() {
        returnUpdUserByLoginINPUTDATAERROR(null, "123", "456");
    }

    @Test
    public void testUpdUserByLogin_newLoginIsNull_returnINPUTDATAERROR() {
        returnUpdUserByLoginINPUTDATAERROR("123", null, "456");
    }

    @Test
    public void testUpdUserByLogin_newPasswordIsNull_returnINPUTDATAERROR() {
        returnUpdUserByLoginINPUTDATAERROR("123", "456", null);
    }

    @Test
    public void testUpdUserByLogin_loginIsEmpty_returnINPUTDATAERROR() {
        returnUpdUserByLoginINPUTDATAERROR("", "123", "456");
    }

    @Test
    public void testUpdUserByLogin_newLoginIsEmpty_returnINPUTDATAERROR() {
        returnUpdUserByLoginINPUTDATAERROR("123", "", "456");
    }

    @Test
    public void testUpdUserByLogin_newPasswordIsEmpty_returnINPUTDATAERROR() {
        returnUpdUserByLoginINPUTDATAERROR("123", "456", "");
    }

    @Test
    public void testUpdUserByLogin_loginIsSpaces_returnINPUTDATAERROR() {
        returnUpdUserByLoginINPUTDATAERROR("   ", "123", "456");
    }

    @Test
    public void testUpdUserByLogin_newLoginIsSpaces_returnINPUTDATAERROR() {
        returnUpdUserByLoginINPUTDATAERROR("123", "   ", "456");
    }

    @Test
    public void testUpdUserByLogin_newPasswordIsSpaces_returnINPUTDATAERROR() {
        returnUpdUserByLoginINPUTDATAERROR("123", "456", "   ");
    }

    @Test
    public void testUpdUserByLogin_inputDataIsNormalQueryIsSuccess_returnSUCCESS() {

        String login = " 123 ";
        String newLogin = " 456 ";
        String newPassword = " 789 ";
        user = new ApplicationUser.Builder()
                .id(1)
                .login(newLogin)
                .password(newPassword)
                .build();
        userList = new ArrayList<>();
        userList.add(user);
        Mockito.when(applicationUserQuery.getResultList()).thenReturn(userList);

        ServiceUser.Result result = serviceUser.updUser(login, newLogin, newPassword);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, newLogin, newPassword);
        Mockito.verify(sessionFactory, times(1)).openSession();
        Mockito.verify(session, times(1)).beginTransaction();
        Mockito.verify(session, times(1)).createQuery(anyString(), eq(ApplicationUser.class));
        Mockito.verify(applicationUserQuery, times(1)).setParameter("login", login);
        Mockito.verify(applicationUserQuery, times(1)).getResultList();
        Mockito.verify(session, times(1)).update(user);
        Mockito.verify(transaction, times(1)).commit();
        Assert.assertSame(result, SUCCESS);

    }

    @Test
    public void testUpdUserByLogin_inputDataIsNormalQueryIsSuccess_returnERROR() {

        String login = " 123 ";
        String newLogin = " 456 ";
        String newPassword = " 789 ";
        user = new ApplicationUser.Builder()
                .id(1)
                .login(newLogin)
                .password(newPassword)
                .build();
        userList = new ArrayList<>();
        Mockito.when(applicationUserQuery.getResultList()).thenReturn(userList);

        ServiceUser.Result result = serviceUser.updUser(login, newLogin, newPassword);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, newLogin, newPassword);
        Mockito.verify(sessionFactory, times(1)).openSession();
        Mockito.verify(session, times(1)).beginTransaction();
        Mockito.verify(session, times(1)).createQuery(anyString(), eq(ApplicationUser.class));
        Mockito.verify(applicationUserQuery, times(1)).setParameter("login", login);
        Mockito.verify(applicationUserQuery, times(1)).getResultList();
        Mockito.verify(session, never()).update(user);
        Mockito.verify(transaction, never()).commit();
        Assert.assertSame(result, ERROR);

    }

    private void returnUpdUserByLoginINPUTDATAERROR(String login, String newLogin, String newPassword) {

        ServiceUser.Result result = serviceUser.updUser(login, newLogin, newPassword);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, newLogin, newPassword);
        Mockito.verify(sessionFactory, never()).openSession();
        Assert.assertSame(result, ServiceUser.Result.INPUT_DATA_ERROR);

    }

    @Test
    public void testUpdUserById_IdIsZero_returnINPUTDATAERROR() {

        returnUpdUserByIdINPUTDATAERROR(0, " 123 ", " 456 ");

    }

    @Test
    public void testUpdUserByLogin_IdIsNumber_returnSUCCESS() {

        int id = 1;
        String newLogin = " 456 ";
        String newPassword = " 789 ";
        user = new ApplicationUser.Builder()
                .id(id)
                .login(newLogin)
                .password(newPassword)
                .build();
        userList = new ArrayList<>();
        userList.add(user);
        Mockito.when(applicationUserQuery.getResultList()).thenReturn(userList);

        ServiceUser.Result result = serviceUser.updUser(id, newLogin, newPassword);

        Mockito.verify(utilString, times(1)).isContainEmptyString(newLogin, newPassword);
        Mockito.verify(sessionFactory, times(1)).openSession();
        Mockito.verify(session, times(1)).beginTransaction();
        Mockito.verify(session, times(1)).createQuery(anyString(), eq(ApplicationUser.class));
        Mockito.verify(applicationUserQuery, times(1)).setParameter("id", id);
        Mockito.verify(applicationUserQuery, times(1)).getResultList();
        Mockito.verify(session, times(1)).update(user);
        Mockito.verify(transaction, times(1)).commit();
        Assert.assertSame(result, SUCCESS);

    }

    @Test
    public void testUpdUserByLogin_IdIsNumber_returnERROR() {
        int id = 1;
        String newLogin = " 456 ";
        String newPassword = " 789 ";
        user = new ApplicationUser.Builder()
                .id(1)
                .login(newLogin)
                .password(newPassword)
                .build();
        userList = new ArrayList<>();
        Mockito.when(applicationUserQuery.getResultList()).thenReturn(userList);

        ServiceUser.Result result = serviceUser.updUser(id, newLogin, newPassword);

        Mockito.verify(utilString, times(1)).isContainEmptyString(newLogin, newPassword);
        Mockito.verify(sessionFactory, times(1)).openSession();
        Mockito.verify(session, times(1)).beginTransaction();
        Mockito.verify(session, times(1)).createQuery(anyString(), eq(ApplicationUser.class));
        Mockito.verify(applicationUserQuery, times(1)).setParameter("id", id);
        Mockito.verify(applicationUserQuery, times(1)).getResultList();
        Mockito.verify(session, never()).update(user);
        Mockito.verify(transaction, never()).commit();
        Assert.assertSame(result, ERROR);

    }

    @Test
    public void testUpdUserById_newLoginIsNull_returnINPUTDATAERROR() {
        returnUpdUserByIdINPUTDATAERROR(1, null, " 456 ");
    }

    @Test
    public void testUpdUserById_newLoginIsEmpty_returnINPUTDATAERROR() {
        returnUpdUserByIdINPUTDATAERROR(1, "", " 456 ");
    }

    @Test
    public void testUpdUserById_newLoginIsSpaces_returnINPUTDATAERROR() {
        returnUpdUserByIdINPUTDATAERROR(1, "   ", " 456 ");
    }

    @Test
    public void testUpdUserById_newPasswordIsNull_returnINPUTDATAERROR() {
        returnUpdUserByIdINPUTDATAERROR(1, " 123 ", null);
    }

    @Test
    public void testUpdUserById_newPasswordIsEmpty_returnINPUTDATAERROR() {
        returnUpdUserByIdINPUTDATAERROR(1, " 123 ", "");
    }

    @Test
    public void testUpdUserById_newPasswordIsSpaces_returnINPUTDATAERROR() {
        returnUpdUserByIdINPUTDATAERROR(1, " 123 ", "   ");
    }

    private void returnUpdUserByIdINPUTDATAERROR(int id, String newLogin, String newPassword) {

        ServiceUser.Result result = serviceUser.updUser(id, newLogin, newPassword);

        Mockito.verify(utilString, times(1)).isContainEmptyString(newLogin, newPassword);
        Mockito.verify(sessionFactory, never()).openSession();
        Assert.assertSame(result, ServiceUser.Result.INPUT_DATA_ERROR);

    }

}
