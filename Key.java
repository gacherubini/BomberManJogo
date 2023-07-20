public class Key extends Entity {
    public Key(int row, int column) {
        super(row, column, Type.KEY, "key.png");
    }

    @Override
    public void action(Entity source) {
        Game.collectKey();
    }
}
