import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

class Main {
    public static void main(String[] args) {
        Game game = new Game();
        Random random = new Random();
        JFrame frame = new JFrame("Puyo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(game);
        frame.setSize(640, 480);
        // game.setSize(640, 480);
        // game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // game.setResizable(false);
        frame.setVisible(true);
        while (game.isGameOver == false) {
            game.putPuyo(random.nextInt(game.width), random.nextInt(4));
            frame.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}