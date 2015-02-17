package DartGame;

/**
 * Created by Tristan on 30-1-2015.
 */
public enum CODES {
    // Indicates that a player has won a game.
    WON_GAME,
    // Indicates that a player has won a leg.
    WON_LEG,
    // Indicates that the next player may throw.
    NEXT_PLAYER,
    // Indicates that the leg is over and a next leg will be played.
    NEXT_LEG,
    // Indicates that something has gone wrong.
    ERROR,
    // Indicates that the score has been updated
    UPDATED_SCORE;
}
