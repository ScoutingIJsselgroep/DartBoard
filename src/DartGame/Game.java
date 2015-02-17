package DartGame;

import java.util.Observable;

/**
 * Game
 * Created by Tristan on 29-1-2015.
 */
public class Game extends Observable{
    // A field with settings including players etc.
    private final Start settings;
    // Integer depicting whose turn it is.
    private int turn;

    /**
     * Constructor
     */
    public Game (Start settings){
        this.settings = settings;
    }

    /**
     * Set the turn to the next player.
     */
    public synchronized void setTurn(){
        this.turn = (turn + 1) % settings.getPlayers().size();
        this.setChanged();
        this.notifyObservers(CODES.NEXT_PLAYER);
    }

    /**
     * Get the current player number that has to make a turn.
     * @return
     */
    public synchronized int getTurn(){
        return turn;
    }

    /**
     * Make an update to the score.
     */
    public void updateScore(int score){
        int currentscore = settings.getPlayers().get(turn).getScore();
        if(score <= currentscore) {
            settings.getPlayers().get(turn).setScore(currentscore - score);
        }
        this.setChanged();
        this.notifyObservers(CODES.UPDATED_SCORE);

        if(settings.getPlayers().get(turn).getScore() == 0){
            settings.getPlayers().get(turn).wonLeg();
            settings.nextLeg();
        }
        this.setTurn();
    }
}
