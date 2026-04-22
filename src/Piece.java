/**
 * Represents a single piece on the game board.
 * Stores the type, position, colour, and stacking state of the piece.
 */
public class Piece {

    private PieceType type;
    private int row;
    private int col;
    private String colour; // "red", "blue", "yellow" - used for heads and snowmen
    private boolean isStacked; // true if this piece is part of a completed stack

    public Piece(PieceType type, int row, int col, String colour) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.colour = colour;
        this.isStacked = false;
    }

    // Getters

    public PieceType getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getColour() {
        return colour;
    }

    public boolean isStacked() {
        return isStacked;
    }

    // Setters

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setStacked(boolean stacked) {
        this.isStacked = stacked;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type + "(" + colour + ") at [" + row + "," + col + "]";
    }
}