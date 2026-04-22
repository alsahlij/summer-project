import javax.swing.*;
/**
 * Main entry point for Snow Problem game.
 * Launches the game window.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameWindow());
    }
}