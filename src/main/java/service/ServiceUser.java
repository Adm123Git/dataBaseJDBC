package service;

import annotation.Overload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Objects;

public class ServiceUser {

    private static ServiceUser instance = null;
    private static ServiceString serviceString = ServiceString.getInstance();
    private Connection connection;

    enum Result {INPUT_DATA_ERROR, SUCCESS, ERROR, USER_EXIST, USER_NOT_FOUND}

    private ServiceUser(Connection connection) {
        this.connection = connection;
    }

    public static ServiceUser getInstance(Connection connection) {
        if (Objects.isNull(instance)) {
            instance = new ServiceUser(connection);
        }
        return instance;
    }

    public Result addUser(String login, String password) {

        if (serviceString.isContainEmptyString(login, password)) {
            return Result.INPUT_DATA_ERROR;
        }

        try {
            PreparedStatement addStatement = connection.prepareStatement("insert into users (login, password) values (?, ?)");
            addStatement.setString(1, login);
            addStatement.setString(2, password);
            return addStatement.executeUpdate() == 1 ? Result.SUCCESS : Result.USER_EXIST;
        } catch (Exception e) {
            return Result.ERROR;
        }

    }

    public Result updUser(String login, String newLogin, String newPassword) {

        if (serviceString.isContainEmptyString(login, newLogin, newPassword)) {
            return Result.INPUT_DATA_ERROR;
        }

        try {
            PreparedStatement updStatement = connection.prepareStatement("update users set (login = ?, password = ?) where login = ?");
            updStatement.setString(1, newLogin);
            updStatement.setString(2, newPassword);
            updStatement.setString(3, login);
            return updStatement.executeUpdate() == 1 ? Result.SUCCESS : Result.USER_NOT_FOUND;
        } catch (Exception e) {
            return Result.ERROR;
        }

    }

    @Overload
    public Result delUser(int id) {

        if (id < 1) {
            return Result.INPUT_DATA_ERROR;
        }

        try {
            PreparedStatement delStatement = connection.prepareStatement("delete from users where id = ?");
            delStatement.setLong(1, id);
            return delStatement.executeUpdate() == 1 ? Result.SUCCESS : Result.USER_NOT_FOUND;
        } catch (Exception e) {
            return Result.ERROR;
        }

    }

    @Overload
    public Result delUser(String login) {

        if (serviceString.isEmpty(login)) {
            return Result.INPUT_DATA_ERROR;
        }

        try {
            PreparedStatement delStatement = connection.prepareStatement("delete from users where login = ?");
            delStatement.setString(1, login);
            return delStatement.executeUpdate() == 1 ? Result.SUCCESS : Result.USER_NOT_FOUND;
        } catch (Exception e) {
            return Result.ERROR;
        }

    }

}
