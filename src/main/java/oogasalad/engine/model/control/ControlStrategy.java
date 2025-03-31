package oogasalad.engine.model.control;

import oogasalad.engine.model.Entity;

public interface ControlStrategy {
    void update(Entity entity);
}
