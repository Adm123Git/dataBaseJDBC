package ru.adm123.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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

import static org.mockito.Matchers.anyString;
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
    @Spy
    private UtilString utilString;
    @InjectMocks
    private ServiceUser serviceUser = HbrntServiceUser.getInstance(sessionFactory);

    @Before
    public void beforeMethod() {
        Mockito.when(sessionFactory.openSession()).thenReturn(session);
        Mockito.when(session.beginTransaction()).thenReturn(transaction);
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

    private void returnAddUserINPUTDATAERROR(String login, String password) {

        ServiceUser.Result result = serviceUser.addUser(login, password);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, password);
        Mockito.verify(sessionFactory, never()).openSession();
        Assert.assertSame(INPUT_DATA_ERROR, result);

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
        user = new ApplicationUser.Builder()
                .login(login)
                .password(password)
                .build();

        ServiceUser.Result result = serviceUser.addUser(login, password);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, password);
        Mockito.verify(sessionFactory, times(1)).openSession();
        Mockito.verify(session, times(1)).beginTransaction();
        Mockito.verify(session, times(1)).persist(user);
        Mockito.verify(transaction, times(1)).commit();
        Mockito.verify(session, times(1)).close();
        Assert.assertSame(SUCCESS, result);

    }


}
