package sk.tuke.gamestudio.game.mines;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConnectionProperties {
    private ConnectionProperties() {}

    public static final String URL = "jdbc:postgresql://localhost/test";
    public static final String LOGIN = "postgres";
    public static final String PASSWORD = "978eqw";

    public static void main(String[] args) throws Exception {
        try (Connection c = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             Statement s = c.createStatement()) {
        }
    }
}
