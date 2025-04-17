package oogasalad.player.view;

import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.utility.ThemeManager;
import oogasalad.engine.utility.constants.GameConfig;
import oogasalad.engine.controller.MainController;

/**
 * The game selector splash screen after game player button on splash screen is clicked.
 *
 * @author Austin Huang
 */
public class GameSelectorView extends VBox {

  private final MainController myMainController;

  /**
   * Create a game selector splash screen view.
   *
   * @param mainController The main controller of the program.
   */
  public GameSelectorView(MainController mainController) {
    super();
    ThemeManager myThemeManager = new ThemeManager(mainController.getStage());
    myMainController = mainController;
    setupLayout();

    HBox topBar = createTopBar();
    HBox gameGrid = createGameGrid(List.of("Basic Pacman", "Complex Pacman", "Pacman", "Pacman"));

    this.getChildren().addAll(topBar, gameGrid);
  }

  private void setupLayout() {
    this.setPrefSize(GameConfig.WIDTH, GameConfig.HEIGHT);
    this.setSpacing(20);
    this.setAlignment(Pos.CENTER);
    this.getStyleClass().add("game-selector-view");
  }

  private HBox createGameGrid(List<String> gameNames) {
    HBox gameGrid = new HBox(30);
    gameGrid.setAlignment(Pos.CENTER);
    for (String name : gameNames) {
      VBox gameCard = createGameCard(name);
      gameGrid.getChildren().add(gameCard);
    }
    return gameGrid;
  }

  private HBox createTopBar() {
    Label title = new Label(LanguageManager.getMessage("GAME_PLAYER"));
    title.getStyleClass().add("title");
    HBox topBar = new HBox(10);
    topBar.setAlignment(Pos.CENTER);
    Button back = new Button("Back");
    back.getStyleClass().add("small-button");
    back.setOnAction(e -> {
      myMainController.hideGameSelectorView();
      myMainController.showSplashScreen();
    });
    Button help = new Button("Help");
    help.getStyleClass().add("small-button");
    topBar.getChildren().addAll(back, title, help);
    HBox.setHgrow(title, Priority.ALWAYS);
    title.setMaxWidth(Double.MAX_VALUE);
    title.setAlignment(Pos.TOP_CENTER);
    return topBar;
  }

  private VBox createGameCard(String gameName) {
    VBox card = new VBox(10);
    card.setAlignment(Pos.CENTER);

    // placeholder image to be replaced later
    Image im = new Image(getClass().getResourceAsStream("/assets/images/placeholder.png"));
    ImageView image = new ImageView(im); // Replace with actual path
    image.setFitWidth(200);
    image.setFitHeight(400);

    Label name = new Label(gameName);
    name.getStyleClass().add("game-name");

    card.getChildren().addAll(image, name);
    card.setOnMouseClicked(e -> {
      // TODO: Load game based on gameName
      // to be refactored to accommodate different games
      myMainController.hideGameSelectorView();
      myMainController.showGamePlayerView();
    });

    return card;
  }
}
