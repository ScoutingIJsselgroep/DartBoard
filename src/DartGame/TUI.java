package DartGame;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * Created by Tristan on 29-1-2015.
 */
public class TUI implements Observer {
    // Scanner
    private Scanner scanner;
    private boolean players;
    private boolean legs;
    private String[] playerArray;
    private Start game;
    private int sum;
    private int count;

    public static void main(String[] args){
        new TUI();
    }

    public TUI(){
        scanner = new Scanner(System.in);
        this.handleInput();
    }

    /**
     * Handle input messages.
     */
    private void handleInput() {
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(!players) {
                playerArray = line.split(" ");
                players = true;
            }else if(!legs){
                game = new Start(playerArray, Integer.parseInt(line));
                game.addObserver(this);
                game.getGame().addObserver(this);
                legs = true;
            }else{
                try {
                    game.getGame().updateScore(Integer.parseInt(line));

                }catch(NumberFormatException e){
                    System.out.println("Number was not correct, please enter a number.");
                }
            }
        }
    }

    /**
     * @param o the object that has been observed.
     * @param arg the argument that have been added.
     */
    public void update(Observable o, Object arg) {
        if(arg.equals(CODES.WON_GAME)){
            System.out.println(((Start)o).getWinner().getPlayername() + " has won!");
        }else if(arg.equals(CODES.NEXT_PLAYER)){
            System.out.println(game.getPlayers().get(((Game)o).getTurn()).getPlayername() + ", please make your move! You require " +  game.getPlayers().get(((Game)o).getTurn()).getScore() + ".");
        }else if(arg.equals(CODES.UPDATED_SCORE)){
            System.out.println(game.getPlayers().get(((Game)o).getTurn()).getPlayername() + ": " + game.getPlayers().get(((Game)o).getTurn()).getScore());
        }else if(arg.equals(CODES.NEXT_LEG)){
            System.out.println("A new leg has started.");
        }
    }
}
