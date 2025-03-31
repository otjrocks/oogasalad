package oogasalad.engine.model;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import oogasalad.engine.model.api.ControlStrategyFactory;
import oogasalad.engine.model.control.ControlStrategy;
import oogasalad.player.view.EntityView;

/**
 * A record to encapsulate a game entity.
 *
 * @author Owen Jennings
 * @author Troy Ludwig
 */
public class Entity {

    private double myX, myY;
    private final EntityData myData;
    private final ControlStrategy myControlStrategy;
    private final ImageView myImage;

    public Entity(Scene scene, EntityData data) {
        myData = data;
        myX = myData.getInitialX();
        myY = myData.getInitialY();
        myControlStrategy = ControlStrategyFactory.getStrategy(scene, myData.getControlType());
        myImage = new EntityView(myData);
    }

    public void update() {
        if (myControlStrategy != null) {
            myControlStrategy.update(this);
        }
    }

    public void move(double dx, double dy) {
        myX += dx;
        myY += dy;
        myImage.setX(myX);
        myImage.setY(myY);
    }

    public EntityData getEntityData() {
        return myData;
    }

    public double getX() {
        return myX;
    }

    public double getY() {
        return myY;
    }

    public ImageView getMyImage() {
        return myImage;
    }
}
