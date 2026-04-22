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
}