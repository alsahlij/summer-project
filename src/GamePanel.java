import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The main game panel that draws the board and all pieces.
 * Handles loading of image resources and rendering each cell.
 */
public class GamePanel extends JPanel {

    // Size of each cell in pixels
    public static final int CELL_SIZE = 100;

    // Board pixel dimensions
    private static final int BOARD_WIDTH  = GameBoard.COLS * CELL_SIZE;
    private static final int BOARD_HEIGHT = GameBoard.ROWS * CELL_SIZE;

    // Extra space at bottom for UI info (moves counter etc)
    private static final int INFO_HEIGHT = 60;

    private GameBoard board;
    private Map<String, Image> images;

    public GamePanel() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT + INFO_HEIGHT));
        setBackground(new Color(173, 216, 230));

        images = new HashMap<>();
        loadImages();

        board = new GameBoard();
        LevelLoader.loadLevel(board, 1);
    }

    /**
     * Loads all PNG image resources from the resources folder.
     */
    private void loadImages() {
        String[] names = {
                "snowball_large", "snowball_small", "tree", "hole",
                "head_blue", "head_red", "head_yellow",
                "snowman_blue", "snowman_red", "snowman_yellow",
                "snowman_stack"
        };

        for (String name : names) {
            ImageIcon icon = new ImageIcon("resources/" + name + ".png");
            images.put(name, icon.getImage());
        }
    }

    /**
     * Returns the correct image key for a given piece,
     * based on its type, colour, and stacking state.
     */
    private String getImageKey(Piece piece) {
        switch (piece.getType()) {
            case TREE:
                return "tree";
            case SNOWBALL_SMALL:
                return "snowball_small";
            case SNOWBALL_LARGE:
                if (piece.isStacked()) {
                    return "snowman_stack";
                }
                return "snowball_large";
            case HEAD:
                return "head_" + piece.getColour();
            default:
                return "hole";
        }
    }

    /**
     * Main paint method draws the board grid and all pieces.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw each cell background (hole image)
        for (int row = 0; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS; col++) {
                int x = col * CELL_SIZE;
                int y = row * CELL_SIZE;
                g.drawImage(images.get("hole"), x, y, CELL_SIZE, CELL_SIZE, this);
            }
        }

        // Draw each piece on top
        for (Piece piece : board.getPieces()) {
            int x = piece.getCol() * CELL_SIZE;
            int y = piece.getRow() * CELL_SIZE;
            String key = getImageKey(piece);
            Image img = images.get(key);
            if (img != null) {
                g.drawImage(img, x, y, CELL_SIZE, CELL_SIZE, this);
            }
        }

        // Draw grid lines (helps visualise the board)
        g.setColor(new Color(150, 150, 200, 80));
        for (int row = 0; row <= GameBoard.ROWS; row++) {
            g.drawLine(0, row * CELL_SIZE, BOARD_WIDTH, row * CELL_SIZE);
        }
        for (int col = 0; col <= GameBoard.COLS; col++) {
            g.drawLine(col * CELL_SIZE, 0, col * CELL_SIZE, BOARD_HEIGHT);
        }

        // Draw info bar at bottom
        g.setColor(Color.WHITE);
        g.fillRect(0, BOARD_HEIGHT, BOARD_WIDTH, INFO_HEIGHT);
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Level: 1", 20, BOARD_HEIGHT + 35);
    }

    public GameBoard getBoard() {
        return board;
    }
}