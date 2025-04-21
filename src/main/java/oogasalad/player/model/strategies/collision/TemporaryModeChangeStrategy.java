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
    private final String transitionMode;
    private final double duration;
    private final double transitionTime;

    public TemporaryModeChangeStrategy(String entityType, String temporaryMode,
                                       String transitionMode, double durationSeconds, double transitionTime) {
        this.entityType = entityType;
        this.temporaryMode = temporaryMode;
        this.duration = durationSeconds;
        this.transitionMode = transitionMode;
        this.transitionTime = transitionTime;
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
                        new ModeChangeInfo(originalMode, transitionMode, currentTime + duration, currentTime + duration - transitionTime));
            }
        }
    }

    public static class ModeChangeInfo {
        public String originalMode;
        public String transitionMode;
        public double transitionTime;
        public double revertTime;

        ModeChangeInfo(String originalMode, String transitionMode, double revertTime, double transitionTime) {
            this.originalMode = originalMode;
            this.transitionMode = transitionMode;
            this.revertTime = revertTime;
            this.transitionTime = transitionTime;
        }
    }
}

