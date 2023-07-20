public class Wall extends Entity {
    public Wall(int row, int column) {
        super(row, column, Type.WALL, "brick_01.png");
    }

    @Override
    public void action(Entity source) {
    }
}
