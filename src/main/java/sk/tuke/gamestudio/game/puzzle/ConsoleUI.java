package sk.tuke.gamestudio.game.puzzle;


import java.io.*;

public class ConsoleUI {

    public Field field;
    public BufferedReader input = new BufferedReader(new InputStreamReader(System.in));



    public String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public void newGameStarted(Field field) {
        this.field = field;
        do {
            update();
            processInput();
        } while (!field.isSolved());
        if (field.isSolved())
            System.out.println(field.getPlayingTime());
        System.out.println("vyhral si");
        update();
    }

    public void update() {
        for (int row = 0; row < field.getRowCount(); row++) {
            for (int column = 0; column < field.getColumnCount(); column++) {
                int tile = field.getTile(row, column);
                if (tile == Field.EMPTY_TILE)
                    System.out.print("   ");
                else
                    System.out.printf("%3d",tile);
            }
            System.out.println();
        }
    }


    public void processInput() {
        System.out.println(".....................................");

        String input = readLine();
        int tile = 0;
        try {
            tile = Integer.parseInt(input);
            field.moveTile(tile);
        } catch (NumberFormatException e) {
            System.out.println("Zly vstup");
        } catch (PuzzleException e) {
            System.out.println("Zla dlazdica");
        }
    }
}
