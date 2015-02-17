package DartGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static DartGame.CODES.*;

/**
 * Created by Tristan on 29-1-2015.
 * @since 1.0
 */
public class Start extends Observable {
    private Game game;
    private final int legs;

    private int currentLeg;
    // List of players competing in this game.
    private List<Player> players = new ArrayList<Player>();
    private List<Game> gameArray = new ArrayList<Game>();
    private Player winner;


    /**
     * Create a new game with players and an amount of legs.
     * @param players String[] of players
     * @param legs int
     */
    public Start(String[] players, int legs){
        this.legs = legs;
        for(String playername: players) {
            this.players.add(new Player(playername));
        }
        for(int i = 0; i < legs; i++){
            gameArray.add(new Game(this));
        }
        this.nextLeg();
    }

    public void nextLeg(){
        boolean alreadywon = false;
        for(Player player: players){
            if(player.getWon() == Math.ceil(legs/2.0)){
                alreadywon = true;
            }
        }
        if(currentLeg < gameArray.size() && !alreadywon){
            this.game = gameArray.get(currentLeg);
            for(Player player: players){
                player.resetScore();
            }
            currentLeg++;
            this.setChanged();
            this.notifyObservers(CODES.NEXT_LEG);
        }else{
            winner = null;
            int won = 0;
            for(Player player: players){
                if(player.getWon() > won){
                    winner = player;
                    won = player.getWon();
                }
            }
            this.setChanged();
            this.notifyObservers(WON_GAME);
        }
    }
    /**
     * Returns the winner of a game.
     */
    public Player getWinner(){
        return winner;
    }
    /**
     * Return the game that is being played.
     * @return game
     */
    public Game getGame(){
        return game;
    }
    /**
     * Return a list of players
     * @return players List<Player>
     */
    public List<Player> getPlayers(){
        return players;
    }
}
