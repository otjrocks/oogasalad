package oogasalad.engine.model.control;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import oogasalad.engine.model.Entity;
import oogasalad.engine.model.EntityData;

/**
 * Executable test for controlling PacMan because I couldn't figure out TestFX
 */
public class KeyboardTest extends Application {
    private Entity player;

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 600, 400);

        EntityData playerData = new EntityData();
        playerData.setControlType("keyboard");
        playerData.setImagePath("assets/images/pacman.png");

        player = new Entity(scene, playerData);
        root.getChildren().add(player.getMyImage());

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                player.update();
            }
        };
        gameLoop.start();

        stage.setScene(scene);
        stage.setTitle("Controllable Entity");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
