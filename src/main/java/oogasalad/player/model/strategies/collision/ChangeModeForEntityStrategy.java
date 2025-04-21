package oogasalad.player.model.strategies.collision;

import oogasalad.engine.records.CollisionContextRecord;
import oogasalad.engine.records.config.model.ModeChangeInfo;

public class ChangeModeForEntityStrategy implements CollisionStrategyInterface{
    private final String newMode;
    private final double duration;

    /**
     * Temporarily changes the mode of an entity
     *
     */
    public ChangeModeForEntityStrategy(String newMode, int duration) {
        this.newMode = newMode;
        this.duration = duration;
    }

    @Override
    public void handleCollision(CollisionContextRecord context) {
        double currentTime = context.gameState().getTimeElapsed();

            String originalMode = context.entity1().getEntityPlacement().getMode();
            if(context.gameMap().getActiveModeChanges().containsKey(context.entity1())) {
                originalMode = context.gameMap().getActiveModeChanges().get(context.entity1()).originalMode();
                context.gameMap().getActiveModeChanges().remove(context.entity1());
            }
        context.entity1().getEntityPlacement().setMode(newMode);

            context.gameMap().getActiveModeChanges().put(context.entity1(),
                    new ModeChangeInfo(originalMode, newMode, currentTime + duration, currentTime + duration));
            }
        }
