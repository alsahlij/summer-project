import java.util.ArrayList;
import java.util.List;

/**
 * Represents the 5x4 game board and all pieces on it.
 * Handles piece lookup and board state.
 */
public class GameBoard {

    public static final int ROWS = 4;
    public static final int COLS = 5;

    private List<Piece> pieces;

    public GameBoard() {
        pieces = new ArrayList<>();
    }

    // Adds a piece to the board
    public void addPiece(Piece piece) {
        pieces.add(piece);
    }


    // Returns the piece at the given row and col, or null if empty.
    public Piece getPieceAt(int row, int col) {
        for (Piece p : pieces) {
            if (p.getRow() == row && p.getCol() == col) {
                return p;
            }
        }
        return null;
    }

    // Returns all pieces on the board
    public List<Piece> getPieces() {
        return pieces;
    }


    // Removes a piece from the board (used when snowball falls off).
    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    /**
     * Checks whether all snowmen are fully assembled.
     * A win means no loose large snowballs, small snowballs, or heads remain.
     */
    public boolean checkWin() {
        for (Piece p : pieces) {
            if (p.getType() == PieceType.SNOWBALL_LARGE) return false;
            if (p.getType() == PieceType.SNOWBALL_SMALL) return false;
            if (p.getType() == PieceType.HEAD) return false;
        }
        return true;
    }


    // Clears all pieces from the board
    public void clearBoard() {
        pieces.clear();
    }

    /**
     * Moves a piece in the given direction.
     * 0=UP, 1=DOWN, 2=LEFT, 3=RIGHT
     * Returns false if the piece goes off the board.
     */
    public boolean movePiece(Piece piece, int direction) {

        // Non-movable pieces
        if (piece.getType() == PieceType.TREE) return true;
        if (piece.getType() == PieceType.HEAD) return true;
        if (piece.isStacked()) return true;

        int dRow = 0;
        int dCol = 0;

        // Direction handling
        if (direction == 0) dRow = -1;
        if (direction == 1) dRow =  1;
        if (direction == 2) dCol = -1;
        if (direction == 3) dCol =  1;

        int newRow = piece.getRow() + dRow;
        int newCol = piece.getCol() + dCol;

        // Slide until blocked or out of bounds
        while (true) {

            // Out of board  remove piece
            if (newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLS) {
                removePiece(piece);
                return false;
            }

            Piece occupant = getPieceAt(newRow, newCol);
            if (occupant != null) {
                // Stop before collision
                piece.setRow(newRow - dRow);
                piece.setCol(newCol - dCol);
                return true;
            }

            newRow += dRow;
            newCol += dCol;
        }
    }
}