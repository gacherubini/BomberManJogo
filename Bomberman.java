import java.util.ArrayList;

import enums.Direction;

public class Bomberman extends Entity {
    public Bomberman(int row, int column) {
        super(row, column, Type.BOMBERMAN, "bomberman.png");
    }

    public void move(Direction direction) {
        Entity target;

        switch (direction) {
            case UP:
                target = Game.getEntity(this.getRow() - 1, this.getColumn());
                break;
            case DOWN:
                target = Game.getEntity(this.getRow() + 1, this.getColumn());
                break;
            case LEFT:
                target = Game.getEntity(this.getRow(), this.getColumn() - 1);
                break;
            case RIGHT:
                target = Game.getEntity(this.getRow(), this.getColumn() + 1);
                break;
            default:
                throw new IllegalArgumentException("Direção inválida: " + direction);
        }

        if (target == null)
            return;

        if (target.getType() == Type.BACKGROUND) {
            Game.addEntity(new Background(this.getRow(), this.getColumn()));

            this.setRow(target.getRow());
            this.setColumn(target.getColumn());

            Game.addEntity(this);
            Game.refreshBoard();

            // Verifica se há inimigos por perto.
            ArrayList<Entity> entities = new ArrayList<>();

            entities.add(Game.getEntity(this.getRow() - 1, this.getColumn()));
            entities.add(Game.getEntity(this.getRow() + 1, this.getColumn()));
            entities.add(Game.getEntity(this.getRow(), this.getColumn() - 1));
            entities.add(Game.getEntity(this.getRow(), this.getColumn() + 1));

            for (Entity entity : entities) {
                if (entity != null && entity.getType() == Type.ENEMY) {
                    this.action(entity);
                    break;
                }
            }

            return;
        }

        target.action(this);
    }

    @Override
    public void action(Entity source) {
        if (source.getType() == Type.ENEMY) {
            this.setIcon(Board.loadIcon("skull.png"));
            Game.refreshBoard();
            Game.defeat();
        }
    }
}
