import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private LinkedList<Point> snake;
    private int snakeSize = 10;

    private int dx = snakeSize;
    private int dy = 0;

    private Point fruit;
    private Random rand;

    private Timer timer;

    private int score = 0;

    private int fruitSize = snakeSize * 4;

    private boolean gameOver = false;

    private String difficulty;

    public SnakeGame() {

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
        JFrame frame = new JFrame("Snake Game");
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JLabel welcomeLabel = new JLabel("Welcome to Snake Game!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        welcomeLabel.setForeground(Color.WHITE);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> showDifficultyScreen());

        panel.add(welcomeLabel);
        panel.add(startButton);

        frame.add(panel);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void showDifficultyScreen() {
        JFrame frame = new JFrame("Select Difficulty");
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JButton easyButton = new JButton("Easy");
        JButton normalButton = new JButton("Normal");
        JButton hardButton = new JButton("Hard");

        easyButton.addActionListener(e -> startGame("Easy"));
        normalButton.addActionListener(e -> startGame("Normal"));
        hardButton.addActionListener(e -> startGame("Hard"));

        panel.add(new JLabel("Select Difficulty:"));
        panel.add(easyButton);
        panel.add(normalButton);
        panel.add(hardButton);

        frame.add(panel);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void startGame(String difficulty) {
        this.difficulty = difficulty;
        this.score = 0;
        this.snake = new LinkedList<>();
        this.snake.add(new Point(WIDTH / 2, HEIGHT / 2));

        rand = new Random();
        spawnFruit();

        int speed = 0;
        switch (difficulty) {
            case "Easy":
                speed = 10;
                break;
            case "Normal":
                speed = 15;
                break;
            case "Hard":
                speed = 20;
                break;
        }

        timer = new Timer(1000 / speed, this);
        timer.start();

        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);

        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return;
        }

        Point head = snake.getFirst();
        Point newHead = new Point(head.x + dx, head.y + dy);

        snake.addFirst(newHead);

        if (newHead.equals(fruit)) {
            score++;
            spawnFruit();
        } else {
            snake.removeLast();
        }

        if (score >= 10) {
            gameOver = true;
        }

        if (newHead.x < 0) {
            dx = Math.abs(dx);
        } else if (newHead.x >= WIDTH) {
            dx = -Math.abs(dx);
        }

        if (newHead.y < 0) {
            dy = Math.abs(dy);
        } else if (newHead.y >= HEIGHT) {
            dy = -Math.abs(dy);
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GREEN);
        for (Point segment : snake) {
            g.fillRect(segment.x, segment.y, snakeSize, snakeSize);
        }

        ImageIcon fruitIcon = new ImageIcon("cherry.png");
        Image fruitImage = fruitIcon.getImage();
        g.drawImage(fruitImage, fruit.x, fruit.y, fruitSize, fruitSize, this);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 10, 30);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over!", WIDTH / 2 - 100, HEIGHT / 2);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            return;
        }

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP && dy == 0) {
            dx = 0;
            dy = -snakeSize;
        } else if (key == KeyEvent.VK_DOWN && dy == 0) {
            dx = 0;
            dy = snakeSize;
        } else if (key == KeyEvent.VK_LEFT && dx == 0) {
            dx = -snakeSize;
            dy = 0;
        } else if (key == KeyEvent.VK_RIGHT && dx == 0) {
            dx = snakeSize;
            dy = 0;
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }


    private void spawnFruit() {
        int x = rand.nextInt(WIDTH / fruitSize) * fruitSize;
        int y = rand.nextInt(HEIGHT / fruitSize) * fruitSize;
        fruit = new Point(x, y);
    }

    public static void main(String[] args) {

        new SnakeGame();
    }
}
