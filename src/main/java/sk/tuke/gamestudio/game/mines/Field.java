package sk.tuke.gamestudio.game.mines;
import java.util.Random;


public class Field {

    private final Tile[][] tiles;

    private final int rowCount;

    private final int columnCount;

    private final int mineCount;

    private long startMillis;

    private int numberOfOpen = 0;

    private GameState state = GameState.PLAYING;


    public Field(int rowCount, int columnCount, int mineCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.mineCount = mineCount;
        tiles = new Tile[rowCount][columnCount];

        generate();
    }

    public void openTile(int row, int column) {
        Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.OPEN);
            numberOfOpen++;

            if (tile instanceof Mine) {
                setState(GameState.FAILED);
                getPlayingTime();
                return;
            }

            if (tile instanceof Clue && ((Clue) tile).getValue() == 0) {
                openAdjacentTiles(row, column);
            }

            if (isSolved()) {
                setState(GameState.SOLVED);
                getPlayingTime();
            }
        }
    }

    public void markTile(int row, int column) {
        Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.MARKED);
        } else if (tile.getState() == Tile.State.MARKED) {
            tile.setState(Tile.State.CLOSED);
        }
    }

    private void generate() {
        generateMines();
        fillWithClues();
        startMillis = System.currentTimeMillis();
    }

    private void fillWithClues() {

        for (int row = 0; row < getRowCount(); row++) {
            for (int column = 0; column < getColumnCount(); column++) {
                if (tiles[row][column] == null)
                    tiles[row][column] = new Clue(countAdjacentMines(row, column));
            }
        }
    }

    private void generateMines() {
        int generateMine = 1;
        Random random = new Random();

        while (generateMine <= getMineCount()) {
            int row = random.nextInt(getRowCount());
            int column = random.nextInt(getColumnCount());

            if (tiles[row][column] == null) {
                tiles[row][column] = new Mine();
                generateMine++;
            }
        }
    }

    public boolean isSolved() {
        return rowCount * columnCount - mineCount == getNumberOfOpen();
    }

    private int getNumberOfOpen() {
        int numberOfOpen = 0;
        for (int row = 0; row < getRowCount(); row++) {
            for (int column = 0; column < getColumnCount(); column++) {
                if (tiles[row][column].getState() == Tile.State.OPEN)
                    numberOfOpen++;
            }
        }
        return numberOfOpen;
    }


    public int getNumberOfMarked() {
        int numberOfMarked = getMineCount();
        for (int row = 0; row < getRowCount(); row++) {
            for (int column = 0; column < getColumnCount(); column++) {
                if (tiles[row][column].getState() == Tile.State.MARKED)
                    numberOfMarked--;
                }
        }
        return numberOfMarked;
    }


    private int countAdjacentMines(int row, int column) {
        int count = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < getRowCount()) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < getColumnCount()) {
                        if (tiles[actRow][actColumn] instanceof Mine) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }


    private void openAdjacentTiles(int row, int column) {
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < getRowCount()) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < getColumnCount()) {
                        openTile(actRow, actColumn);
                    }
                }
            }
        }
    }


    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }


    public Tile getTile(int row, int column) {
        return tiles[row][column];
    }

    public int getPlayingTime() {
        return ((int) (System.currentTimeMillis() - startMillis)) / 1000;
    }

    public int getScoreOfGame(){
        if (getState() == GameState.SOLVED)
            return rowCount*columnCount*3 - getPlayingTime() +7;
        else if (getState() == GameState.FAILED)
            return 0;
        return rowCount*columnCount*3 - getPlayingTime() +7;
    }

}
