package sk.tuke.gamestudio.game.puzzle;

import java.util.Random;


public class Field {

    private int tiles[][];

    private int rowCount;

    private int columnCount;

    private long startTime;

    public static final int EMPTY_TILE = 0;

    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        tiles = new int[rowCount][columnCount];

        generateField();
    }

    public void generateField() {
        generateValues();
        shuffleValues();
        startTime = System.currentTimeMillis();
    }

    public void generateValues() {
        int value = 1;
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                tiles[row][column] = value;
                value++;
            }
        }tiles[rowCount - 1][columnCount - 1] = EMPTY_TILE;
    }

    public void shuffleValues() {
        Random random = new Random();
        for (int i = 0; i < 100 * rowCount * columnCount; ) {
            int tile = random.nextInt(rowCount * columnCount - 1) + 1;
            try {
                moveTile(tile);
                i++;
            } catch (PuzzleException e) {
            }
        }
    }

    private Position getPositionOfTile(int tile) {
        for (int row = 0; row < rowCount; row++)
            for (int column = 0; column < columnCount; column++)
                if (tile == getTile(row, column))
                    return new Position(row, column);
        return null;
    }

    public void moveTile(int tile) throws PuzzleException {
        if (tile <= 0 || tile >= rowCount * columnCount)
            throw new PuzzleException();
        Position position = getPositionOfTile(tile);
        Position emptyPosition = getPositionOfTile(EMPTY_TILE);
        if ((position.getRow() == emptyPosition.getRow() && Math.abs(emptyPosition.getColumn() - position.getColumn()) == 1) ||
                (position.getColumn() == emptyPosition.getColumn() && Math.abs(emptyPosition.getRow() - position.getRow()) == 1)) {
            tiles[emptyPosition.getRow()][emptyPosition.getColumn()] = tile;
            tiles[position.getRow()][position.getColumn()] = EMPTY_TILE;
            return;
        }
        throw new PuzzleException();
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getTile(int row, int column) {
        return tiles[row][column];
    }

    public boolean isSolved() {
        int value = 1;
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (tiles[row][column] != EMPTY_TILE && tiles[row][column] != value)
                    return false;
                value++;
            }
        }
        return true;
    }

    public long getPlayingTime() {
        return ((System.currentTimeMillis() - startTime) / 1000);
    }

    public int getScore(){
        return (int) (rowCount*columnCount*33 - getPlayingTime() + 3);

    }

}
