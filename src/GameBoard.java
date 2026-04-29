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
            if (p.getType() == PieceType.HEAD && !p.isStacked()) return false;
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

    // Applies stacking rules for adjacent pieces.
    public void checkAndApplyStacking(Piece movedPiece) {
        if (movedPiece == null) return;

        if (movedPiece.getType() == PieceType.SNOWBALL_SMALL && !movedPiece.isStacked()) {
            Piece adjacentLarge = findAdjacentOfType(movedPiece, PieceType.SNOWBALL_LARGE);
            if (adjacentLarge != null && !adjacentLarge.isStacked()) {
                // Merge: remove small, mark large as stacked (shows snowman_stack image)
                pieces.remove(movedPiece);
                adjacentLarge.setStacked(true);

                checkHeadStacking(adjacentLarge);
                return;
            }
        }

        if (movedPiece.getType() == PieceType.SNOWBALL_LARGE && !movedPiece.isStacked()) {
            Piece adjacentSmall = findAdjacentOfType(movedPiece, PieceType.SNOWBALL_SMALL);
            if (adjacentSmall != null && !adjacentSmall.isStacked()) {
                // Merge: remove small, mark large as stacked
                pieces.remove(adjacentSmall);
                movedPiece.setStacked(true);

                checkHeadStacking(movedPiece);
                return;
            }
        }

        if (movedPiece.getType() == PieceType.HEAD && !movedPiece.isStacked()) {
            checkHeadStacking(movedPiece);
        }
    }


    // Checks if any head is adjacent to the given stack piece.
    private void checkHeadStacking(Piece stackPiece) {
        // stackPiece is the large snowball that is now stacked
        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
        for (int[] d : directions) {
            int nr = stackPiece.getRow() + d[0];
            int nc = stackPiece.getCol() + d[1];
            Piece neighbour = getPieceAt(nr, nc);
            if (neighbour != null && neighbour.getType() == PieceType.HEAD
                    && !neighbour.isStacked()) {
                // Complete the snowman
                stackPiece.setType(PieceType.HEAD);
                stackPiece.setColour(neighbour.getColour());
                stackPiece.setStacked(true);
                pieces.remove(neighbour);
                return;
            }
        }

        // Also check if the moved head is itself adjacent to a stack
        if (stackPiece.getType() == PieceType.HEAD) {
            Piece adjacentStack = findAdjacentStackedLarge(stackPiece);
            if (adjacentStack != null) {
                adjacentStack.setType(PieceType.HEAD);
                adjacentStack.setColour(stackPiece.getColour());
                adjacentStack.setStacked(true);
                pieces.remove(stackPiece);
            }
        }
    }

    // Returns adjacent piece of given type (4 directions).
    private Piece findAdjacentOfType(Piece piece, PieceType type) {
        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
        for (int[] d : directions) {
            Piece neighbour = getPieceAt(piece.getRow() + d[0], piece.getCol() + d[1]);
            if (neighbour != null && neighbour.getType() == type) {
                return neighbour;
            }
        }
        return null;
    }


    // Returns adjacent stacked large snowball
    private Piece findAdjacentStackedLarge(Piece piece) {
        int[][] directions = {{-1,0},{1,0},{0,-1},{0,1}};
        for (int[] d : directions) {
            Piece neighbour = getPieceAt(piece.getRow() + d[0], piece.getCol() + d[1]);
            if (neighbour != null
                    && neighbour.getType() == PieceType.SNOWBALL_LARGE
                    && neighbour.isStacked()) {
                return neighbour;
            }
        }
        return null;
    }
}