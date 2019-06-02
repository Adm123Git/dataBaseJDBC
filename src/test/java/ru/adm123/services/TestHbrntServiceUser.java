package ru.adm123.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

import static org.mockito.Mockito.never;

@RunWith(MockitoJUnitRunner.class)
public class TestHbrntServiceUser {

    @Spy
    private UtilString utilString;
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @InjectMocks
    private ServiceUser serviceUser = HbrntServiceUser.getInstance(sessionFactory);

    @Before
    public void beforeMethod() {
        Mockito.when(sessionFactory.openSession()).thenReturn(session);
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

        boolean result = utilString.isContainEmptyString(login, password);

        Assert.assertTrue(result);
        Mockito.verify(sessionFactory, never()).openSession();

    }

}
