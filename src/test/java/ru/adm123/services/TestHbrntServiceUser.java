package ru.adm123.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
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
    public void testUpdUserByLogin_inputDataIsNormalQueryIsError_returnERROR() {

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

    @Test
    public void testUpdUserById_inputDataIsNormalQueryIsSuccess_returnERROR() {

        int id = 1;
        String newLogin = " 456 ";
        String newPassword = " 789 ";
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
    public void testUpdUserById_inputDataIsNormalQueryIsSuccess_returnSUCCESS() {

        int id = 1;
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

    private void returnUpdUserByIdINPUTDATAERROR(int id, String newLogin, String newPassword) {

        ServiceUser.Result result = serviceUser.updUser(id, newLogin, newPassword);

        Mockito.verify(utilString, times(1)).isContainEmptyString(newLogin, newPassword);
        Mockito.verify(sessionFactory, never()).openSession();
        Assert.assertSame(result, ServiceUser.Result.INPUT_DATA_ERROR);

    }

    @Test
    public void testDelUserById_idIsZero_returnINPUTDATAERROR() {

        int id = 0;

        ServiceUser.Result result = serviceUser.delUser(id);

        Mockito.verify(sessionFactory, never()).openSession();
        Assert.assertSame(result, INPUT_DATA_ERROR);

    }

    @Test
    public void testDelUserById_idIsNumber_returnSUCCESS() {

        int id = 1;
        user = new ApplicationUser.Builder()
                .id(1)
                .login("123")
                .password("456")
                .build();
        userList = new ArrayList<>();
        userList.add(user);
        Mockito.when(applicationUserQuery.getResultList()).thenReturn(userList);

        ServiceUser.Result result = serviceUser.delUser(id);

        Mockito.verify(sessionFactory, times(1)).openSession();
        Mockito.verify(session, times(1)).beginTransaction();
        Mockito.verify(session, times(1)).createQuery(anyString(), eq(ApplicationUser.class));
        Mockito.verify(applicationUserQuery, times(1)).setParameter("id", id);
        Mockito.verify(applicationUserQuery, times(1)).getResultList();
        Mockito.verify(session, times(1)).delete(user);
        Mockito.verify(transaction, times(1)).commit();
        Assert.assertSame(result, SUCCESS);

    }

    @Test
    public void testDelUserById_idIsNumber_returnERROR() {

        int id = 1;
        userList = new ArrayList<>();
        Mockito.when(applicationUserQuery.getResultList()).thenReturn(userList);

        ServiceUser.Result result = serviceUser.delUser(id);

        Mockito.verify(sessionFactory, times(1)).openSession();
        Mockito.verify(session, times(1)).beginTransaction();
        Mockito.verify(session, times(1)).createQuery(anyString(), eq(ApplicationUser.class));
        Mockito.verify(applicationUserQuery, times(1)).setParameter("id", id);
        Mockito.verify(applicationUserQuery, times(1)).getResultList();
        Mockito.verify(session, never()).delete(user);
        Mockito.verify(transaction, never()).commit();
        Assert.assertSame(result, ERROR);

    }

    @Test
    public void testDelUserByLogin_loginIsNull_returnINPUTDATAERROR() {
        returnDelUserByLoginINPUTDATAERROR(null);
    }

    @Test
    public void testDelUserByLogin_loginIsEmpty_returnINPUTDATAERROR() {
        returnDelUserByLoginINPUTDATAERROR("");
    }

    @Test
    public void testDelUserByLogin_loginIsSpaces_returnINPUTDATAERROR() {
        returnDelUserByLoginINPUTDATAERROR("    ");
    }

    @Test
    public void testDelUserByLogin_loginIsNormal_returnSUCCESS() {

        String login = " 1234 ";
        user = new ApplicationUser.Builder()
                .id(1)
                .login(login)
                .password("456")
                .build();
        userList = new ArrayList<>();
        userList.add(user);
        Mockito.when(applicationUserQuery.getResultList()).thenReturn(userList);

        ServiceUser.Result result = serviceUser.delUser(login);

        Mockito.verify(sessionFactory, times(1)).openSession();
        Mockito.verify(session, times(1)).beginTransaction();
        Mockito.verify(session, times(1)).createQuery(anyString(), eq(ApplicationUser.class));
        Mockito.verify(applicationUserQuery, times(1)).setParameter("login", login);
        Mockito.verify(applicationUserQuery, times(1)).getResultList();
        Mockito.verify(session, times(1)).delete(user);
        Mockito.verify(transaction, times(1)).commit();
        Assert.assertSame(result, SUCCESS);

    }

    @Test
    public void testDelUserByLogin_loginIsNormal_returnERROR() {

        String login = " 1234 ";
        userList = new ArrayList<>();
        Mockito.when(applicationUserQuery.getResultList()).thenReturn(userList);

        ServiceUser.Result result = serviceUser.delUser(login);

        Mockito.verify(sessionFactory, times(1)).openSession();
        Mockito.verify(session, times(1)).beginTransaction();
        Mockito.verify(session, times(1)).createQuery(anyString(), eq(ApplicationUser.class));
        Mockito.verify(applicationUserQuery, times(1)).setParameter("login", login);
        Mockito.verify(applicationUserQuery, times(1)).getResultList();
        Mockito.verify(session, never()).delete(user);
        Mockito.verify(transaction, never()).commit();
        Assert.assertSame(result, ERROR);

    }

    private void returnDelUserByLoginINPUTDATAERROR(String login) {

        ServiceUser.Result result = serviceUser.delUser(login);

        Mockito.verify(utilString, times(1)).isEmpty(login);
        Mockito.verify(sessionFactory, never()).openSession();
        Assert.assertSame(result, ServiceUser.Result.INPUT_DATA_ERROR);

    }

    @Test
    public void getUserById_idIsZero_returnNULL() {

        int id = 0;

        ApplicationUser result = serviceUser.getUser(id);

        Mockito.verify(sessionFactory, never()).openSession();
        Assert.assertSame(result, null);

    }

    @Test
    public void getUserById_idIsNormal_returnNULL() {

        int id = 1;
        userList = new ArrayList<>();
        Mockito.when(applicationUserQuery.getResultList()).thenReturn(userList);
        ApplicationUser result = serviceUser.getUser(id);

        Mockito.verify(sessionFactory, times(1)).openSession();
        Mockito.verify(session, times(1)).createQuery(anyString(), eq(ApplicationUser.class));
        Mockito.verify(applicationUserQuery, times(1)).setParameter("id", id);
        Mockito.verify(applicationUserQuery, times(1)).getResultList();
        Assert.assertSame(result, null);

    }

    @Test
    public void getUserById_idIsNormal_returnSUCCESS() {

        int id = 1;
        user = new ApplicationUser.Builder()
                .id(1)
                .login("123")
                .password("456")
                .build();
        userList = new ArrayList<>();
        userList.add(user);
        Mockito.when(applicationUserQuery.getResultList()).thenReturn(userList);
        ApplicationUser result = serviceUser.getUser(id);

        Mockito.verify(sessionFactory, times(1)).openSession();
        Mockito.verify(session, times(1)).createQuery(anyString(), eq(ApplicationUser.class));
        Mockito.verify(applicationUserQuery, times(1)).setParameter("id", id);
        Mockito.verify(applicationUserQuery, times(1)).getResultList();
        Assert.assertSame(result, user);

    }

    @Test
    public void getUserByLogin_loginIsNull_returnNULL() {
        returnGetUserByLoginNULL(null);
    }

    @Test
    public void getUserByLogin_loginIsEmpty_returnNULL() {
        returnGetUserByLoginNULL("");
    }

    @Test
    public void getUserByLogin_loginIsSpaces_returnNULL(){
        returnGetUserByLoginNULL("    ");
    }

    @Test
    public void getUserByLogin_loginIsNormal_returnNULL() {

        String login = "123";
        userList = new ArrayList<>();
        Mockito.when(applicationUserQuery.getResultList()).thenReturn(userList);
        ApplicationUser result = serviceUser.getUser(login);

        Mockito.verify(sessionFactory, times(1)).openSession();
        Mockito.verify(session, times(1)).createQuery(anyString(), eq(ApplicationUser.class));
        Mockito.verify(applicationUserQuery, times(1)).setParameter("login", login);
        Mockito.verify(applicationUserQuery, times(1)).getResultList();
        Assert.assertSame(result, null);

    }

    @Test
    public void getUserByLogin_loginIsNormal_returnSUCCESS() {

        String login = "123";
        user = new ApplicationUser.Builder()
                .id(1)
                .login(login)
                .password("456")
                .build();
        userList = new ArrayList<>();
        userList.add(user);
        Mockito.when(applicationUserQuery.getResultList()).thenReturn(userList);
        ApplicationUser result = serviceUser.getUser(login);

        Mockito.verify(sessionFactory, times(1)).openSession();
        Mockito.verify(session, times(1)).createQuery(anyString(), eq(ApplicationUser.class));
        Mockito.verify(applicationUserQuery, times(1)).setParameter("login", login);
        Mockito.verify(applicationUserQuery, times(1)).getResultList();
        Assert.assertSame(result, user);

    }

    private void returnGetUserByLoginNULL(String login) {

        ApplicationUser result = serviceUser.getUser(login);

        Mockito.verify(utilString, times(1)).isEmpty(login);
        Mockito.verify(sessionFactory, never()).openSession();
        Assert.assertSame(result, null);

    }

    @Test
    public void getUserByLoginPassword_loginIsNull_returnNULL() {
        returnGetUserByLoginPasswordNULL(null, "456");
    }

    @Test
    public void getUserByLoginPassword_loginIsEmpty_returnNULL() {
        returnGetUserByLoginPasswordNULL("", "456");
    }

    @Test
    public void getUserByLoginPassword_loginIsSpaces_returnNULL() {
        returnGetUserByLoginPasswordNULL("    ", "456");
    }

    @Test
    public void getUserByLoginPassword_passwordIsNull_returnNULL() {
        returnGetUserByLoginPasswordNULL("123", null);
    }

    @Test
    public void getUserByLoginPassword_passwordIsEmpty_returnNULL() {
        returnGetUserByLoginPasswordNULL("123", "");
    }

    @Test
    public void getUserByLoginPassword_passwordIsSpaces_returnNULL() {
        returnGetUserByLoginPasswordNULL("123", "   ");
    }

    @Test
    public void getUserByLoginPassword_dataIsNull_returnNULL() {
        returnGetUserByLoginPasswordNULL(null, null);
    }

    @Test
    public void getUserByLoginPassword_dataIsEmpty_returnNULL() {
        returnGetUserByLoginPasswordNULL("", "");
    }

    @Test
    public void getUserByLoginPassword_dataIsSpaces_returnNULL() {
        returnGetUserByLoginPasswordNULL("   ", "   ");
    }

    private void returnGetUserByLoginPasswordNULL(String login, String password) {

        ApplicationUser result = serviceUser.getUser(login, password);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, password);
        Mockito.verify(sessionFactory, never()).openSession();
        Assert.assertSame(result, null);

    }

    @Test
    public void getUserByLoginPassword_dataIsNormal_returnSUCCESS() {

        String login = "123";
        String password = "456";
        user = new ApplicationUser.Builder()
                .id(1)
                .login(login)
                .password(password)
                .build();
        userList = new ArrayList<>();
        userList.add(user);
        Mockito.when(applicationUserQuery.getResultList()).thenReturn(userList);

        ApplicationUser result = serviceUser.getUser(login, password);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, password);
        Mockito.verify(sessionFactory, times(1)).openSession();
        Mockito.verify(session, times(1)).createQuery(anyString(), eq(ApplicationUser.class));
        Mockito.verify(applicationUserQuery, times(1)).setParameter("login", login);
        Mockito.verify(applicationUserQuery, times(1)).setParameter("password", password);
        Mockito.verify(applicationUserQuery, times(1)).getResultList();
        Assert.assertSame(result, user);

    }

    @Test
    public void getUserByLoginPassword_dataIsNormal_returnNULL() {

        String login = "123";
        String password = "456";
        userList = new ArrayList<>();
        Mockito.when(applicationUserQuery.getResultList()).thenReturn(userList);

        ApplicationUser result = serviceUser.getUser(login, password);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, password);
        Mockito.verify(sessionFactory, times(1)).openSession();
        Mockito.verify(session, times(1)).createQuery(anyString(), eq(ApplicationUser.class));
        Mockito.verify(applicationUserQuery, times(1)).setParameter("login", login);
        Mockito.verify(applicationUserQuery, times(1)).setParameter("password", password);
        Mockito.verify(applicationUserQuery, times(1)).getResultList();
        Assert.assertSame(result, null);

    }

}
