import java.io.*;
import java.util.*;

/**
 * Manages persistent high scores for each level.
 * Scores are saved to a text file so they persist between sessions.
 * Lower move count = better score (fewer moves is better).
 */
public class HighScoreManager {

    private static final String SCORES_FILE = "resources/highscores.txt";

    // Maps level number to best (lowest) move count
    private Map<Integer, Integer> scores;

    public HighScoreManager() {
        scores = new HashMap<>();
        loadScores();
    }

    /**
     * Submits a score for a level.
     * Only saves if it beats the existing best score.
     * Returns true if this is a new best score.
     */
    public boolean submitScore(int level, int moves) {
        if (!scores.containsKey(level) || moves < scores.get(level)) {
            scores.put(level, moves);
            saveScores();
            return true;
        }
        return false;
    }


    // Returns the best score for a level, or -1 if no score exists.
    public int getBestScore(int level) {
        return scores.getOrDefault(level, -1);
    }


    // Loads scores from the scores file.
    private void loadScores() {
        File file = new File(SCORES_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    int level = Integer.parseInt(parts[0].trim());
                    int moves = Integer.parseInt(parts[1].trim());
                    scores.put(level, moves);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load high scores: " + e.getMessage());
        }
    }


    // Saves all scores to the scores file.
    private void saveScores() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SCORES_FILE))) {
            for (Map.Entry<Integer, Integer> entry : scores.entrySet()) {
                writer.println(entry.getKey() + "=" + entry.getValue());
            }
        } catch (IOException e) {
            System.out.println("Could not save high scores: " + e.getMessage());
        }
    }
}