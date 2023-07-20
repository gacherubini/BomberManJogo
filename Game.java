import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;

import enums.Direction;

public class Game {
    private static final int MAX_LEVEL = 2;

    private static final int MAX_ROW = 17;
    private static final int MAX_COLUMN = 25;

    private static Entity[][] entities;

    private static int currentLevel = 1;
    private static Board board = new Board(MAX_ROW, MAX_COLUMN);

    private static Bomberman bomberman;
    private static House house;
    private static Key key;

    private static ArrayList<Enemy> enemies;
    private static Timer enemiesTimer;

    private static JLabel keyLabel;

    private static JPanel itemsPanel;
    private static JPanel navigationsButtonsPanel1;
    private static JPanel navigationsButtonsPanel2;
    private static JPanel mainPanel;

    private static JFrame mainFrame;

    public static int getMaxRow() {
        return MAX_ROW;
    }

    public static int getMaxColumn() {
        return MAX_COLUMN;
    }

    public static boolean isValidPosition(int row, int column) {
        return (row < 0 || row >= MAX_ROW || column < 0 || column >= MAX_COLUMN) ? false : true;
    }

    public static Entity getEntity(int row, int column) {
        if (!isValidPosition(row, column))
            return null;

        return entities[row][column];
    }

    public static void addEntity(Entity entity) {
        int row = entity.getRow();
        int column = entity.getColumn();

        if (!isValidPosition(row, column))
            throw new IllegalArgumentException(String.format("Posição inválida: [%d][%d]", row, column));

        entities[row][column] = entity;

        // Game.refreshBoard();
    }

    public static House getHouse() {
        return house;
    }

    public static Bomberman getBomberman() {
        return bomberman;
    }

    private static Entity createEntity(char code, int row, int column) {
        switch (code) {
            case '.':
                return new Background(row, column);
            case '#':
                return new Wall(row, column);
            case '*':
                bomberman = new Bomberman(row, column);
                return bomberman;
            case '>':
                house = new House(row, column);
                return house;
            case '<':
                key = new Key(row, column);
                return key;
            case '!':
                Enemy enemy = new Enemy(row, column);
                enemies.add(enemy);
                return enemy;
            default:
                throw new IllegalArgumentException("Código inválido: " + code);
        }
    }

    public static void refreshBoard() {
        for (int row = 0; row < MAX_ROW; row++) {
            for (int column = 0; column < MAX_COLUMN; column++) {
                if (board.getLabelIcon(row, column) != entities[row][column].getIcon()) {
                    board.setLabelIcon(row, column, entities[row][column].getIcon());
                }
            }
        }
    }

    public static void main(String[] args) {
        initLevel(currentLevel);
        initItemsPanel();
        initNavigationButtonsPanel1();
        initNavigationButtonsPanel2();
        initMainPanel();
        initMainFrame();
        initEnemyTimer();
    }

    private static void initLevel(int level) {
        entities = new Entity[MAX_ROW][MAX_COLUMN];

        bomberman = null;
        house = null;
        key = null;
        enemies = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(String.format("levels/level_%d.txt", level)))) {
            String line = null;
            int row = 0;

            while ((line = reader.readLine()) != null) {
                for (int column = 0; column < MAX_COLUMN; column++) {
                    Entity entity = createEntity(line.charAt(column), row, column);
                    entities[row][column] = entity;

                    board.setLabelIcon(row, column, entity.getIcon());
                }

                row++;
            }

            reader.close();
        } catch (IOException e) {
            System.out.println(e);
            System.exit(-1);
        }
    }

    private static void initItemsPanel() {
        keyLabel = new JLabel(Board.loadIcon("key_64.png"));
        keyLabel.setToolTipText("Você não possui a chave.");
        keyLabel.setEnabled(false);

        itemsPanel = new JPanel(new FlowLayout());
        itemsPanel.add(keyLabel);
    }

    private static void initNavigationButtonsPanel1() {
        JButton moveUpButton = new JButton("\u2191");
        moveUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bomberman.move(Direction.UP);
            }
        });

        navigationsButtonsPanel1 = new JPanel(new FlowLayout());
        navigationsButtonsPanel1.add(moveUpButton);
    }

    private static void initNavigationButtonsPanel2() {
        JButton moveLeftButton = new JButton("\u2190");
        moveLeftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bomberman.move(Direction.LEFT);
            }
        });

        JButton moveDownButton = new JButton("\u2193");
        moveDownButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bomberman.move(Direction.DOWN);
            }
        });

        JButton moveRightButton = new JButton("\u2192");
        moveRightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bomberman.move(Direction.RIGHT);
            }
        });

        navigationsButtonsPanel2 = new JPanel(new FlowLayout());
        navigationsButtonsPanel2.add(moveLeftButton);
        navigationsButtonsPanel2.add(moveDownButton);
        navigationsButtonsPanel2.add(moveRightButton);
    }

    private static void initMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(board);
        mainPanel.add(itemsPanel);
        mainPanel.add(navigationsButtonsPanel1);
        mainPanel.add(navigationsButtonsPanel2);
    }

    private static void initMainFrame() {
        mainFrame = new JFrame();
        mainFrame.add(mainPanel);
        mainFrame.setTitle(String.format("Jogo - Nível %d", currentLevel));
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private static void initEnemyTimer() {
        enemiesTimer = new Timer();

        for (Enemy enemy : enemies) {
            int delay = ThreadLocalRandom.current().nextInt(0, 1001);

            enemiesTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    enemy.move();
                }
            }, delay, 1500);
        }
    }

    public static void collectKey() {
        // JOptionPane.showMessageDialog(mainFrame, String.format("Você encontrou a
        // chave!"));

        keyLabel.setToolTipText("Você possui a chave.");
        keyLabel.setEnabled(true);

        house.unlockDoor();

        Game.addEntity(new Background(key.getRow(), key.getColumn()));
        Game.refreshBoard();
    }

    public static void openDoor() {
        if (house.getIsDoorLocked() == true) {
            JOptionPane.showMessageDialog(mainFrame, "A porta está trancada! É necessário encontrar a chave primeiro.");
            return;
        }

        nextLevel();
    }

    public static void nextLevel() {
        enemiesTimer.cancel();

        JOptionPane.showMessageDialog(mainFrame, String.format("Parabéns! Você concluiu o nível %d!", currentLevel));

        if (currentLevel >= MAX_LEVEL) {
            victory();
            return;
        }

        currentLevel++;

        // Trocar de lugar...
        keyLabel.setToolTipText("Você não possui a chave.");
        keyLabel.setEnabled(false);

        mainFrame.setTitle(String.format("Jogo - Nível %d", currentLevel));

        initLevel(currentLevel);
        initEnemyTimer();
    }

    public static void victory() {
        enemiesTimer.cancel();

        JOptionPane.showMessageDialog(mainFrame, "Parabéns! Você ganhou!");
        System.exit(0);
    }

    public static void defeat() {
        enemiesTimer.cancel();

        JOptionPane.showMessageDialog(mainFrame, "Você perdeu!");
        System.exit(0);
    }
}