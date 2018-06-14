package sk.tuke.gamestudio.game.mines;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static sk.tuke.gamestudio.game.mines.ConnectionProperties.*;

public class CreateTable {
    private static final String CREATE_TABLE = "CREATE TABLE minesplayer (date VARCHAR(64) NOT NULL," +
            "time_of_game VARCHAR(64) NOT NULL, score VARCHAR(64) NOT NULL)";

    public static void main(String[] args) throws Exception {
        try (Connection c = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             Statement s = c.createStatement()) {
            s.executeUpdate(CREATE_TABLE);
        }
    }
}
