package dbUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Executor<T> {
    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public void execUpdate(String update) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(update);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public <T> T execQuery(String query,
                           ResultHandler<T> handler) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            ResultSet result = statement.getResultSet();
            return handler.handle(result);
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
