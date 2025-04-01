package oogasalad.player.view;

import static oogasalad.engine.config.GameConfig.HEIGHT;
import static oogasalad.engine.config.GameConfig.WIDTH;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.model.GameState;

/**
 * The main screen that contains the HUD elements and the player view.
 */
public class GameScreenView extends BorderPane {

    private final GameState gameState;
    private final Label scoreLabel;
    private final Label livesLabel;
    private final VBox hudContainer;
    private final GamePlayerView gamePlayerView;

    /**
     * Create the Game Screen View that contains the HUD and GamePlayerView.
     */
    public GameScreenView(MainController controller, GameState gameState) {
        super();
        this.gameState = gameState;

        scoreLabel = new Label("Score: " + gameState.getScore());
        livesLabel = new Label("Lives: " + gameState.getLives());
        hudContainer = new VBox(10, scoreLabel, livesLabel);
        hudContainer.setStyle("-fx-padding: 10; -fx-background-color: lightgray;");

        gamePlayerView = new GamePlayerView(controller, gameState);

        this.setTop(hudContainer);
        this.setCenter(gamePlayerView);
    }

    /**
     * Updates the HUD display based on game state changes.
     */
    public void updateHud() {
        scoreLabel.setText("Score: " + gameState.getScore());
        livesLabel.setText("Lives: " + gameState.getLives());
    }

    /**
     * Expose the gamePlayerView for other components if needed.
     */
    public GamePlayerView getGamePlayerView() {
        return gamePlayerView;
    }
}
