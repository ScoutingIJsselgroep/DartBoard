package DartGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tristan on 29-1-2015.
 */
public class Player {
    // Number of legs won by a player.
    private int won;
    // List of previous three moves of a player
    private List<Integer> move = new ArrayList<Integer>();
    // The name of the player
    private String playername;
    // The score of the player
    private int score;
    // The score a player starts with
    private static final int START_SCORE = 501;

    /**
     * Constructor of Player.
     * @param playername The name of the player.
     */
    public Player(String playername){
        this.playername = playername;
        this.score = START_SCORE;
        this.resetMoves();
    }

    private void resetMoves() {
        move.add(0, 0);
        move.add(1, 0);
        move.add(2, 0);
        move.add(3, 0);
    }

    /**
     * Returns the score of the player.
     * @return score
     */
    public synchronized int getScore(){
        return score;
    }

    /**
     * Sets the score of the player.
     * @param score
     */
    public synchronized void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns a player name.
     * @return
     */
    public String getPlayername() {
        return playername;
    }

    /**
     * Resets the score of a player
     */
    public void resetScore() {
        this.score = START_SCORE;
    }

    /**
     * Adds 1 to the number of legs won.
     */
    public void wonLeg(){
        this.won++;
    }

    /**
     * Gets the amount of legs won
     * @return legs int
     */
    public int getWon(){
        return this.won;
    }

    /**
     * Adds a score to a move (history of three moves)
     * @param scorecount
     * @param score
     */
    public void setMove(int scorecount, int score) {
        if(scorecount == 1){
            this.resetMoves();
        }
        this.move.add(scorecount, score);
    }

    /**
     * Gets the move of integer
     */
    public int getMove(int scorecount){
        return this.move.get(scorecount);
    }

}
