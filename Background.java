public class Background extends Entity {
    public Background(int row, int column) {
        super(row, column, Type.BACKGROUND, "snow_01.png");
    }

    @Override
    public void action(Entity source) {
        throw new UnsupportedOperationException("Ação não implementada!");
    }
}
