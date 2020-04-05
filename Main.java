import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

class Main {
    public static void main(String[] args) {
        Game game = new Game();

        JFrame frame = new JFrame("Puyo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(game);
        frame.addKeyListener(new Key());
        frame.setSize(640, 480);
        frame.setVisible(true);
    }
}