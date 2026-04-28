import java.io.*;
import java.util.*;

/**
 * Loads level data from resources/levels.txt into the GameBoard.
 * Each level is defined in a simple text format to promote easy addition or editing of levels.
 * Format: LEVEL n / PIECE row col colour / END
 */
public class LevelLoader {

    public static final int TOTAL_LEVELS = 80;
    private static final String LEVELS_FILE = "resources/levels.txt";

    // Cache all levels after first load so we don't re-read file every time
    private static Map<Integer, List<String[]>> levelCache = null;

    public static void loadLevel(GameBoard board, int levelNumber) {
        board.clearBoard();

        if (levelCache == null) {
            levelCache = parseLevelsFile();
        }

        List<String[]> pieces = levelCache.get(levelNumber);
        if (pieces == null) {
            System.out.println("Level " + levelNumber + " not found in levels.txt");
            return;
        }

        for (String[] parts : pieces) {
            String type   = parts[0];
            int row       = Integer.parseInt(parts[1]);
            int col       = Integer.parseInt(parts[2]);
            String colour = parts[3];

            PieceType pieceType;
            switch (type) {
                case "SMALL": pieceType = PieceType.SNOWBALL_SMALL; break;
                case "LARGE": pieceType = PieceType.SNOWBALL_LARGE; break;
                case "HEAD":  pieceType = PieceType.HEAD;           break;
                case "TREE":  pieceType = PieceType.TREE;           break;
                default:
                    System.out.println("Unknown piece type: " + type);
                    continue;
            }

            board.addPiece(new Piece(pieceType, row, col, colour));
        }
    }

    // Parses the levels.txt file and returns a map of level number to piece data.
    private static Map<Integer, List<String[]>> parseLevelsFile() {
        Map<Integer, List<String[]>> allLevels = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(LEVELS_FILE))) {
            String line;
            int currentLevel = -1;
            List<String[]> currentPieces = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip comments and blank lines
                if (line.startsWith("#") || line.isEmpty()) continue;

                if (line.startsWith("LEVEL")) {
                    currentLevel = Integer.parseInt(line.split(" ")[1]);
                    currentPieces = new ArrayList<>();
                } else if (line.equals("END")) {
                    if (currentLevel != -1 && currentPieces != null) {
                        allLevels.put(currentLevel, currentPieces);
                    }
                    currentLevel = -1;
                } else {
                    // Piece line: TYPE row col colour
                    if (currentPieces != null) {
                        String[] parts = line.split(" ");
                        if (parts.length == 4) {
                            currentPieces.add(parts);
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading levels file: " + e.getMessage());
        }

        return allLevels;
    }
}