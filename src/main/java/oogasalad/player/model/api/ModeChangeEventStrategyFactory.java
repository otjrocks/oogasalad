package oogasalad.player.model.api;

import oogasalad.player.model.strategies.modechangeevent.ModeChangeEventStrategyInterface;
import oogasalad.player.model.strategies.spawnevent.SpawnEventStrategyInterface;

public class ModeChangeEventStrategyFactory {
    private static final String STRATEGY_PACKAGE = "oogasalad.player.model.strategies.modechangeevent";

    /**
     * Create the correct ModeChangeEventStrategy corresponding to the type provided.
     *
     * @param type The type string of the mode change event strategy you wish to create.
     */
    public static ModeChangeEventStrategyInterface createSpawnEventStrategy(String type) {
        try {
            Class<?> clazz = Class.forName(STRATEGY_PACKAGE + "." + type + "ModeChangeEventStrategy");
            if (ModeChangeEventStrategyInterface.class.isAssignableFrom(clazz)) {
                return (ModeChangeEventStrategyInterface) clazz.getDeclaredConstructor().newInstance();
            } else {
                throw new IllegalArgumentException(
                        type + "ModeChangeEventStrategy does not implement ModeChangeEventStrategy.");
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Strategy class not found for type: " + type, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate strategy for type: " + type, e);
        }
    }
}
