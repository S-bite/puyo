import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.border.*;

class Main {
    public static void main(String[] args) {
        Game game = new Game(false);
        JFrame frame = new JFrame("Puyo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
        frame.add(Box.createVerticalStrut(20), BorderLayout.SOUTH);
        frame.add(Box.createHorizontalStrut(100), BorderLayout.WEST);
        frame.add(Box.createHorizontalStrut(100), BorderLayout.EAST);
        frame.getContentPane().add(game);
        frame.addKeyListener(new Key());
        frame.setSize(640, 480);
        frame.setVisible(true);
    }
}