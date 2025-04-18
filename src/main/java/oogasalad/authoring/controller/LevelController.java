package oogasalad.authoring.controller;

import java.util.List;
import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.authoring.view.AuthoringView;

/**
 * Handles creation, selection, and organization of levels.
 * Interacts with both the AuthoringModel and AuthoringView to manage level logic.
 * Should be used by AuthoringController to delegate level-related tasks.
 *
 * @author Will He
 */
public class LevelController {

  private final AuthoringModel model;
  private final AuthoringView view;
  private final AuthoringController mainController;

  /**
   * Constructs a LevelController to manage levels within the authoring environment.
   *
   * @param model           the underlying model
   * @param view            the view to update
   * @param mainController  reference to the main controller for canvas/entity syncing
   */
  public LevelController(AuthoringModel model, AuthoringView view, AuthoringController mainController) {
    this.model = model;
    this.view = view;
    this.mainController = mainController;
  }

  /**
   * Adds a new empty level with a default name and switches to it.
   */
  public void addNewLevel() {
    int nextIndex = model.getLevels().size() + 1;
    String levelName = "Level " + nextIndex;
    String fileName = "level" + nextIndex + "_map.json";

    LevelDraft newLevel = new LevelDraft(levelName, fileName);
    model.addLevel(newLevel);

    updateLevelDropdown();
    switchToLevel(model.getLevels().size() - 1);
  }

  /**
   * Switches the model and view to display the specified level.
   *
   * @param index the index of the level to switch to
   */
  public void switchToLevel(int index) {
    model.setCurrentLevelIndex(index);
    view.getLevelSettingsView().refresh(); // or similar
    mainController.updateCanvas();
    mainController.updateEntitySelector(); // if needed
    view.getLevelSelectorView().highlightLevel(index); // optional
  }

  /**
   * Updates the level dropdown/list in the view to reflect current levels.
   */
  public void updateLevelDropdown() {
    List<LevelDraft> levels = model.getLevels();
    view.getLevelSelectorView().updateLevels(levels);
  }

  /**
   * Default level 1
   */
  public void initDefaultLevelIfEmpty() {
    if (model.getLevels().isEmpty()) {
      LevelDraft defaultLevel = new LevelDraft("Level 1", "level1_map.json");
      defaultLevel.setWidth(20);
      defaultLevel.setHeight(15);
      model.addLevel(defaultLevel);
    }
  }

  /**
   * Get current level
   * @return current LevelDraft
   */
  public LevelDraft getCurrentLevel() {
    return model.getCurrentLevel();
  }

  /**
   * Update size of canvas for this level
   * @param width Width of this level
   * @param height Height of this level
   */
  public void updateCanvasSize(int width, int height) {
    mainController.getCanvasView().resizeGrid(width, height);
  }
}
