import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.util.*;

public class Game extends JPanel implements Runnable {
    final int height = 12;
    final int width = 6;
    final int size = 30;
    final Color[] colors = { Color.red, Color.blue, Color.green, Color.black };
    final int puyoNum = 10;
    Boolean canControl;
    Boolean isGameOver;
    int[][] board;
    int score;
    int curChain;
    int putPosition;
    private Thread gameLoop;
    ArrayList<Integer> puyoList;
    int nextPuyoIndex;

    Game() {
        super();
        this.board = new int[this.height][this.width];
        this.score = 0;
        this.canControl = true;
        this.isGameOver = false;
        this.curChain = 0;
        this.putPosition = 0;
        this.nextPuyoIndex = 0;
        this.puyoList = new ArrayList<>();

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                this.board[i][j] = -1;
            }
        }
        Random random = new Random();
        for (int i = 0; i < this.puyoNum; i++) {
            this.puyoList.add(random.nextInt(4));
        }
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    public Boolean applyGravityAndCheckIsSettle() {
        Boolean isSettle = true;
        for (int i = this.height - 1; i >= 1; i--) {
            for (int j = 0; j < this.width; j++) {
                if (this.board[i][j] == -1 && this.board[i - 1][j] != -1) {
                    this.board[i][j] = this.board[i - 1][j];
                    this.board[i - 1][j] = -1;
                    isSettle = false;
                }
            }
        }
        return isSettle;
    }

    public void putPuyo() {
        if (this.canControl == false)
            return;

        this.board[0][this.putPosition] = this.puyoList.get(this.nextPuyoIndex);
        this.nextPuyoIndex++;
        this.canControl = false;
    }

    public int removeChain(int y, int x) {
        final int[] dy = { 1, -1, 0, 0 };
        final int[] dx = { 0, 0, 1, -1 };
        int gainScore = 0;
        ArrayDeque<Point> que = new ArrayDeque<>();
        ArrayList<Point> chainPuyo = new ArrayList<>();

        que.push(new Point(x, y));
        while (!que.isEmpty()) {
            Point cur = que.poll();
            if (chainPuyo.indexOf(cur) != -1) {
                continue;
            }
            chainPuyo.add(cur);
            for (int i = 0; i < 4; i++) {
                int nx = cur.x + dx[i];
                int ny = cur.y + dy[i];
                Point next = new Point(nx, ny);
                if (0 <= ny && ny < this.height && 0 <= nx && nx < this.width
                        && this.board[ny][nx] == this.board[cur.y][cur.x] && chainPuyo.indexOf(next) == -1) {
                    que.push(next);
                }
            }
        }
        if (chainPuyo.size() >= 4) {
            System.out.println(chainPuyo);
            gainScore = 1000 * (int) Math.pow(2, this.curChain - 1) + 100 * (chainPuyo.size() - 4);
            for (Point puyo : chainPuyo) {
                this.board[puyo.y][puyo.x] = -1;
            }
        }
        return gainScore;
    }

    public void getMovefromStream() {

    }

    public void getKeyInput() {
        if (Key.isPress[KeyEvent.VK_RIGHT]) {
            this.putPosition++;
            if (this.putPosition >= this.width)
                this.putPosition = this.width - 1;
        }
        if (Key.isPress[KeyEvent.VK_LEFT]) {
            this.putPosition--;
            if (this.putPosition < 0)
                this.putPosition = 0;
        }
        if (Key.isPress[KeyEvent.VK_DOWN]) {
            this.putPuyo();
        }
    }

    public Boolean updateGameState() {
        if (this.applyGravityAndCheckIsSettle() == false) {
            this.canControl = false;
            return true;
        }
        Boolean isChange = false;

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (this.board[i][j] == -1)
                    continue;
                int gainScore = this.removeChain(i, j);
                if (gainScore > 0) {
                    isChange = true;
                }
                this.score += gainScore;
            }
        }
        if (isChange)
            this.curChain++;
        else
            this.curChain = 1;
        this.canControl = !isChange;
        this.isGameOver = this.canControl == true && this.nextPuyoIndex == this.puyoNum;
        return isChange;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (this.board[i][j] == -1)
                    continue;
                Color color = this.colors[this.board[i][j]];
                g.setColor(color);
                g.fillOval(j * this.size, i * this.size, this.size, this.size);
            }
        }
        if (this.nextPuyoIndex != this.puyoNum) {
            g.setColor(this.colors[this.puyoList.get(this.nextPuyoIndex)]);
            g.fillOval(this.size * this.putPosition, 0, this.size, this.size);
        }
        g.setColor(Color.BLACK);
        g.drawRect(this.size * this.putPosition, 0, this.size, this.size);
        g.drawString("Score : " + this.score, 30 * this.width + 30, 30);
        for (int i = 0; i < 4 && this.nextPuyoIndex + i < this.puyoNum; i++) {
            Color color = this.colors[this.puyoList.get(this.nextPuyoIndex + i)];
            g.setColor(color);
            g.fillOval(180, i * this.size, this.size, this.size);
        }
    }

    public void run() {

        while (!this.isGameOver) {
            // long start = System.currentTimeMillis();
            this.getKeyInput();
            this.updateGameState();
            // this.paintComponent(this.getGraphics());
            // long end = System.currentTimeMillis();
            // System.out.println((end - start) + "ms");
            this.repaint();
            Toolkit.getDefaultToolkit().sync();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
