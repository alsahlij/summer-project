import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Main panel for rendering the board and handling mouse input.
 * First click selects a snowball, second click chooses direction.
 * Keyboard: R = restart, N = next level, P = previous level.
 */
public class GamePanel extends JPanel implements MouseListener, KeyListener {

    public static final int CELL_SIZE = 100;

    private static final int BOARD_WIDTH  = GameBoard.COLS * CELL_SIZE;
    private static final int BOARD_HEIGHT = GameBoard.ROWS * CELL_SIZE;
    private static final int INFO_HEIGHT  = 60;

    private GameBoard board;
    private Map<String, Image> images;
    private Piece selectedPiece;
    private boolean gameOver;
    private boolean gameWon;
    private int currentLevel;
    private int moveCount;

    public GamePanel() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT + INFO_HEIGHT));
        setBackground(new Color(173, 216, 230));

        images = new HashMap<>();
        loadImages();

        currentLevel  = 1;
        moveCount     = 0;
        gameOver      = false;
        gameWon       = false;
        selectedPiece = null;

        board = new GameBoard();
        LevelLoader.loadLevel(board, currentLevel);

        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

    // Load all game images once
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

    // Pick correct image based on piece state
    private String getImageKey(Piece piece) {
        switch (piece.getType()) {
            case TREE:           return "tree";
            case SNOWBALL_SMALL: return "snowball_small";
            case SNOWBALL_LARGE:
                if (piece.isStacked()) return "snowman_stack";
                return "snowball_large";
            case HEAD:
                if (piece.isStacked()) return "snowman_" + piece.getColour();
                return "head_" + piece.getColour();
            default: return "hole";
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw board background
        for (int row = 0; row < GameBoard.ROWS; row++) {
            for (int col = 0; col < GameBoard.COLS; col++) {
                g.drawImage(images.get("hole"),
                        col * CELL_SIZE, row * CELL_SIZE,
                        CELL_SIZE, CELL_SIZE, this);
            }
        }

        // Highlight selected piece
        if (selectedPiece != null) {
            g.setColor(new Color(255, 255, 0, 120));
            g.fillRect(
                    selectedPiece.getCol() * CELL_SIZE,
                    selectedPiece.getRow() * CELL_SIZE,
                    CELL_SIZE, CELL_SIZE);
        }

        // Draw all pieces
        for (Piece piece : board.getPieces()) {
            Image img = images.get(getImageKey(piece));
            if (img != null) {
                g.drawImage(img,
                        piece.getCol() * CELL_SIZE,
                        piece.getRow() * CELL_SIZE,
                        CELL_SIZE, CELL_SIZE, this);
            }
        }

        // Grid overlay
        g.setColor(new Color(150, 150, 200, 80));
        for (int row = 0; row <= GameBoard.ROWS; row++) {
            g.drawLine(0, row * CELL_SIZE, BOARD_WIDTH, row * CELL_SIZE);
        }
        for (int col = 0; col <= GameBoard.COLS; col++) {
            g.drawLine(col * CELL_SIZE, 0, col * CELL_SIZE, BOARD_HEIGHT);
        }

        // Info bar
        g.setColor(Color.WHITE);
        g.fillRect(0, BOARD_HEIGHT, BOARD_WIDTH, INFO_HEIGHT);

        // Draw level and moves
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Level: " + currentLevel, 20, BOARD_HEIGHT + 38);
        g.drawString("Moves: " + moveCount,   170, BOARD_HEIGHT + 38);

        // Status messages
        g.setFont(new Font("Arial", Font.PLAIN, 13));
        if (gameOver) {
            drawMessage(g, "Game Over! Snowball fell off!", Color.RED);
            g.setColor(Color.DARK_GRAY);
            g.drawString("R = Restart", 330, BOARD_HEIGHT + 38);
        } else if (gameWon) {
            drawMessage(g, "You Win! Well done!", new Color(0, 150, 0));
            g.setColor(new Color(0, 130, 0));
            g.drawString("R = Restart  |  N = Next Level", 290, BOARD_HEIGHT + 38);
        } else if (selectedPiece != null) {
            g.setColor(Color.DARK_GRAY);
            g.drawString("Click a direction to move  |  R = Restart", 270, BOARD_HEIGHT + 38);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.drawString("Click a snowball to select  |  R = Restart", 270, BOARD_HEIGHT + 38);
        }
    }

    // Draw centered overlay message
    private void drawMessage(Graphics g, String message, Color colour) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, BOARD_HEIGHT / 2 - 40, BOARD_WIDTH, 80);
        g.setColor(colour);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        FontMetrics fm = g.getFontMetrics();
        int x = (BOARD_WIDTH - fm.stringWidth(message)) / 2;
        g.drawString(message, x, BOARD_HEIGHT / 2 + 10);
    }

    // Mouse Input
    @Override
    public void mouseClicked(MouseEvent e) {
        // Always re-grab focus on click
        requestFocusInWindow();

        if (gameOver || gameWon) {
            restartLevel();
            return;
        }

        int col = e.getX() / CELL_SIZE;
        int row = e.getY() / CELL_SIZE;

        // Ignore clicks outside board
        if (row >= GameBoard.ROWS || col >= GameBoard.COLS || row < 0 || col < 0) return;

        if (selectedPiece == null) {
            handleSelection(row, col);
        } else {
            handleDirectionClick(row, col);
        }

        repaint();
    }


    // Select a movable snowball.
    private void handleSelection(int row, int col) {
        Piece clicked = board.getPieceAt(row, col);
        if (clicked == null) return;

        if ((clicked.getType() == PieceType.SNOWBALL_LARGE
                || clicked.getType() == PieceType.SNOWBALL_SMALL)
                && !clicked.isStacked()) {
            selectedPiece = clicked;
        }
    }


    // Determine direction from second click and move the piece.
    private void handleDirectionClick(int row, int col) {
        int dRow = row - selectedPiece.getRow();
        int dCol = col - selectedPiece.getCol();

        if (dRow == 0 && dCol == 0) {
            selectedPiece = null;
            return;
        }

        int direction;
        if (Math.abs(dRow) >= Math.abs(dCol)) {
            direction = (dRow > 0) ? 1 : 0;
        } else {
            direction = (dCol > 0) ? 3 : 2;
        }

        boolean success = board.movePiece(selectedPiece, direction);
        selectedPiece = null;
        moveCount++;

        if (!success) {
            gameOver = true;
        } else {
            // Check stacking after every move
            board.checkAndApplyStacking();
            if (board.checkWin()) {
                gameWon = true;
            }
        }
    }

    // Keyboard Input

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_R) {
            restartLevel();
        }

        if (key == KeyEvent.VK_N) {
            nextLevel();
        }

        if (key == KeyEvent.VK_P) {
            previousLevel();
        }
    }

    // Reset current level state
    public void restartLevel() {
        gameOver      = false;
        gameWon       = false;
        selectedPiece = null;
        moveCount     = 0;
        LevelLoader.loadLevel(board, currentLevel);
        repaint();
    }


    // Advances to the next level if available.
    public void nextLevel() {
        if (currentLevel < LevelLoader.TOTAL_LEVELS) {
            currentLevel++;
            restartLevel();
        }
    }


    // Goes back to the previous level.
    public void previousLevel() {
        if (currentLevel > 1) {
            currentLevel--;
            restartLevel();
        }
    }

    // Unused interface methods
    @Override public void mousePressed(MouseEvent e)  {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e)  {}
    @Override public void mouseExited(MouseEvent e)   {}
    @Override public void keyTyped(KeyEvent e)        {}
    @Override public void keyReleased(KeyEvent e)     {}

    public int getCurrentLevel() { return currentLevel; }
    public int getMoveCount()    { return moveCount; }
    public GameBoard getBoard()  { return board; }
}