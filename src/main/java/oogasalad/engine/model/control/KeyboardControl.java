package oogasalad.engine.model.control;

import javafx.scene.Scene;
import oogasalad.engine.model.Entity;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

/**
 * Control strategy using the arrow keys to indicate pacman's direction
 */
public class KeyboardControl implements ControlStrategy {
    private final Set<KeyCode> myActiveKeys = new HashSet<>();
    private final double SPEED = 5.0;

    /**
     * Determine which keys are currently being pressed to determine which actions
     * should be carried out
     * @param scene: The scene that the entity is moving in
     */
    public KeyboardControl(Scene scene) {
        scene.setOnKeyPressed(event -> myActiveKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> myActiveKeys.remove(event.getCode()));
    }

    /**
     * Overridden update method to define the keyboard controls
     *
     * @param entity: The entity being controlled/updated
     */
    @Override
    public void update(Entity entity) {
        double dx = 0, dy = 0;

        if (myActiveKeys.contains(KeyCode.UP)) {
            dy -= SPEED;
        }
        if (myActiveKeys.contains(KeyCode.DOWN)) {
            dy += SPEED;
        }
        if (myActiveKeys.contains(KeyCode.LEFT)) {
            dx -= SPEED;
        }
        if (myActiveKeys.contains(KeyCode.RIGHT)) {
            dx += SPEED;
        }

        entity.move(dx, dy);
    }
}

