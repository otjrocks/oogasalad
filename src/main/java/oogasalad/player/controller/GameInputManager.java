package oogasalad.player.controller;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * Class for handling user input for controllable entities in the game player
 */
public class GameInputManager {
    private final Group root;
  private final Set<KeyCode> activeKeys = new HashSet<>();
    private KeyCode lastActiveKey = null;

    /**
     * Constructor for the input manager that records what keys are actively
     * being pressed by the user.
     *
     * @param scene: The scene currently displaying the controllable entities
     * @param root: The content root the game player is a child of
     */
    public GameInputManager(Scene scene, Group root) {
      this.root = root;
        scene.setOnKeyPressed(event -> {
          activeKeys.remove(lastActiveKey);
          activeKeys.add(event.getCode());
          lastActiveKey = event.getCode();
        });
    }

    /**
     * Returns the content root referenced by the content root.
     */
    public Group getRoot() {
        return root;
    }

    /**
     * Checks if the user wants the entity to go up.
     */
    public boolean isMovingUp() {
        return activeKeys.contains(KeyCode.UP);
    }

    /**
     * Checks if the user wants the entity to go down.
     */
    public boolean isMovingDown() {
        return activeKeys.contains(KeyCode.DOWN);
    }

    /**
     * Checks if the user wants the entity to go left.
     */
    public boolean isMovingLeft() {
        return activeKeys.contains(KeyCode.LEFT);
    }

    /**
     * Checks if the user wants the entity to go right.
     */
    public boolean isMovingRight() {
        return activeKeys.contains(KeyCode.RIGHT);
    }

    //These will be abstracted later
}

