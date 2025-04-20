package oogasalad.player.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import oogasalad.engine.config.JsonConfigParser;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.exceptions.ConfigException;
import oogasalad.engine.records.config.GameConfigRecord;
import oogasalad.engine.utility.FileUtility;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.engine.utility.ThemeManager;
import oogasalad.engine.utility.constants.GameConfig;

/**
 * The game selector splash screen after game player button on splash screen is clicked.
 *
 * @author Austin Huang
 */
public class GameSelectorView {

  private final VBox myRoot;
  private final MainController myMainController;
  private final List<GameConfigRecord> gameConfigRecords;

  /**
   * Create a game selector splash screen view.
   *
   * @param mainController The main controller of the program.
   */
  public GameSelectorView(MainController mainController) {
    super();
    myRoot = new VBox();
    new ThemeManager(mainController.getStage());
    myMainController = mainController;
    setupLayout();

    gameConfigRecords = getAllGameConfigs();

    HBox topBar = createTopBar();

    List<String> gameNames = new ArrayList<>();
    for (GameConfigRecord gameConfigRecord : gameConfigRecords) {
      gameNames.add(gameConfigRecord.metadata().gameTitle());
    }
    Pagination gameGrid = createGameGrid(gameNames);

    myRoot.getChildren().addAll(topBar, gameGrid);
  }

  /**
   * Return the root JavaFX element for this view.
   *
   * @return A VBox which is used to display this view.
   */
  public VBox getRoot() {
    return myRoot;
  }

  private void setupLayout() {
    myRoot.setPrefSize(GameConfig.WIDTH, GameConfig.HEIGHT);
    myRoot.setSpacing(20);
    myRoot.setAlignment(Pos.CENTER);
    myRoot.getStyleClass().add("game-selector-view");
  }

  private Pagination createGameGrid(List<String> gameNames) {
    int itemsPerPage = 4;
    int totalPages = (int) Math.ceil((double) gameNames.size() / itemsPerPage);

    Pagination pagination = new Pagination(totalPages, 0);
    pagination.setPageFactory(new Callback<>() {
      @Override
      public VBox call(Integer pageIndex) {
        VBox pageBox = new VBox(10);
        pageBox.setAlignment(Pos.CENTER);

        if (gameNames.isEmpty()) {
          Label emptyLabel = new Label("No games available.");
          pageBox.getChildren().add(emptyLabel);
          return pageBox;
        }

        int start = pageIndex * itemsPerPage;
        int end = Math.min(start + itemsPerPage, gameNames.size());
        List<String> pageGames = gameNames.subList(start, end);

        HBox gameGrid = new HBox(30);
        gameGrid.setAlignment(Pos.CENTER);
        gameGrid.getStyleClass().add("game-grid");


        for (String name : pageGames) {
          VBox gameCard = createGameCard(name);
          gameGrid.getChildren().add(gameCard);
        }

        pageBox.getChildren().add(gameGrid);
        return pageBox;
      }
    });

    return pagination;
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
    card.getStyleClass().add("game-card");

    // placeholder image to be replaced later
    Image im = new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/placeholder.png")));
    ImageView image = new ImageView(im); // Replace with actual path
    image.setFitWidth(200);
    image.setFitHeight(400);

    Label name = new Label(gameName);
    name.getStyleClass().add("game-name");

    Button randomizeButton = new Button("Randomize Levels");
    randomizeButton.getStyleClass().add("small-button");
    randomizeButton.setOnAction(e -> {
      System.out.println("Randomizing levels for " + gameName);
    });

    card.getChildren().addAll(image, name, randomizeButton);
    card.setOnMouseClicked(e -> {
      // TODO: Load game based on gameName
      // to be refactored to accommodate different games
      myMainController.hideGameSelectorView();
      myMainController.showGamePlayerView();
    });

    return card;
  }

  private List<GameConfigRecord> getAllGameConfigs() {
    JsonConfigParser configParser = new JsonConfigParser();
    String gamesFolderPath = "data/games/";

    List<String> folderNames = FileUtility.getFolderNamesInDirectory(gamesFolderPath);
    List<GameConfigRecord> gameConfigs = new ArrayList<>();
    for (String folderName : folderNames) {
      try {
        gameConfigs.add(
            configParser.loadGameConfig(gamesFolderPath + folderName + "/gameConfig.json"));
      } catch (ConfigException e) {
        LoggingManager.LOGGER.warn("Could not load config file " + folderName + e);
        // not an error, just don't load game if no config
      }
    }

    return gameConfigs;
  }
}
