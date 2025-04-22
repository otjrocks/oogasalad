package oogasalad.player.view;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
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

  private final String gamesFolderPath = "data/games/";
  private final String smallButtonString = "small-button";

  private final VBox myRoot;
  private final MainController myMainController;
  private final List<GameConfigRecord> gameConfigRecords;
  private final Map<String, String> gameNameToFolder = new HashMap<>();
  private JsonConfigParser configParser = new JsonConfigParser();
  private Label titleLabel;
  private Label fileLabel;
  private Button backButton;
  private Button helpButton;
  private Button uploadButton;
  private Button startButton;
  private FileChooser fileChooser;

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

    fileChooser = new FileChooser();
    HBox topBar = createTopBar();

    List<String> gameNames = new ArrayList<>();
    for (GameConfigRecord gameConfigRecord : gameConfigRecords) {
      gameNames.add(gameConfigRecord.metadata().gameTitle());
    }
    Pagination gameGrid = createGameGrid(gameNames);

    myRoot.getChildren().addAll(topBar, createFileUploadSection(), gameGrid);
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
    titleLabel = new Label(LanguageManager.getMessage("GAME_PLAYER"));
    titleLabel.getStyleClass().add("title");
    HBox topBar = new HBox(10);
    topBar.setAlignment(Pos.CENTER);

    backButton = new Button("Back");
    backButton.getStyleClass().add(smallButtonString);
    backButton.setOnAction(e -> {
      myMainController.hideGameSelectorView();
      myMainController.showSplashScreen();
    });

    helpButton = new Button("Help");
    helpButton.getStyleClass().add(smallButtonString);

    topBar.getChildren().addAll(backButton, titleLabel, helpButton);
    HBox.setHgrow(titleLabel, Priority.ALWAYS);
    titleLabel.setMaxWidth(Double.MAX_VALUE);
    titleLabel.setAlignment(Pos.TOP_CENTER);
    return topBar;
  }

  VBox createGameCard(String gameName) {
    VBox card = new VBox(10);
    card.setAlignment(Pos.CENTER);
    card.getStyleClass().add("game-card");

    ImageView image = new ImageView(getImage(gameName)); // Replace with actual path
    image.setFitWidth(200);
    image.setFitHeight(300);

    Label name = new Label(gameName);
    name.getStyleClass().add("game-name");

    Button randomizeButton = new Button("Randomize Levels");
    randomizeButton.getStyleClass().add(smallButtonString);
    randomizeButton.setOnAction(e -> {
      myMainController.hideGameSelectorView();
      myMainController.showGamePlayerView(gameNameToFolder.get(gameName), true);
    });

    card.getChildren().addAll(image, name, randomizeButton);
    card.setOnMouseClicked(e -> {
      myMainController.hideGameSelectorView();
      myMainController.showGamePlayerView(gameNameToFolder.get(gameName), false);
    });

    return card;
  }

  private Image getImage(String gameName) {
    GameConfigRecord config = gameConfigRecords.stream()
        .filter(g -> g.metadata().gameTitle().equals(gameName))
        .findFirst()
        .orElse(null);
    String folderName = gameNameToFolder.get(gameName);

    Image image;
    if (config != null && config.metadata().image() != null) {
      try {
        image = new Image(
            new FileInputStream(gamesFolderPath + folderName + "/" + config.metadata().image()));

      } catch (Exception e) {
        LoggingManager.LOGGER.warn(
            "Could not load image for " + gameName + ", using placeholder.");
        image = new Image(Objects.requireNonNull(
            getClass().getResourceAsStream("/assets/images/placeholder.png")));
      }
    } else {
      image = new Image(
          Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/placeholder.png")));
    }

    return image;
  }

  private List<GameConfigRecord> getAllGameConfigs() {
    List<String> folderNames = FileUtility.getFolderNamesInDirectory(gamesFolderPath);
    List<GameConfigRecord> gameConfigs = new ArrayList<>();
    for (String folderName : folderNames) {
      try {
        GameConfigRecord config = configParser.loadGameConfig(
            gamesFolderPath + folderName + "/gameConfig.json");
        gameConfigs.add(config);
        gameNameToFolder.put(config.metadata().gameTitle(), folderName);
      } catch (ConfigException e) {
        LoggingManager.LOGGER.warn("Could not load config file " + folderName, e);
        // this one is just a warning since if it don't have a valid game config we just don't load
      }
    }

    return gameConfigs;
  }

  private VBox createFileUploadSection() {
    // This method is adapted from file upload code by Will He in the Cell Society project.
    uploadButton = new Button("Upload");
    uploadButton.getStyleClass().add(smallButtonString);
    startButton = new Button("Start");
    startButton.getStyleClass().add(smallButtonString);
    startButton.setDisable(true);
    fileLabel = new Label("No file selected");

    uploadButton.setOnAction(e -> handleFileUpload(fileChooser, fileLabel, startButton));
    startButton.setOnAction(e -> {
      String filePath = fileLabel.getText();
      try {
        GameConfigRecord config = configParser.loadGameConfig(filePath);
        String gameName = config.metadata().gameTitle();
        myMainController.hideGameSelectorView();
        myMainController.showGamePlayerView(gameNameToFolder.get(gameName), false);
      } catch (Exception ex) {
        LoggingManager.LOGGER.error("Exception: {}", ex.getMessage());
        showErrorDialog("Exception", ex.getMessage()); // use languageController later
      }
    });

    VBox fileUploadSection = new VBox(10, uploadButton, fileLabel, startButton);
    fileUploadSection.setAlignment(Pos.CENTER);
    VBox.setMargin(fileUploadSection, new Insets(30, 0, 0, 0));
    return fileUploadSection;
  }

  private void handleFileUpload(FileChooser fileChooser, Label label, Button startButton) {
    // This method is adapted from file upload code by Will He in the Cell Society project.
    fileChooser.setTitle("Open File"); // use languageController later
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));

    File selectedFile = fileChooser.showOpenDialog(null);
    if (selectedFile != null) {
      String relativePath = getRelativePath(selectedFile, "data");
      if (relativePath != null) {
        fileLabel.setText(relativePath);
        startButton.setDisable(false);
      }
      else {
        fileLabel.setText("Invalid file"); // use languageController later
      }
    }
  }

  private String getRelativePath(File file, String baseFolder) {
    // This method is adapted from file upload code by Will He in the Cell Society project.
    File baseDir = new File(System.getProperty("user.dir"), baseFolder);
    String absolutePath = file.getAbsolutePath();
    String basePath = baseDir.getAbsolutePath();

    if (absolutePath.startsWith(basePath)) {
      return "data/" + absolutePath.substring(basePath.length() + 1).replace("\\", "/");
    }
    return null;
  }

  private void showErrorDialog(String title, String message) {
    // This method is adapted from file upload code by Will He in the Cell Society project.
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Resets file label and disables start button.
   */
  public void resetUploadSection() {
    fileLabel.setText("No file selected");
    startButton.setDisable(true);
  }
}
