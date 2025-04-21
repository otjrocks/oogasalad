package oogasalad.player.model.strategies.collision;

import java.util.*;
import oogasalad.engine.records.CollisionContextRecord;
import oogasalad.player.model.Entity;

/**
 * Temporarily changes the mode of all entities of a given type, and reverts after a duration.
 * This version is designed to work without multithreading, using elapsedTime to track reversion.
 */
public class TemporaryModeChangeStrategy implements CollisionStrategyInterface {

    private final String entityType;
    private final String temporaryMode;
    private final double duration;

    public TemporaryModeChangeStrategy(String entityType, String temporaryMode, double durationSeconds) {
        this.entityType = entityType;
        System.out.println(entityType);
        this.temporaryMode = temporaryMode;
        this.duration = durationSeconds;
    }

    @Override
    public void handleCollision(CollisionContextRecord context) {
        double currentTime = context.gameState().getTimeElapsed();

        for (Entity entity : context.gameMap()) {
            if (entity.getEntityPlacement().getTypeString().equals(entityType)
                    && !context.gameMap().getActiveModeChanges().containsKey(entity)) {

                String originalMode = entity.getEntityPlacement().getMode();
                entity.getEntityPlacement().setMode(temporaryMode);

                context.gameMap().getActiveModeChanges().put(entity,
                        new ModeChangeInfo(originalMode, currentTime + duration));
            }
        }
    }

    public static class ModeChangeInfo {
        public String originalMode;
        public double revertTime;

        ModeChangeInfo(String originalMode, double revertTime) {
            this.originalMode = originalMode;
            this.revertTime = revertTime;
        }
    }
}

