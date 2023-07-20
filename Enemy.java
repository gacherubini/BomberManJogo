import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Enemy extends Entity {
    private int previousRow;
    private int previousColumn;

    public Enemy(int row, int column) {
        super(row, column, Type.ENEMY, "enemy_01.png");
    }

    private void attack() {
        ArrayList<Entity> targets = new ArrayList<>();

        targets.add(Game.getEntity(this.getRow() - 1, this.getColumn()));
        targets.add(Game.getEntity(this.getRow() + 1, this.getColumn()));
        targets.add(Game.getEntity(this.getRow(), this.getColumn() - 1));
        targets.add(Game.getEntity(this.getRow(), this.getColumn() + 1));

        for (Entity target : targets) {
            if (target.getType() == Type.BOMBERMAN) {
                target.action(this);
                break;
            }
        }
    }

    public void move() {
        ArrayList<Entity> entities = new ArrayList<>();
        ArrayList<Entity> possibleTargets = new ArrayList<>();
        int randomIndex;

        entities.add(Game.getEntity(this.getRow() - 1, this.getColumn()));
        entities.add(Game.getEntity(this.getRow() + 1, this.getColumn()));
        entities.add(Game.getEntity(this.getRow(), this.getColumn() - 1));
        entities.add(Game.getEntity(this.getRow(), this.getColumn() + 1));

        for (Entity entity : entities) {
            if (entity != null && entity.getType() == Type.BACKGROUND) {
                // Chance menor de retornar para a posição anterior.
                if (entity.getRow() == this.previousRow && entity.getColumn() == this.previousColumn) {
                    randomIndex = ThreadLocalRandom.current().nextInt(0, 2);

                    if (randomIndex != 0)
                        continue;
                }

                possibleTargets.add(entity);
            }
        }

        if (possibleTargets.size() == 0)
            return;

        randomIndex = ThreadLocalRandom.current().nextInt(0, possibleTargets.size());

        Entity target = possibleTargets.get(randomIndex);

        Game.addEntity(new Background(this.getRow(), this.getColumn()));

        this.previousRow = this.getRow();
        this.previousColumn = this.getColumn();

        this.setRow(target.getRow());
        this.setColumn(target.getColumn());

        Game.addEntity(this);
        Game.refreshBoard();

        this.attack();
    }

    @Override
    public void action(Entity source) {
    }
}
