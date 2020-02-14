package dao;

import models.User;

import java.sql.*;

@Deprecated
public class JDBC_UserDAO implements UserDAO {


    private final Connection connection;

    public JDBC_UserDAO() throws ClassNotFoundException, SQLException {
        String url = "jdbc:postgresql://localhost:5432/Module3";
        String name = "postgres";
        String password = "1qazPOSTGRE";

        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(url, name, password);
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
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                return null;
            } else {
                resultSet.next();
                User user = new User(resultSet.getString("login"),
                        resultSet.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void banControlByLogin(String login, boolean value) {

    }
}
