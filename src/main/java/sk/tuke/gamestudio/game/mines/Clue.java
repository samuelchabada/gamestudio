package sk.tuke.gamestudio.game.mines;

/**
 * Clue tile.
 */
public class Clue  extends Tile {
    private final int value;

    /**
     * Constructor.
     *
     * @param value value of the clue
     */
    public Clue(int value) {
        this.value = value;
    }


    /**
     * Value of the clue.
     */
    public int getValue() {
        return value;
    }


}
