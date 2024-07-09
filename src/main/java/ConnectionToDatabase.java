import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionToDatabase {
    static Connection connection = null;

    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/D:/Soft/H2/bin/municipalities", "sa", "sa");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
