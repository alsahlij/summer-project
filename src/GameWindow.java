import javax.swing.*;

/**
 * The main application window for Snow Problem.
 */
public class GameWindow extends JFrame {

    private GamePanel gamePanel;

    public GameWindow() {
        setTitle("Snow Problem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gamePanel = new GamePanel();
        add(gamePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        gamePanel.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameWindow());
    }
}