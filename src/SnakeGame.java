

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class SnakeGame extends JPanel implements ActionListener, KeyListener{

    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    int borderWidth;
    int borderHeight;
    int tileSize = 25;
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    Tile food;
    Random random;
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;
    SnakeGame(int borderWidth, int borderHeight) {
        this.borderWidth = borderWidth;
        this.borderHeight = borderHeight;
        setPreferredSize(new Dimension(this.borderWidth, this.borderHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);


        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 1;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        for (int i = 0; i < borderWidth/tileSize; i++) {
            g.drawLine(i * tileSize, 0, i *tileSize, borderHeight);
            g.drawLine(0, i * tileSize, borderWidth, i * tileSize);
        }

        g.setColor(Color.red);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
//snake body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x * tileSize, snakePart.y *tileSize, tileSize, tileSize);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        } else {
            g.setColor(Color.BLACK);
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(borderWidth / tileSize);
        food.y = random.nextInt(borderHeight / tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
//  eating food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);

            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

//game over
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)) {

                gameOver = true;
            }
        }

        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > borderWidth ||
                snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > borderHeight) {

            gameOver = true;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }

    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}


