package dbUtils;

import java.sql.*;

public class Executor<T> {
    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public <T> T execQuery(PreparedStatement preparedStatement,
                           ResultHandler<T> handler) {
        try {
            preparedStatement.execute();
            ResultSet result = preparedStatement.getResultSet();
            return handler.handle(result);
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
