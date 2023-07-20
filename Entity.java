import javax.swing.ImageIcon;

public abstract class Entity {
    public enum Type {
        BACKGROUND,
        WALL,
        BOMBERMAN,
        ENEMY,
        KEY,
        HOUSE
    }

    private int row;
    private int column;
    private Type type;
    private ImageIcon icon;

    public Entity(int row, int column, Type type, String iconFileName) {
        this.row = row;
        this.column = column;
        this.type = type;
        this.icon = Board.loadIcon(iconFileName);
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public Type getType() {
        return this.type;
    }

    public ImageIcon getIcon() {
        return this.icon;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public abstract void action(Entity source);

    @Override
    public String toString() {
        return String.format("%s[%s][%s]", this.type, this.row, this.column);
    }
}
