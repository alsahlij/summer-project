import javax.swing.*;

/**
 * The main application window for Snow Problem.
 * Sets up the JFrame and holds the GamePanel.
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
    }

    public static void main(String[] args) {
        // Run GUI on the Event Dispatch Thread - standard Java Swing practice
        SwingUtilities.invokeLater(() -> new GameWindow());
    }
}