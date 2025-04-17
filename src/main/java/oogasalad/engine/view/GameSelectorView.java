package oogasalad.engine.view;

import static oogasalad.engine.config.GameConfig.ELEMENT_SPACING;

import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.ThemeManager;
import oogasalad.engine.config.GameConfig;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.view.components.Selector;

/**
 * The game selector splash screen after game player button on splash screen is clicked.
 *
 * @author Austin Huang
 */
public class GameSelectorView extends VBox {

  private Selector myLanguageSelector;
  private final ThemeManager myThemeManager;
  private Selector myThemeSelector;
  private final MainController myMainController;

  /**
   * Create a game selector splash screen view.
   *
   * @param mainController The main controller of the program.
   */
  public GameSelectorView(MainController mainController) {
    super();
    myThemeManager = new ThemeManager(mainController.getStage());
    myMainController = mainController;
    this.setPrefSize(GameConfig.WIDTH, GameConfig.HEIGHT);
    this.setSpacing(20);
    this.setAlignment(Pos.CENTER);
    this.getStyleClass().add("game-selector-view");

    // initializeTitle()
    Label title = new Label(LanguageManager.getMessage("GAME_PLAYER"));
    title.setId("gameSelectorScreenTitle");
    title.getStyleClass().add("title");

    // initializeHBox()
    HBox gameGrid = new HBox(30);
    gameGrid.setAlignment(Pos.CENTER);

    List<String> gameNames = List.of("Basic Pacman", "Complex Pacman", "Pacman", "Pacman");
    for (String name : gameNames) {
      VBox gameCard = createGameCard(name);
      gameGrid.getChildren().add(gameCard);
    }

    HBox topBar = new HBox(10);
    topBar.setAlignment(Pos.CENTER);
    Button back = new Button("Back");
    back.getStyleClass().add("small-button");
    Button help = new Button("Help");
    help.getStyleClass().add("small-button");
    topBar.getChildren().addAll(back, title, help);
    HBox.setHgrow(title, Priority.ALWAYS);
    title.setMaxWidth(Double.MAX_VALUE);
    title.setAlignment(Pos.TOP_CENTER);

    this.getChildren().addAll(topBar, gameGrid);

    back.setOnAction(e -> {
      mainController.hideGameSelectorView();
      mainController.showSplashScreen();
    });
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
