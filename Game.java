import java.awt.*;
import javax.swing.*;

import java.util.*;

public class Game extends JPanel implements Runnable {
    final int height = 12;
    final int width = 6;
    final int size = 30;
    final Color[] colors = { Color.red, Color.blue, Color.green, Color.black };
    Boolean canControl;
    Boolean isGameOver;
    int[][] board;
    int score;
    int curChain = 0;
    private Thread gameLoop;

    Game() {
        super();
        this.board = new int[this.height][this.width];
        this.score = 0;
        this.canControl = true;
        this.isGameOver = false;
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                this.board[i][j] = -1;
            }
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

    public void putPuyo(int x, int color) {
        if (this.canControl == false)
            return;
        this.board[0][x] = color;
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
            // change this later!!!!!!
            gainScore = this.curChain * chainPuyo.size();
            for (Point puyo : chainPuyo) {
                this.board[puyo.y][puyo.x] = -1;
            }
        }
        return gainScore;
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
        return isChange;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawLine(0, 0, 100, 50);
        g.drawOval(50, 50, 100, 100);
        g.fillOval(100, 75, 50, 50);
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (this.board[i][j] == -1)
                    continue;
                Color color = this.colors[this.board[i][j]];
                g.setColor(color);
                g.fillOval(j * this.size, i * this.size, 30, 30);
            }
        }
    }

    public void run() {
        while (true) {
            long start = System.currentTimeMillis();
            this.updateGameState();
            // this.paintComponent(this.getGraphics());
            long end = System.currentTimeMillis();
            System.out.println((end - start) + "ms");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
