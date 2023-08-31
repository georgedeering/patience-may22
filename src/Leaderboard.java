/**
 * @author George Deering
 * @version 1.0
 */
public record Leaderboard(String name, int score) {
    /**
     * getScore gets the score of specified player
     * @return int score
     */
    public int getScore() {
        return score;
    }

    /**
     * getName gets the name of specified player
     * @return String name
     */
    public String getName() {
        return name;
    }
}