public class House extends Entity {
    private boolean isDoorLocked = true;

    public House(int row, int column) {
        super(row, column, Type.HOUSE, "house.png");
    }

    public boolean getIsDoorLocked() {
        return this.isDoorLocked;
    }

    public void unlockDoor() {
        this.isDoorLocked = false;
    }

    @Override
    public void action(Entity source) {
        Game.openDoor();
    }
}
