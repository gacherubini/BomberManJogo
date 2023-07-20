import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Image;

import java.net.URL;

import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

public class Board extends JPanel {
    private static final int ICON_WIDTH = 40;
    private static final int ICON_HEIGHT = 40;

    private static HashMap<String, ImageIcon> icons = new HashMap<>();

    private int MAX_ROW;
    private int MAX_COLUMN;

    private JLabel[][] labels;

    public Board(int rows, int columns) {
        super();

        this.MAX_ROW = rows;
        this.MAX_COLUMN = columns;

        this.labels = new JLabel[MAX_ROW][MAX_COLUMN];

        this.setLayout(new GridLayout(MAX_ROW, MAX_COLUMN));

        for (int row = 0; row < MAX_ROW; row++) {
            for (int column = 0; column < MAX_COLUMN; column++) {
                this.labels[row][column] = new JLabel(loadIcon("snow_02.png"));
                this.labels[row][column].setBorder(BorderFactory.createBevelBorder(0, Color.WHITE, Color.BLACK));

                this.add(this.labels[row][column]);
            }
        }
    }

    public static ImageIcon loadIcon(String fileName) {
        if (icons.containsKey(fileName))
            return icons.get(fileName);

        URL url = Board.class.getResource("images/" + fileName);

        if (url == null) {
            System.out.printf("Não foi possível encontrar o arquivo: %s", fileName);
            System.exit(-1);
        }

        ImageIcon icon = new ImageIcon(url);
        icon = new ImageIcon(icon.getImage().getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH));

        icons.put(fileName, icon);

        return icon;
    }

    public void setLabelIcon(int row, int column, ImageIcon icon) {
        this.labels[row][column].setIcon(icon);
    }

    public ImageIcon getLabelIcon(int row, int column) {
        return (ImageIcon) this.labels[row][column].getIcon();
    }
}
