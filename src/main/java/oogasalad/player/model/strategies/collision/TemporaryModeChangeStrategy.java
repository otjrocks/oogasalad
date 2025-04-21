package oogasalad.player.model.strategies.collision;

import oogasalad.engine.records.CollisionContextRecord;
import oogasalad.engine.records.config.model.ModeChangeInfo;
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

    /**
     * Temporarily changes the mode of an entity
     *
     * @param entityType: The type of entity you want to change
     * @param temporaryMode: What mode it should be changed to
     * @param transitionMode: A transition mode for returning to default
     * @param durationSeconds: The amount of time the entity to take before returning to default
     * @param transitionTime: The length of the transition period
     */
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
            if (entity.getEntityPlacement().getTypeString().equals(entityType)) {

                String originalMode = entity.getEntityPlacement().getMode();
                if(context.gameMap().getActiveModeChanges().containsKey(entity)) {
                    originalMode = context.gameMap().getActiveModeChanges().get(entity).originalMode();
                    context.gameMap().getActiveModeChanges().remove(entity);
                }
                entity.getEntityPlacement().setMode(temporaryMode);

                context.gameMap().getActiveModeChanges().put(entity,
                        new ModeChangeInfo(originalMode, transitionMode, currentTime + duration, currentTime + duration - transitionTime));
            }
        }
    }
}

