package DartGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Tristan on 30-1-2015.
 */
public class BoardGUI extends JFrame implements Observer {

    private final int legs;
    private Start game;

    private static final int[] VALUEARRAY = {6, 10, 15, 2, 17, 3, 19, 7, 16, 8, 11, 14, 9, 12, 5, 20, 1, 18, 4, 13, 6};
    private static final int COUNT = 20;
    private static final int FRAME_HEIGHT = 1000;
    private static final int FRAME_WIDTH = 1000;
    private static final double OUTER_TEXT_MARGIN = 0.9;
    private static final double OUTER_DOUBLES = 0.95;
    private static final double INNER_DOUBLES = 0.9435531788472965;
    private static final double OUTER_TRIPLES = 0.6225787284610814;
    private static final double INNER_TRIPLES = 0.5659536541889483;
    private static final double OUTER_BULL = 0.0943553178847296;
    private static final double OUTER_BULLSEYE = 0.0377302436125966;
    private int count;
    private int scorecount;
    private int totalscore;
    private String typedscore;


    public BoardGUI(String[] playerArray, int legs){

        super("DartBoard");
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() != KeyEvent.VK_ENTER) {
                    try{
                        Integer.parseInt(e.getKeyChar() + "");
                        if (typedscore == null) {
                            typedscore = e.getKeyChar() + "";
                        } else {
                            typedscore += e.getKeyChar();
                        }
                    }catch (NumberFormatException f){
                    }

                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        int scoretyped = 0;
                        if(typedscore != null) {
                            scoretyped = Integer.parseInt(typedscore);
                            totalscore += scoretyped;
                        }
                        scorecount++;
                        game.getPlayers().get(game.getGame().getTurn()).setMove(scorecount, scoretyped);
                        revalidateComponents();
                        if (scorecount == 3) {
                            game.getGame().updateScore(totalscore);
                            scorecount = 0;
                            totalscore = 0;
                        }
                        typedscore = null;
                    } catch (Exception f) {
                        System.out.println("Exeption occured");
                        f.printStackTrace();
                    }

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        this.setAlwaysOnTop(true);
        this.legs = legs;
        game = new Start(playerArray, legs);
        game.addObserver(this);
        game.getGame().addObserver(this);

        for(int i = 0; i < game.getPlayers().size(); i++) {
            this.add(new PlayerPanel((i * 200) + 20, game.getPlayers().get(i)));
        }

        this.add(new DrawPane());
        //this.add(new DartMark(100, 100));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH + 400, FRAME_HEIGHT);
        setUndecorated(true);


        setVisible(true);
    }

    public static int okcancel(String theMessage) {
        int result = JOptionPane.showConfirmDialog((Component) null, theMessage,
                "alert", JOptionPane.OK_OPTION);
        return result;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg.equals(CODES.WON_GAME)){
            setAlwaysOnTop(false);
            this.setVisible(false);
            int i = okcancel(((Start)o).getWinner().getPlayername() + " has won! Open new playerchooser?");

            if(i == 0){
                StartDialog dialog = new StartDialog();
                dialog.pack();
                dialog.setVisible(true);
                dispose();
            }else{
                System.exit(0);
            }
            System.out.println(((Start)o).getWinner().getPlayername() + " has won!");
        }else if(arg.equals(CODES.NEXT_PLAYER)){
            System.out.println("Switching to next player");
            this.revalidateComponents();
        }else if(arg.equals(CODES.UPDATED_SCORE)){
            System.out.println(game.getPlayers().get(((Game)o).getTurn()).getPlayername() + ": " + game.getPlayers().get(((Game)o).getTurn()).getScore());

        }else if(arg.equals(CODES.NEXT_LEG)){
            System.out.println("A new leg has started.");
            game.getGame().addObserver(this);
            this.count = 0;
            this.scorecount = 0;
            this.totalscore = 0;
        }
    }

    private void revalidateComponents() {
        for(Component component: this.getComponents()){
            component.revalidate();
            component.repaint();
        }
    }

    class PlayerPanel extends JPanel {
        private final int height;
        private final Player player;

        public PlayerPanel(int height, Player player) {
            this.player = player;
            this.height = height;
            this.setBounds(FRAME_WIDTH + 20, height, 400, 180);
        }
        public void paintComponent(Graphics g){
            if(game.getGame().getTurn() == game.getPlayers().indexOf(player)) {
                g.setColor(new Color(10, 128, 0));
            }else{
                g.setColor(new Color(128, 0, 11));
            }
            g.fillRect(0, 0, 400, 200);
            g.setColor((new Color(0, 0, 0)));
            g.setFont(new Font("Helvetica", Font.BOLD, 35));
            g.drawString(player.getPlayername(), 30, 50);
            g.drawString(player.getScore() + "", 30, 100);
            for(int i = 1; i <= 3; i++){
                if(scorecount + 1 == i && game.getGame().getTurn() == game.getPlayers().indexOf(player)) {
                    g.setFont(new Font("Helvetica", Font.BOLD, 20));
                    g.drawString(player.getMove(i) + "\t", 40 * i, 130);
                }else{
                    g.setFont(new Font("Helvetica", Font.PLAIN, 20));
                    g.drawString(player.getMove(i) + "\t", 40 * i, 130);
                }
            }
            if(Throwout.throwOut(player.getScore()) != null){
                g.setFont(new Font("Helvetica", Font.PLAIN, 20));
                g.drawString("Throwout: " + Throwout.throwOut(player.getScore()), 30, 160);
            }

            g.setFont(new Font("Helvetica", Font.BOLD, 30));
            g.drawString("Won: " + player.getWon() + " / " + (int)Math.ceil(legs/2.0), 200, 130);
        }
    }
    
    class DartMark extends JPanel {
        private final int x;
        private final int y;

        public DartMark(int x, int y){
            this.x = x;
            this.y = y;
        }

        public void paintComponent(Graphics g){
            g.setColor(new Color(255, 255, 48));
            g.fillOval(10, 10, x - 5, y - 5);
        }
    }
    class DrawPane extends JPanel implements MouseListener{
        public DrawPane(){
            this.addMouseListener(this);
            setUndecorated(true);
        }
        public void paintComponent(Graphics g){
            g.fillOval(0,0,FRAME_WIDTH,FRAME_HEIGHT);
            int center_x = FRAME_WIDTH/2;
            int center_y = FRAME_HEIGHT/2;
            // Depicts a position on the board.
            g.setColor(new Color(16, 128, 4));

            // Outer circle, has text.
            for (int i = 0; i < COUNT; i++) {
                // Degrees d: COUNT / (2 * PI)
                double degrees =  (Math.PI * 2 / (double) COUNT ) * i;                g.setFont(new Font("Helvetica", Font.BOLD, 30));
                // Position y: (FRAME_WIDTH / 2) * Math.sin (degrees)
                int y = (int) (((FRAME_HEIGHT/2) * OUTER_TEXT_MARGIN * Math.sin(degrees)) + (FRAME_HEIGHT / 2) + 10);
                // Position x: (FRAME_HEIGHT / 2) * Math.cos (degrees)
                int x = (int) (((FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * Math.cos(degrees)) + (FRAME_WIDTH /2) - 12);
                g.drawString(VALUEARRAY[i] + "", x, y);
            }


            // Outer ring of doubles
            int outerdoublesx = (int) ((FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES);
            int outerdoublesy = (int) (((FRAME_HEIGHT/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES));
            g.drawOval((FRAME_WIDTH / 2) - outerdoublesx, (FRAME_HEIGHT / 2) - outerdoublesy, outerdoublesx * 2, outerdoublesy * 2);
            for (int i = 1; i <= COUNT; i++) {
                // Degrees d: COUNT / (2 * PI)
                double degrees =  (Math.PI * 2 / (double) COUNT ) * i + (Math.PI * 2 / (double) COUNT ) * 0.5;
                // Position y: (FRAME_WIDTH / 2) * Math.sin (degrees)
                int y = (int) (((FRAME_HEIGHT/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * Math.sin(degrees)) + (FRAME_HEIGHT / 2));
                int y1 = (int) (((FRAME_HEIGHT/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * OUTER_BULL* Math.sin(degrees)) + (FRAME_HEIGHT / 2));
                // Position x: (FRAME_HEIGHT / 2) * Math.cos (degrees)
                int x = (int) (((FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * Math.cos(degrees)) + (FRAME_WIDTH /2));

                int x1 = (int) ((FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * OUTER_BULL * Math.cos(degrees)) + (FRAME_WIDTH /2);

                g.drawLine(x, y, x1, y1);
                g.fillOval(x - 5, y - 5, 10, 10);
            }

            int innerdoublesx = (int) ((FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * INNER_DOUBLES);
            int innerrdoublesy = (int) (((FRAME_HEIGHT/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * INNER_DOUBLES));
            g.drawOval((FRAME_WIDTH / 2) - innerdoublesx, (FRAME_HEIGHT / 2) - innerrdoublesy, innerdoublesx * 2, innerrdoublesy * 2);
            for (int i = 1; i <= COUNT; i++) {
                // Degrees d: COUNT / (2 * PI)
                double degrees =  (Math.PI * 2 / (double) COUNT ) * i + (Math.PI * 2 / (double) COUNT ) * 0.5;
                // Position y: (FRAME_WIDTH / 2) * Math.sin (degrees)
                int y = (int) (((FRAME_HEIGHT/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * INNER_DOUBLES * Math.sin(degrees)) + (FRAME_HEIGHT / 2));
                // Position x: (FRAME_HEIGHT / 2) * Math.cos (degrees)
                int x = (int) (((FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * INNER_DOUBLES* Math.cos(degrees)) + (FRAME_WIDTH /2));
                g.fillOval(x - 5, y - 5, 10, 10);
            }

            // Outer Triples
            int outertriplesx = (int) ((FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * OUTER_TRIPLES);
            int outertriplesy = (int) (((FRAME_HEIGHT/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * OUTER_TRIPLES));
            g.drawOval((FRAME_WIDTH / 2) - outertriplesx, (FRAME_HEIGHT / 2) - outertriplesy, outertriplesx * 2, outertriplesy * 2);
            for (int i = 1; i <= COUNT; i++) {
                // Degrees d: COUNT / (2 * PI)
                double degrees =  (Math.PI * 2 / (double) COUNT ) * i + (Math.PI * 2 / (double) COUNT ) * 0.5;
                // Position y: (FRAME_WIDTH / 2) * Math.sin (degrees)
                int y = (int) (((FRAME_HEIGHT/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * OUTER_TRIPLES * Math.sin(degrees)) + (FRAME_HEIGHT / 2));
                // Position x: (FRAME_HEIGHT / 2) * Math.cos (degrees)
                int x = (int) (((FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * OUTER_TRIPLES * Math.cos(degrees)) + (FRAME_WIDTH /2));
                g.fillOval(x - 5, y - 5, 10, 10);
            }

            // Inner Triples
            int innertriplesx = (int) ((FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * INNER_TRIPLES);
            int innertriplesy = (int) (((FRAME_HEIGHT/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * INNER_TRIPLES));
            g.drawOval((FRAME_WIDTH / 2) - innertriplesx, (FRAME_HEIGHT / 2) - innertriplesy, innertriplesx * 2, innertriplesy * 2);
            for (int i = 1; i <= COUNT; i++) {
                // Degrees d: COUNT / (2 * PI)
                double degrees =  (Math.PI * 2 / (double) COUNT ) * i + (Math.PI * 2 / (double) COUNT ) * 0.5;
                // Position y: (FRAME_WIDTH / 2) * Math.sin (degrees)
                int y = (int) (((FRAME_HEIGHT/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * INNER_TRIPLES * Math.sin(degrees)) + (FRAME_HEIGHT / 2));
                // Position x: (FRAME_HEIGHT / 2) * Math.cos (degrees)
                int x = (int) (((FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * INNER_TRIPLES * Math.cos(degrees)) + (FRAME_WIDTH /2));
                g.fillOval(x - 5, y - 5, 10, 10);
            }
            int bullx = (int) ((FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * OUTER_BULL);
            int bully = (int) (((FRAME_HEIGHT/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * OUTER_BULL));
            g.fillOval((FRAME_WIDTH /2) - bullx, (FRAME_HEIGHT / 2) - bully, bullx * 2, bully * 2);

            g.setColor(new Color(128, 0, 15));
            int bullseyex = (int) ((FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * OUTER_BULLSEYE);
            int bullseyey = (int) (((FRAME_HEIGHT/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * OUTER_BULLSEYE));
            g.fillOval((FRAME_WIDTH /2) - bullseyex, (FRAME_HEIGHT / 2) - bullseyey, bullseyex * 2, bullseyey * 2);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX() - (FRAME_WIDTH / 2);
            int y = -e.getY() + (FRAME_HEIGHT / 2);
            double rad = Math.atan2(y, x);
            if(rad < 0){
                rad += Math.PI * 2;
            }
            int count = (int) Math.round(rad/(Math.PI * 2 / (double) COUNT ));
            int score = 0;
            if(Math.sqrt(x*x + y * y) > (FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * INNER_TRIPLES && Math.sqrt(x*x + y * y) < (FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * OUTER_TRIPLES){
                score = VALUEARRAY[COUNT - count] * 3;
            }else if(Math.sqrt(x*x + y * y) > (FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * OUTER_TRIPLES && Math.sqrt(x*x + y * y) < (FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * INNER_DOUBLES){
                score = VALUEARRAY[COUNT - count];
            }else if(Math.sqrt(x*x + y * y) > (FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * INNER_DOUBLES &&  Math.sqrt(x*x + y * y) < (FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES){
                score = VALUEARRAY[COUNT - count] * 2;
            }else if(Math.sqrt(x*x + y * y) > (FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES){
                score = 0;
            }else if(Math.sqrt(x*x + y * y) < (FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * INNER_TRIPLES &&  Math.sqrt(x*x + y * y) > ((FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * OUTER_BULL)){
                score = VALUEARRAY[COUNT - count];
            }else if(Math.sqrt(x*x + y * y) < (FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * OUTER_BULL && Math.sqrt(x*x + y * y) > (FRAME_WIDTH/2) * OUTER_TEXT_MARGIN * OUTER_DOUBLES * OUTER_BULLSEYE){
                score = 25;
            }else{
                score = 50;
            }
            totalscore += score;
            scorecount ++;
            game.getPlayers().get(game.getGame().getTurn()).setMove(scorecount, score);
            revalidateComponents();
            if(scorecount == 3){
                game.getGame().updateScore(totalscore);
                scorecount = 0;
                totalscore = 0;
            }


        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public static void main(String[] args){
        String[] playerArray = {"Tristan", "Rudy", "Deef"};
        int legs = 3;
        new BoardGUI(playerArray, legs);
    }
}
