import java.util.*;

/**
 * Automatically solves any Snow Problem level using recursive backtracking.
 * Explores all possible moves and returns the sequence that wins.
 */
public class PuzzleSolver {

    // Maximum depth to search - prevents infinite loops on unsolvable states
    private static final int MAX_DEPTH = 25;

    // Stores the winning sequence of moves once found
    private List<int[]> solution;

    // Visited states to avoid loops
    private Set<String> visited;

    /**
     * Attempts to solve the given board.
     * Returns a list of moves if solved, or null if no solution found.
     */
    public List<int[]> solve(GameBoard board) {
        solution = null;
        visited  = new HashSet<>();

        // Take a deep copy of the board state to work with
        List<int[]> moves = new ArrayList<>();
        search(board, moves, 0);

        return solution;
    }

    /**
     * Recursive backtracking search.
     * Tries every possible move from the current board state.
     */
    private boolean search(GameBoard board, List<int[]> moves, int depth) {
        // Stop if too deep
        if (depth > MAX_DEPTH) return false;

        // Check if already won
        if (board.checkWin()) {
            solution = new ArrayList<>(moves);
            return true;
        }

        // Get a string representing current board state
        String state = getBoardState(board);

        // Skip if we have visited this state before
        if (visited.contains(state)) return false;
        visited.add(state);

        // Get all moveable pieces (small and large snowballs that are not stacked)
        List<Piece> moveablePieces = new ArrayList<>();
        for (Piece p : board.getPieces()) {
            if ((p.getType() == PieceType.SNOWBALL_SMALL
                    || p.getType() == PieceType.SNOWBALL_LARGE)
                    && !p.isStacked()) {
                moveablePieces.add(p);
            }
        }

        // Try each piece in each of 4 directions
        for (int i = 0; i < moveablePieces.size(); i++) {
            for (int dir = 0; dir < 4; dir++) {

                // Save board state before this move
                String savedState = getBoardState(board);

                // Try the move
                Piece piece = moveablePieces.get(i);

                // Find the actual piece on the board by position
                Piece actualPiece = board.getPieceAt(piece.getRow(), piece.getCol());
                if (actualPiece == null) continue;
                if (actualPiece.isStacked()) continue;

                // Record original position
                int origRow = actualPiece.getRow();
                int origCol = actualPiece.getCol();

                // Make a deep copy of the board for this branch
                GameBoard boardCopy = deepCopy(board);

                // Find the piece in the copy
                Piece copyPiece = boardCopy.getPieceAt(origRow, origCol);
                if (copyPiece == null) continue;

                // Apply move on the copy
                boolean success = boardCopy.movePiece(copyPiece, dir);
                if (!success) continue; // piece fell off - skip

                // Apply stacking on copy
                boardCopy.checkAndApplyStacking(copyPiece);

                // Recurse
                moves.add(new int[]{origRow, origCol, dir});
                if (search(boardCopy, moves, depth + 1)) {
                    return true;
                }
                moves.remove(moves.size() - 1);
            }
        }

        return false;
    }


    // Creates a deep copy of the board with all pieces copied.
    private GameBoard deepCopy(GameBoard original) {
        GameBoard copy = new GameBoard();
        for (Piece p : original.getPieces()) {
            Piece newPiece = new Piece(p.getType(), p.getRow(), p.getCol(), p.getColour());
            newPiece.setStacked(p.isStacked());
            copy.addPiece(newPiece);
        }
        return copy;
    }


    // Converts board state to a unique string for visited set.

    private String getBoardState(GameBoard board) {
        List<String> parts = new ArrayList<>();
        for (Piece p : board.getPieces()) {
            parts.add(p.getType() + "," + p.getRow() + ","
                    + p.getCol() + "," + p.isStacked() + "," + p.getColour());
        }
        Collections.sort(parts);
        return String.join("|", parts);
    }
}