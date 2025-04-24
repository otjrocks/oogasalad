package oogasalad.player.model.strategies.modechangeevent;

import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.model.ModeChangeEventRecord;

/**
 * An interface that defines how/whether a mode change should occur
 *
 * @author Troy Ludwig
 */
public interface ModeChangeEventStrategyInterface {
    /**
     * Determine if a mode change event should occur based on the context of the game
     *
     * @param modeChangeEvent : What mode should the entity be changed to
     * @param gameContextRecord The game context information to use in your determination
     */
    boolean shouldChange(ModeChangeEventRecord modeChangeEvent, GameContextRecord gameContextRecord);
}
