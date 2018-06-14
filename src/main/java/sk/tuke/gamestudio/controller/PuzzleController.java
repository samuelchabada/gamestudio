package sk.tuke.gamestudio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sk.tuke.gamestudio.game.puzzle.Field;
import sk.tuke.gamestudio.game.puzzle.PuzzleException;

import java.util.Formatter;

import static sk.tuke.gamestudio.game.puzzle.Field.EMPTY_TILE;



@Controller
public class PuzzleController {
    private Field field = new Field(3,3);


    public String score(){
        return  "Score: " + String.valueOf(field.getScore() + " points");
    }

    public String solved(){
        if (field.isSolved() == false)
            return "Playing";
        else if (field.isSolved() == true)
            return "Solved";
        return String.valueOf(field.isSolved());
    }

        @RequestMapping("/puzzle")
    public String puzzle(@RequestParam(value = "tile", required = false) String tileString) {

        try {
            int tile = Integer.parseInt(tileString);
            field.moveTile(tile);

        }catch (NumberFormatException  | PuzzleException e) {}

        return "puzzle";
    }


    @RequestMapping("/puzzleNew")
    public String puzzleNew() {
        field = new Field(3, 3);
        return "puzzle";
    }

    public String printField() {
        Formatter f = new Formatter();
        f.format("<table class='puzzletable'>\n");
        for(int row = 0; row < field.getRowCount(); row++) {
            f.format("<tr>\n");
            for(int column = 0; column < field.getColumnCount(); column++) {
                int value = field.getTile(row,column);
                f.format("<td>\n");
                f.format("<a href='/puzzle?tile=%d'>\n", value);
                f.format("<img src='/Images/puzzle/64/%s.png'>",getImageValue(value));
                f.format("</a>");
                f.format("</td>\n");
            }
            f.format("</tr>\n");
        }
        f.format("</table>\n");
        return f.toString();
    }


    private Object getImageValue(int values) {
        switch (values){
            case 1 : return "1";
            case 2 : return "2";
            case 3 : return "3";
            case 4 : return "4";
            case 5 : return "5";
            case 6 : return "6";
            case 7 : return "7";
            case 8 : return "8";
            case 9 : return "9";
            case EMPTY_TILE: return "cross";

        }
        throw new IllegalArgumentException("Not supported tile type");
    }


}

