package dao;

import dbUtils.Executor;
import models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBC_UserDAO implements UserDAO {


    private final Connection connection;
    private final Executor<User> executor;

    public JDBC_UserDAO() throws ClassNotFoundException, SQLException {
        String url = "jdbc:postgresql://localhost:5432/Module3";
        String name = "postgres";
        String password = "1qazPOSTGRE";

        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(url, name, password);
        executor = new Executor<>(connection);
    }

    @Override
    public void save(User user) {
        String command = "INSERT INTO users VALUES (?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            preparedStatement.setString(1,user.getLogin());
            preparedStatement.setString(2,user.getPassword());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findByLogin(String login) {
        String command = "SELECT * FROM users WHERE login=?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(command);
            preparedStatement.setString(1,login);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return executor.execQuery(preparedStatement, resultSet -> {
            if (!resultSet.isBeforeFirst()) {
                return null;
            } else {
                resultSet.next();
                User user = new User(resultSet.getString("login"),
                        resultSet.getString("password"));
                return user;
            }
        });
    }
}
