package oogasalad.player.model.strategies.control;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Follows closely behind an entity by doing its moves after a short delay
 */
public class FollowEntityControlStrategy implements ControlStrategyInterface {
    private final GameMapInterface gameMap;
    private final Queue<double[]> positionQueue;
    private final Entity targetEntity;
    private static final int DELAY = 5;

    /**
     *
     * @param gameMap: The map in which the entity is being updated
     * @param placement: The placement of the entity (unused)
     * @param config: The controlConfig associated (also unused)
     */
    public FollowEntityControlStrategy(GameMapInterface gameMap, EntityPlacement placement, ControlConfigInterface config) {
        this.gameMap = gameMap;
        this.positionQueue = new LinkedList<>();
        this.targetEntity = findTargetEntity();
    }

    private Entity findTargetEntity() {
        Entity Pacman = null;
        List<Entity> temp = new ArrayList<>();
        gameMap.iterator().forEachRemaining(temp::add);

        for (int i = temp.size() - 1; i >= 0; i--) {
            Entity entity = temp.get(i);
            if (entity.getEntityPlacement().getTypeString().equals("Tail")) {
                return entity;
            }
            if(entity.getEntityPlacement().getTypeString().equals("Pacman")) {
                Pacman = entity;
            }
        }
        return Pacman;
    }

    @Override
    public void update(Entity entity) {
        if (targetEntity == null) return;

        double[] targetPos = { targetEntity.getEntityPlacement().getX(), targetEntity.getEntityPlacement().getY() };

        positionQueue.add(targetPos);

        if (positionQueue.size() > DELAY) {
            double[] nextPos = positionQueue.poll();

            double dx = nextPos[0] - entity.getEntityPlacement().getX();
            double dy = nextPos[1] - entity.getEntityPlacement().getY();

            if (dx != 0 || dy != 0) {
                entity.setEntityDirection(targetEntity.getEntityDirection());
                entity.setDx(dx);
                entity.setDy(dy);
            }
        }
    }
}
