package oogasalad.player.model.strategies.modechangeevent;

import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.model.ModeChangeEventRecord;
import oogasalad.engine.utility.LoggingManager;

public class TimeElapsedModeChangeEventStrategy implements ModeChangeEventStrategyInterface{

    public boolean shouldChange(ModeChangeEventRecord modeChangeEvent, GameContextRecord gameContextRecord){
        Object amountObj = modeChangeEvent.changeCondition().parameters().get("amount");
        if (amountObj == null) {
            LoggingManager.LOGGER.warn(
                    "TimeElapsedModeChangeEventStrategy changeCondition requires amount parameter, but it was not provided in the config, defaulting to never changing entity.");
            return false;
        }
        try {
            int amount = Integer.parseInt(amountObj.toString());
            return gameContextRecord.gameState().getTimeElapsed() >= amount && gameContextRecord.gameState().getTimeElapsed() < amount+1;
        } catch (NumberFormatException e) {
            LoggingManager.LOGGER.warn(
                    "TimeElapsedModeChangeEventStrategy changeCondition parameter 'amount' must be an integer, but received: {}",
                    amountObj);
            return false;
        }
    }
}
