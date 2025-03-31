package oogasalad.engine.model.api;

import javafx.scene.Scene;
import oogasalad.engine.model.control.ControlStrategy;
import oogasalad.engine.model.control.KeyboardControl;

/**
 * Generates different control strategies based on a String argument
 */
public class ControlStrategyFactory {

    public static ControlStrategy getStrategy(Scene scene, String strategyType) {
        return switch (strategyType.toLowerCase()) {
            case "keyboard" -> new KeyboardControl(scene);
            default -> throw new IllegalArgumentException("Unknown strategy: " + strategyType);
        };
    }
}
