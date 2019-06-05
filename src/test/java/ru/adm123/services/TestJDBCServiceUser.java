package ru.adm123.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;
import ru.adm123.domains.ApplicationUser;
import ru.adm123.utils.UtilString;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;


@RunWith(MockitoJUnitRunner.class)
public class TestJDBCServiceUser {

    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    @Spy
    private UtilString utilString;
    @InjectMocks
    private ServiceUser serviceUser = JDBCServiceUser.getInstance(connection);

    @Before
    public void beforeMethod() throws SQLException {
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    public void testAddUser_loginIsNull_returnINPUTDATAERROR() throws SQLException {
        returnAddUserINPUTDATAERROR(null, "1234");
    }

    @Test
    public void testAddUser_passwordIsNull_returnINPUTDATAERROR() throws SQLException {
        returnAddUserINPUTDATAERROR("1234", null);
    }

    @Test
    public void testAddUser_loginIsEmpty_returnINPUTDATAERROR() throws SQLException {
        returnAddUserINPUTDATAERROR("", "1234");
    }

    @Test
    public void testAddUser_passwordIsEmpty_returnINPUTDATAERROR() throws SQLException {
        returnAddUserINPUTDATAERROR("1234", "");
    }

    @Test
    public void testAddUser_loginIsSpaces_returnINPUTDATAERROR() throws SQLException {
        returnAddUserINPUTDATAERROR("    ", "1234");
    }

    @Test
    public void testAddUser_passwordIsSpaces_returnINPUTDATAERROR() throws SQLException {
        returnAddUserINPUTDATAERROR("1234", "    ");
    }

    @Test
    public void testAddUser_inputDataIsNull_returnINPUTDATAERROR() throws SQLException {
        returnAddUserINPUTDATAERROR(null, null);
    }

    @Test
    public void testAddUser_inputDataIsEmpty_returnINPUTDATAERROR() throws SQLException {
        returnAddUserINPUTDATAERROR("    ", "    ");
    }

    @Test
    public void testAddUser_inputDataIsSpaces_returnINPUTDATAERROR() throws SQLException {
        returnAddUserINPUTDATAERROR("", "");
    }

    @Test
    public void testAddUser_inputDataIsNormalQueryIsSuccess_returnSUCCESS() throws SQLException {

        String login = " 1234 ";
        String password = " 5678 ";
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

        ServiceUser.Result result = serviceUser.addUser(login, password);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, password);
        Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        Assert.assertSame(result, ServiceUser.Result.SUCCESS);

    }

    @Test
    public void testAddUser_inputDataIsNormalQueryIsError_returnERROR() throws SQLException {

        String login = " 1234 ";
        String password = " 5678 ";
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(-1);

        ServiceUser.Result result = serviceUser.addUser(login, password);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, password);
        Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        Assert.assertSame(result, ServiceUser.Result.ERROR);

    }

    private void returnAddUserINPUTDATAERROR (String login, String password) throws SQLException {

        ServiceUser.Result result = serviceUser.addUser(login, password);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, password);
        Mockito.verify(connection, never()).prepareStatement(anyString());
        Mockito.verify(preparedStatement, never()).executeUpdate();
        Assert.assertSame(result, ServiceUser.Result.INPUT_DATA_ERROR);

    }

    @Test
    public void testUpdUserByLogin_loginIsNull_returnINPUTDATAERROR() throws SQLException {
        returnUpdUserByLoginINPUTDATAERROR(null, "123", "456");
    }

    @Test
    public void testUpdUserByLogin_newLoginIsNull_returnINPUTDATAERROR() throws SQLException {
        returnUpdUserByLoginINPUTDATAERROR("123", null, "456");
    }

    @Test
    public void testUpdUserByLogin_newPasswordIsNull_returnINPUTDATAERROR() throws SQLException {
        returnUpdUserByLoginINPUTDATAERROR("123", "456", null);
    }

    @Test
    public void testUpdUserByLogin_loginIsEmpty_returnINPUTDATAERROR() throws SQLException {
        returnUpdUserByLoginINPUTDATAERROR("", "123", "456");
    }

    @Test
    public void testUpdUserByLogin_newLoginIsEmpty_returnINPUTDATAERROR() throws SQLException {
        returnUpdUserByLoginINPUTDATAERROR("123", "", "456");
    }

    @Test
    public void testUpdUserByLogin_newPasswordIsEmpty_returnINPUTDATAERROR() throws SQLException {
        returnUpdUserByLoginINPUTDATAERROR("123", "456", "");
    }

    @Test
    public void testUpdUserByLogin_loginIsSpaces_returnINPUTDATAERROR() throws SQLException {
        returnUpdUserByLoginINPUTDATAERROR("    ", "123", "456");
    }

    @Test
    public void testUpdUserByLogin_newLoginIsSpaces_returnINPUTDATAERROR() throws SQLException {
        returnUpdUserByLoginINPUTDATAERROR("123", "    ", "456");
    }

    @Test
    public void testUpdUserByLogin_newPasswordIsSpaces_returnINPUTDATAERROR() throws SQLException {
        returnUpdUserByLoginINPUTDATAERROR("123", "456", "    ");
    }

    @Test
    public void testUpdUserByLogin_inputDataIsNormalQueryIsSuccess_returnSUCCESS() throws SQLException {

        String login = " 123 ";
        String newLogin = " 456 ";
        String newPassword = " 789 ";
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

        ServiceUser.Result result = serviceUser.updUser(login, newLogin, newPassword);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, newLogin, newPassword);
        Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        Assert.assertSame(result, ServiceUser.Result.SUCCESS);

    }

    @Test
    public void testUpdUserByLogin_inputDataIsNormalQueryIsError_returnERROR() throws SQLException {

        String login = " 123 ";
        String newLogin = " 456 ";
        String newPassword = " 789 ";
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(-1);

        ServiceUser.Result result = serviceUser.updUser(login, newLogin, newPassword);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, newLogin, newPassword);
        Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        Assert.assertSame(result, ServiceUser.Result.ERROR);

    }

    private void returnUpdUserByLoginINPUTDATAERROR(String login, String newLogin, String newPassword) throws SQLException {

        ServiceUser.Result result = serviceUser.updUser(login, newLogin, newPassword);

        Mockito.verify(utilString, times(1)).isContainEmptyString(login, newLogin, newPassword);
        Mockito.verify(connection, never()).prepareStatement(anyString());
        Mockito.verify(preparedStatement, never()).executeUpdate();
        Assert.assertSame(result, ServiceUser.Result.INPUT_DATA_ERROR);

    }

    @Test
    public void testUpdUserById_idIsZero_returnINPUTDATAERROR() throws SQLException {
        returnUpdUserByIdINPUTDATAERROR(0, "123", "456");
    }

    @Test
    public void testUpdUserById_newLoginIsNull_returnINPUTDATAERROR() throws SQLException {
        returnUpdUserByIdINPUTDATAERROR(1, null, "456");
    }

    @Test
    public void testUpdUserById_newPasswordIsNull_returnINPUTDATAERROR() throws SQLException {
        returnUpdUserByIdINPUTDATAERROR(1, "456", null);
    }

    @Test
    public void testUpdUserById_newLoginIsEmpty_returnINPUTDATAERROR() throws SQLException {
        returnUpdUserByIdINPUTDATAERROR(1, "", "456");
    }

    @Test
    public void testUpdUserById_newPasswordIsEmpty_returnINPUTDATAERROR() throws SQLException {
        returnUpdUserByIdINPUTDATAERROR(1, "456", "");
    }

    @Test
    public void testUpdUserById_newLoginIsSpaces_returnINPUTDATAERROR() throws SQLException {
        returnUpdUserByIdINPUTDATAERROR(1, "    ", "456");
    }

    @Test
    public void testUpdUserById_newPasswordIsSpaces_returnINPUTDATAERROR() throws SQLException {
        returnUpdUserByIdINPUTDATAERROR(1, "456", "    ");
    }

    @Test
    public void testUpdUserById_inputDataIsNormalQueryIsSuccess_returnSUCCESS() throws SQLException {

        int id = 1;
        String newLogin = " 456 ";
        String newPassword = " 789 ";
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

        ServiceUser.Result result = serviceUser.updUser(id, newLogin, newPassword);

        Mockito.verify(utilString, times(1)).isContainEmptyString(newLogin, newPassword);
        Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        Assert.assertSame(result, ServiceUser.Result.SUCCESS);

    }

    @Test
    public void testUpdUserById_inputDataIsNormalQueryIsError_returnERROR() throws SQLException {

        int id = 1;
        String newLogin = " 456 ";
        String newPassword = " 789 ";
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(-1);

        ServiceUser.Result result = serviceUser.updUser(id, newLogin, newPassword);

        Mockito.verify(utilString, times(1)).isContainEmptyString(newLogin, newPassword);
        Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        Assert.assertSame(result, ServiceUser.Result.ERROR);

    }

    private void returnUpdUserByIdINPUTDATAERROR(int id, String newLogin, String newPassword) throws SQLException {

        ServiceUser.Result result = serviceUser.updUser(id, newLogin, newPassword);

        Mockito.verify(utilString, times(1)).isContainEmptyString(newLogin, newPassword);
        Mockito.verify(connection, never()).prepareStatement(anyString());
        Mockito.verify(preparedStatement, never()).executeUpdate();
        Assert.assertSame(result, ServiceUser.Result.INPUT_DATA_ERROR);

    }

    @Test
    public void testDelUserById_idIsZero_returnINPUTDATAERROR() throws SQLException {

        int id = 0;

        ServiceUser.Result result = serviceUser.delUser(id);

        Mockito.verify(connection, never()).prepareStatement(anyString());
        Mockito.verify(preparedStatement, never()).executeUpdate();
        Assert.assertSame(result, ServiceUser.Result.INPUT_DATA_ERROR);

    }

    @Test
    public void testDelUserById_idIsNumber_returnSUCCESS() throws SQLException {

        int id = 1;
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

        ServiceUser.Result result = serviceUser.delUser(id);

        Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        Assert.assertSame(result, ServiceUser.Result.SUCCESS);

    }

    @Test
    public void testDelUserById_idIsNumber_returnERROR() throws SQLException {

        int id = 1;
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(-1);

        ServiceUser.Result result = serviceUser.delUser(id);

        Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        Assert.assertSame(result, ServiceUser.Result.ERROR);

    }

    @Test
    public void testDelUserByLogin_loginIsNull_returnINPUTDATAERROR() throws SQLException {
        returnDelUserByLoginINPUTDATAERROR(null);
    }

    @Test
    public void testDelUserByLogin_loginIsEmpty_returnINPUTDATAERROR() throws SQLException {
        returnDelUserByLoginINPUTDATAERROR("");
    }

    @Test
    public void testDelUserByLogin_loginIsSpaces_returnINPUTDATAERROR() throws SQLException {
        returnDelUserByLoginINPUTDATAERROR("    ");
    }

    @Test
    public void testDelUserByLogin_loginIsNormal_returnSUCCESS() throws SQLException {

        String login = " 1234 ";
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

        ServiceUser.Result result = serviceUser.delUser(login);

        Mockito.verify(utilString, times(1)).isEmpty(login);
        Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        Assert.assertSame(result, ServiceUser.Result.SUCCESS);

    }

    @Test
    public void testDelUserByLogin_loginIsNormal_returnERROR() throws SQLException {

        String login = " 1234 ";
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(-1);

        ServiceUser.Result result = serviceUser.delUser(login);

        Mockito.verify(utilString, times(1)).isEmpty(login);
        Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).executeUpdate();
        Assert.assertSame(result, ServiceUser.Result.ERROR);

    }

    private void returnDelUserByLoginINPUTDATAERROR(String login) throws SQLException {

        ServiceUser.Result result = serviceUser.delUser(login);

        Mockito.verify(utilString, times(1)).isEmpty(login);
        Mockito.verify(connection, never()).prepareStatement(anyString());
        Mockito.verify(preparedStatement, never()).executeUpdate();
        Assert.assertSame(result, ServiceUser.Result.INPUT_DATA_ERROR);

    }

    @Test
    public void getUserById_idIsZero_returnNULL() throws SQLException {

        int id = 0;

        ApplicationUser result = serviceUser.getUser(id);

        Mockito.verify(connection, never()).prepareStatement(anyString());
        Mockito.verify(preparedStatement, never()).executeUpdate();
        Assert.assertSame(result, null);

    }

    @Test
    public void getUserById_idIsNormal_returnNULL() throws SQLException {

        int id = 1;
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.getRow()).thenReturn(0);

        ApplicationUser result = serviceUser.getUser(id);

        Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(resultSet, times(1)).getRow();

        Assert.assertSame(result, null);

    }

    @Test
    public void getUserById_idIsNormal_returnSUCCESS() throws SQLException {

        int id = 1;
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.getRow()).thenReturn(1);

        ApplicationUser result = serviceUser.getUser(id);
        result.setId(id);

        Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(resultSet, times(1)).getRow();

        Assert.assertSame(result.getId(), id);

    }

    @Test
    public void getUserByLogin_loginIsNull_returnNULL() throws SQLException {
        returnGetUserByLoginNULL(null);
    }

    @Test
    public void getUserByLogin_loginIsEmpty_returnNULL() throws SQLException {
        returnGetUserByLoginNULL("");
    }

    @Test
    public void getUserByLogin_loginIsSpaces_returnNULL() throws SQLException {
        returnGetUserByLoginNULL("    ");
    }

    @Test
    public void getUserByLogin_loginIsNormal_returnNULL() throws SQLException {

        String login = "123";
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.getRow()).thenReturn(0);

        ApplicationUser result = serviceUser.getUser(login);

        Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(resultSet, times(1)).getRow();

        Assert.assertSame(result, null);

    }

    @Test
    public void getUserByLogin_loginIsNormal_returnSUCCESS() throws SQLException {

        String login = "123";
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.getRow()).thenReturn(1);

        ApplicationUser result = serviceUser.getUser(login);
        result.setUserLogin(login);

        Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(resultSet, times(1)).getRow();

        Assert.assertSame(result.getUserLogin(), login);

    }

    private void returnGetUserByLoginNULL(String login) throws SQLException {

        ApplicationUser result = serviceUser.getUser(login);

        Mockito.verify(utilString, times(1)).isEmpty(login);
        Mockito.verify(connection, never()).prepareStatement(anyString());
        Mockito.verify(preparedStatement, never()).executeQuery();
        Assert.assertSame(result, null);

    }

    @Test
    public void getUserByLoginPassword_loginIsNull_returnNULL() throws SQLException {
        returnGetUserByLoginPasswordNULL(null, "456");
    }

    @Test
    public void getUserByLoginPassword_loginIsEmpty_returnNULL() throws SQLException {
        returnGetUserByLoginPasswordNULL("", "456");
    }

    @Test
    public void getUserByLoginPassword_loginIsSpaces_returnNULL() throws SQLException {
        returnGetUserByLoginPasswordNULL("    ", "456");
    }

    @Test
    public void getUserByLoginPassword_passwordIsNull_returnNULL() throws SQLException {
        returnGetUserByLoginPasswordNULL("123", null);
    }

    @Test
    public void getUserByLoginPassword_passwordIsEmpty_returnNULL() throws SQLException {
        returnGetUserByLoginPasswordNULL("123", "");
    }

    @Test
    public void getUserByLoginPassword_passwordIsSpaces_returnNULL() throws SQLException {
        returnGetUserByLoginPasswordNULL("123", "   ");
    }

    @Test
    public void getUserByLoginPassword_dataIsNull_returnNULL() throws SQLException {
        returnGetUserByLoginPasswordNULL(null, null);
    }

    @Test
    public void getUserByLoginPassword_dataIsEmpty_returnNULL() throws SQLException {
        returnGetUserByLoginPasswordNULL("", "");
    }

    @Test
    public void getUserByLoginPassword_dataIsSpaces_returnNULL() throws SQLException {
        returnGetUserByLoginPasswordNULL("   ", "   ");
    }

    private void returnGetUserByLoginPasswordNULL(String login, String password) throws SQLException {

        ApplicationUser user = serviceUser.getUser(login, password);
        Mockito.verify(utilString, times(1)).isContainEmptyString(login, password);
        Mockito.verify(connection, never()).prepareStatement(anyString());
        Mockito.verify(preparedStatement, never()).executeQuery();
        Assert.assertSame(user, null);

    }

    @Test
    public void getUserByLoginPassword_dataIsNormal_returnSUCCESS() throws SQLException {

        String login = " 1234 ";
        String password = " 5678 ";
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.getRow()).thenReturn(1);

        ApplicationUser result = serviceUser.getUser(login, password);
        result.setUserLogin(login);
        result.setPassword(password);

        Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(resultSet, times(1)).getRow();
        Assert.assertSame(result.getUserLogin(), login);
        Assert.assertSame(result.getPassword(), password);

    }

    @Test
    public void getUserByLoginPassword_dataIsNormal_returnNULL() throws SQLException {

        String login = " 1234 ";
        String password = " 5678 ";
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.getRow()).thenReturn(0);

        ApplicationUser result = serviceUser.getUser(login, password);

        Mockito.verify(connection, times(1)).prepareStatement(anyString());
        Mockito.verify(preparedStatement, times(1)).executeQuery();
        Mockito.verify(resultSet, times(1)).next();
        Mockito.verify(resultSet, times(1)).getRow();
        Assert.assertSame(result, null);

    }

}
