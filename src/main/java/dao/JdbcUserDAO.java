package dao;

import dbUtils.Executor;
import models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUserDAO implements UserDAO {


    private final Connection connection;
    private final Executor<User> executor;

    public JdbcUserDAO() throws ClassNotFoundException, SQLException {
        String url = "jdbc:postgresql://localhost:5432/Module3";
        String name = "postgres";
        String password = "1qazPOSTGRE";

        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(url, name, password);
        executor = new Executor<>(connection);
    }

    @Override
    public void save(User user) {
        StringBuilder command = new StringBuilder();
        command.append("INSERT INTO users VALUES ('")
                .append(user.getLogin())
                .append("','")
                .append(user.getPassword())
                .append("')");
        executor.execUpdate(command.toString());
    }

    @Override
    public User findByLogin(String login) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM users WHERE login='")
                .append(login).append("'");
        return executor.execQuery(stringBuilder.toString(), resultSet -> {
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
