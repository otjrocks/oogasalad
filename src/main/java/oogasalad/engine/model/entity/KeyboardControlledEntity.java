package oogasalad.engine.model.entity;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import oogasalad.engine.model.EntityData;

import java.util.HashSet;
import java.util.Set;

public class KeyboardControlledEntity extends Entity {
    private final Set<KeyCode> myActiveKeys = new HashSet<>();
    private final double SPEED = 1.0;

    public KeyboardControlledEntity(Group root, EntityData entityData) {
        super(entityData);
        root.setOnKeyPressed(event -> myActiveKeys.add(event.getCode()));
        root.setOnKeyReleased(event -> myActiveKeys.remove(event.getCode()));
    }

    @Override
    public void update() {
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

        getEntityData().setInitialX(getEntityData().getInitialX() + dx);
        getEntityData().setInitialY(getEntityData().getInitialY() + dy);
    }
}
