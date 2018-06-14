package sk.tuke.gamestudio.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.mines.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.concurrent.TimeUnit;

import static sk.tuke.gamestudio.game.mines.ConnectionProperties.LOGIN;
import static sk.tuke.gamestudio.game.mines.ConnectionProperties.PASSWORD;
import static sk.tuke.gamestudio.game.mines.ConnectionProperties.URL;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MinesController {
    private Field field = new Field(9, 9, 1);
    private static final String SELECT = "SELECT date, time_of_game score FROM minesplayer";
    private static final String INSERT = "INSERT INTO minesplayer (date, score) VALUES (?, ?, ?)";

    private boolean marking;

    public String score(){
        return  "Score: " + String.valueOf(field.getScoreOfGame()) +" points" ;
    }

    public String marked(){
        return "Marked: " + String.valueOf(field.getNumberOfMarked());
    }

//    public String name(){
//        return player.setName();
//    }

    @RequestMapping("/mark")
    public String mark() {
        marking = !marking;
        return "mines";
    }

    @RequestMapping("/mines")
    public String mines(@RequestParam(value = "row", required = false) String rowString,
                        @RequestParam(value = "column", required = false) String columnString) {
        try {
            int row = Integer.parseInt(rowString);
            int column = Integer.parseInt(columnString);
            if (marking)
                field.markTile(row, column);
            else {
                field.openTile(row, column);
                if (field.getState() == GameState.SOLVED) {
                    try {
                        storeToDatabase();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }return "mines";
                }
            }
        } catch (NumberFormatException e) {}

        return "mines";
    }


    @RequestMapping("/minesNew")
    public String minesNew() {
        field = new Field(9, 9, 1);
        return "mines";
    }

    public String printField() {
        Formatter f = new Formatter();
        f.format("<table class='minetable'>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            f.format("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTile(row, column);
                f.format("<td>\n");
                f.format("<a href='/mines?row=%d&column=%d'>\n", row, column);
                f.format("<img src='/Images/mines/%s.png'>", getImageName(tile));
                f.format("</a>");
                f.format("</td>\n");
            }
            f.format("</tr>\n");
        }
        f.format("</table>\n");
        return f.toString();
    }


    private Object getImageName(Tile tile) {
        if (field.getState() == GameState.PLAYING){
            switch (tile.getState()) {
                case CLOSED:
                    return "closed";
                case MARKED:
                    return "marked";
                case OPEN:
                    if (tile instanceof Clue)
                        return "open" + ((Clue) tile).getValue();
                    return "mine";
            }

        }else if (field.getState() == GameState.SOLVED) {
            switch (tile.getState()) {
                case CLOSED:
                    if (tile instanceof Mine)
                        return "marked";
                    return "closed";
                case MARKED:
                    if (tile instanceof Mine)
                        return "detected";
                    return "marked";
                case OPEN:
                    if (tile instanceof Clue)
                        return "open" + ((Clue) tile).getValue();
                    return "mine";
            }
        }else  {
            switch (tile.getState()) {
                case CLOSED:
                    if (tile instanceof Mine)
                        return "undetected";
                    else if (tile instanceof Clue)
                        return "open" + ((Clue) tile).getValue();
                    return  "closed";
                case MARKED:
                    if (tile instanceof Mine)
                        return "detected";
                    return "marked";
                case OPEN:
                    if (tile instanceof Clue)
                        return "open" + ((Clue) tile).getValue();
                    return "mine";
            }
        }
        throw new IllegalArgumentException("Not supported tile type");
    }


    public boolean isMarking() {
        return marking;
    }

    public GameState getGameState() {
        return field.getState();
    }

    public void storeToDatabase() throws SQLException {
        try (Connection c = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            try(PreparedStatement s = c.prepareStatement(INSERT)) {
//                s.setString(1, String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
//                s.setString(1, String.valueOf(
//                        TimeUnit.MILLISECONDS.toSeconds(field.getPlayingTime()) -
//                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(field.getPlayingTime()))));
                s.setString(1, String.valueOf(Calendar.getInstance().getTime()));
                s.setString(2, String.valueOf(TimeUnit.MILLISECONDS.toMinutes(field.getPlayingTime())));
                s.setString(3, String.valueOf(field.getScoreOfGame()));

                s.executeUpdate();
            }
        }
    }
}
