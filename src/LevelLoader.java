/**
 * Loads level data onto the GameBoard.
 * Grid is 5 columns (0-4) x 4 rows (0-3). Row 0 = top, Col 0 = left.
 * Each level is one case
 */
public class LevelLoader {

    public static void loadLevel(GameBoard board, int levelNumber) {
        board.clearBoard();

        switch (levelNumber) {

            case 1:
                // Starter level 1
                board.addPiece(new Piece(PieceType.SNOWBALL_SMALL, 0, 1, "none"));
                board.addPiece(new Piece(PieceType.HEAD,           3, 1, "blue"));
                board.addPiece(new Piece(PieceType.SNOWBALL_LARGE, 3, 3, "none"));
                break;

            case 2:
                // Starter level 2
                board.addPiece(new Piece(PieceType.SNOWBALL_LARGE, 1, 0, "none"));
                board.addPiece(new Piece(PieceType.TREE,           1, 1, "none"));
                board.addPiece(new Piece(PieceType.HEAD,           1, 3, "yellow"));
                board.addPiece(new Piece(PieceType.SNOWBALL_SMALL, 2, 3, "none"));
                break;

            case 3:
                // Starter level 3
                board.addPiece(new Piece(PieceType.TREE,           0, 0, "none"));
                board.addPiece(new Piece(PieceType.SNOWBALL_SMALL, 0, 2, "none"));
                board.addPiece(new Piece(PieceType.SNOWBALL_LARGE, 1, 2, "none"));
                board.addPiece(new Piece(PieceType.HEAD,           2, 1, "red"));
                board.addPiece(new Piece(PieceType.TREE,           3, 1, "none"));
                break;

            default:
                System.out.println("Level " + levelNumber + " not yet implemented.");
                break;
        }
    }
}