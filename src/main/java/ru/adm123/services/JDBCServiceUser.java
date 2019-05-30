package ru.adm123.services;

import ru.adm123.annotations.Overload;
import com.sun.istack.internal.Nullable;
import ru.adm123.domains.ApplicationUser;
import ru.adm123.utils.UtilString;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class JDBCServiceUser implements ServiceUser {

    private static JDBCServiceUser instance = null;
    //private static UtilString utilString = UtilString.getInstance();
    private UtilString utilString;// = new UtilString();
    private Connection connection;

    /*public JDBCServiceUser(Connection connection) {
        this.connection = connection;
    }*/

    private JDBCServiceUser(Connection connection) {
        this.connection = connection;
    }

    public static JDBCServiceUser getInstance(Connection connection) {
        if (Objects.isNull(instance)) {
            instance = new JDBCServiceUser(connection);
        }
        return instance;
    }

    public Result addUser(String login, String password) {

        if (utilString.isContainEmptyString(login, password)) {
            return Result.INPUT_DATA_ERROR;
        }

        try {
            PreparedStatement addStatement = connection.prepareStatement("insert into users (login, password) values (?, ?)");
            addStatement.setString(1, login);
            addStatement.setString(2, password);
            return addStatement.executeUpdate() == 1 ? Result.SUCCESS : Result.ERROR;
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

        try {
            PreparedStatement updStatement = connection.prepareStatement("update users set login = ?, password = ? where login = ?");
            updStatement.setString(1, newLogin);
            updStatement.setString(2, newPassword);
            updStatement.setString(3, login);
            return updStatement.executeUpdate() == 1 ? Result.SUCCESS : Result.ERROR;
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

        try {
            PreparedStatement updStatement = connection.prepareStatement("update users set login = ?, password = ? where id = ?");
            updStatement.setString(1, newLogin);
            updStatement.setString(2, newPassword);
            updStatement.setInt(3, id);
            return updStatement.executeUpdate() == 1 ? Result.SUCCESS : Result.ERROR;
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

        try {
            PreparedStatement delStatement = connection.prepareStatement("delete from users where id = ?");
            delStatement.setInt(1, id);
            return delStatement.executeUpdate() == 1 ? Result.SUCCESS : Result.ERROR;
        } catch (Exception e) {
            return Result.ERROR;
        }

    }

    @Overload
    public Result delUser(String login) {

        if (utilString.isEmpty(login)) {
            return Result.INPUT_DATA_ERROR;
        }

        try {
            PreparedStatement delStatement = connection.prepareStatement("delete from users where login = ?");
            delStatement.setString(1, login);
            return delStatement.executeUpdate() == 1 ? Result.SUCCESS : Result.ERROR;
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

        try {
            PreparedStatement selectStatement = connection.prepareStatement("select * from users where id = ?");
            selectStatement.setInt(1, id);
            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next();
            if (resultSet.getRow() != 1) {
                return null;
            } else {
                return new ApplicationUser.Builder()
                        .id(resultSet.getInt("id"))
                        .login(resultSet.getString("login"))
                        .password(resultSet.getString("password"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        try {
            PreparedStatement selectStatement = connection.prepareStatement("select * from users where login = ?");
            selectStatement.setString(1, login);
            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next();
            if (resultSet.getRow() != 1) {
                return null;
            } else {
                return new ApplicationUser.Builder()
                        .id(resultSet.getInt("id"))
                        .login(resultSet.getString("login"))
                        .password(resultSet.getString("password"))
                        .build();
            }
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

        try {
            PreparedStatement selectStatement = connection.prepareStatement("select * from users where login = ? and password = ?");
            selectStatement.setString(1, login);
            selectStatement.setString(2, password);
            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next();
            if (resultSet.getRow() != 1) {
                return null;
            } else {
                return new ApplicationUser.Builder()
                        .id(resultSet.getInt("id"))
                        .login(resultSet.getString("login"))
                        .password(resultSet.getString("password"))
                        .build();
            }
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public void printUserList() {

        try {
            PreparedStatement selectStatement = connection.prepareStatement("select * from users order by id");
            ResultSet resultSet = selectStatement.executeQuery();
            int resultStringCount = 0;
            while (resultSet.next()) {
                System.out.println(
                        new ApplicationUser.Builder()
                                .id(resultSet.getInt("id"))
                                .login(resultSet.getString("login"))
                                .password(resultSet.getString("password"))
                                .build()
                                .toString()
                );
                resultStringCount = resultSet.getRow();
            }
            if (resultStringCount == 0) {
                System.out.println("Empty table `users`");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
