import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Key implements KeyListener {
    static boolean up = false;
    static boolean down = false;
    static boolean right = false;
    static boolean left = false;
    static boolean enter = false;
    static boolean[] isPress = new boolean[1024];

    @Override
    public void keyPressed(KeyEvent e) {
        isPress[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        isPress[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}