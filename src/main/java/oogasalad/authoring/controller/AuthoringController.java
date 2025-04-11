package oogasalad.authoring.controller;

import java.io.File;

import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.view.AuthoringView;
import oogasalad.authoring.view.EntityPlacementView;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.config.ModeConfig;
import oogasalad.engine.model.GameSettings;

import java.util.*;
import oogasalad.engine.records.newconfig.model.Settings;

/**
 * Coordinates updates between the {@link AuthoringModel} and {@link AuthoringView}.
 * This controller responds to user actions in the authoring interface,
 * managing entity templates and placements on the canvas.
 * Responsibilities include:
 * <ul>
 *   <li>Creating and registering new entity types</li>
 *   <li>Responding to entity selection and placement actions</li>
 *   <li>Syncing view components with model data</li>
 * </ul>
 *
 * @author Will He, Angela Predolac
 */
public class AuthoringController {

  private final AuthoringModel model;
  private final AuthoringView view;
  private final LevelController levelController;


  private EntityType selectedType;
  private EntityPlacement selectedPlacement;

  /**
   * Constructs an AuthoringController with the given model and view.
   *
   * @param model the backend model managing game entities and placements
   * @param view  the visual UI components shown to the user
   */
  public AuthoringController(AuthoringModel model, AuthoringView view) {
    this.model = model;
    this.view = view;
    this.levelController = new LevelController(model, view, this);
  }

  /**
   * Creates a new {@link EntityType} with a default mode and registers it
   * in the model. Updates the entity selector and opens the entity editor
   * for the newly created type.
   * <p>
   * Triggered when the user clicks the "+ Add Entity Type" button.
   * </p>
   */
  public void createNewEntityType() {


    String newTypeName = "NewEntity" + UUID.randomUUID().toString().substring(0, 4);
    EntityType newType = new EntityType(newTypeName, "", "", defaultModeMap(), null, null);
    // TODO: update this to include all required fields such as control type and effect type instead of providing null.
    model.addEntityType(newType);
    updateEntitySelector();
    selectEntityType(newTypeName);
  }

  /**
   * Selects an existing {@link EntityType} by its name and updates the
   * entity editor with its details.
   * <p>
   * Triggered when a tile is clicked in the EntitySelectorView.
   * </p>
   *
   * @param typeName the string name of the selected entity type
   */
  public void selectEntityType(String typeName) {
    model.findEntityType(typeName).ifPresentOrElse(type -> {
      selectedType = type;
      view.getEntityEditorView().setEntityType(type);
      view.getEntityEditorView().getRoot().setVisible(true);
      view.getEntitySelectorView().highlightEntityTile(typeName);

      selectedPlacement = null;
      view.getEntityPlacementView().setVisible(false);
    }, () -> {
      selectedType = null;
      view.getEntityEditorView().setEntityType(null);
      view.getEntityEditorView().getRoot().setVisible(false);
    });
  }

  /**
   * Places a new instance of an entity on the canvas at the given coordinates.
   * If the given type name is valid, a new {@link EntityPlacement} is created,
   * added to the current level, and rendered visually.
   *
   * @param typeName the name of the entity type being placed
   * @param x        the X-coordinate of the placement (in pixels)
   * @param y        the Y-coordinate of the placement (in pixels)
   */
  public void placeEntity(String typeName, double x, double y) {
    model.findEntityType(typeName)
        .map(template -> model.getCurrentLevel().createAndAddEntityPlacement(template, x, y))
        .ifPresent(placement -> view.getCanvasView().addEntityVisual(placement));

  }

  /**
   * Moves an existing entity placement to a new position on the canvas.
   * Updates both the model and visual representation of the entity.
   *
   * @param placement the EntityPlacement being moved
   * @param x         the new X-coordinate (in pixels)
   * @param y         the new Y-coordinate (in pixels)
   */
  public void moveEntity(EntityPlacement placement, double x, double y) {
    if (placement == null) {
      return;
    }
    placement.moveTo(x, y);
    if (selectedPlacement == placement) {
      view.getEntityPlacementView().updatePositionDisplay();
    }
  }

  /**
   * Refreshes the entity selector view grid to reflect the current
   * set of entity types stored in the model.
   */
  public void updateEntitySelector() {
    view.getEntitySelectorView().updateEntities(new ArrayList<>(model.getEntityTypes()));
  }


  /**
   * Reloads and re-renders all entity visuals on the canvas based on the
   * current level’s list of {@link EntityPlacement}s.
   */
  public void updateCanvas() {
    List<EntityPlacement> placements = model.getCurrentLevel().getEntityPlacements();
    view.getCanvasView().reloadFromPlacements(placements);

    // Re-select the currently selected placement after canvas update
    if (selectedPlacement != null && placements.contains(selectedPlacement)) {
      selectEntityPlacement(selectedPlacement);
    } else {
      // If the selected placement was removed, clear the selection
      selectedPlacement = null;
      view.getEntityPlacementView().setVisible(false);
    }
  }


  // Private helper to provide default values for a new entity's mode config
  private Map<String, ModeConfig> defaultModeMap() {
    ModeConfig defaultMode = new ModeConfig();
    defaultMode.setModeName("Default");

    File imageFile = new File("src/main/resources/assets/images/pacman.png");
    defaultMode.setImagePath(imageFile.toURI().toString());

    defaultMode.setMovementSpeed(100);
    Map<String, ModeConfig> map = new HashMap<>();
    map.put("Default", defaultMode);
    return map;
  }

  /**
   * Get Level Controller
   * @return level controller
   */
  public LevelController getLevelController() {
    return levelController;
  }

  /**
   * Updates the game settings in the model when changes are made in the view
   *
   * @param updatedSettings the updated GameSettings object
   */
  public void updateGameSettings(Settings updatedSettings) {
    model.setDefaultSettings(updatedSettings);
  }

  /**
   * Gets the model for view components to access
   *
   * @return the AuthoringModel instance
   */
  public AuthoringModel getModel() {
    return model;
  }

  /**
   * Helper method to refresh the game settings view
   * Call this whenever the model's settings change from another source
   */
  public void refreshGameSettingsView() {
    if (view.getGameSettingsView() != null) {
      view.getGameSettingsView().updateFromModel();
    }
  }

  /**
   * Gets the view for components to access
   *
   * @return the AuthoringView instance
   */
  public AuthoringView getView() {
    return view;
  }

  /**
   * Selects an entity placement on the canvas.
   * This is called when a user clicks on an entity in the canvas.
   *
   * @param placement the entity placement that was selected, or null to deselect
   */
  public void selectEntityPlacement(EntityPlacement placement) {
    EntityPlacementView placementView = view.getEntityPlacementView();
    selectedPlacement = placement;

    if (placement != null) {
      // Show the placement view and hide the type editor
      placementView.setEntityPlacement(placement);
      view.getEntityEditorView().getRoot().setVisible(false);
      placementView.setVisible(true);

    } else {
      placementView.setVisible(false);
    }
  }

  /**
   * Updates an entity placement's properties and refreshes views.
   * Called when the properties of an entity are edited in the EntityPlacementView.
   *
   * @param placement the updated entity placement
   */
  public void updateEntityPlacement(EntityPlacement placement) {
    if (placement == null) return;
    updateCanvas();
  }

  /**
   * Updates the mode of an entity placement.
   * Called when the mode is changed in the EntityPlacementView.
   *
   * @param placement the entity placement to update
   * @param newMode the new mode to set
   */
  public void updateEntityPlacementMode(EntityPlacement placement, String newMode) {
    if (placement == null || newMode == null) return;

    // Verify that the mode exists for this entity type
    if (placement.getType().modes().containsKey(newMode)) {
      placement.setMode(newMode);
      updateCanvas();
    }
  }

  /**
   * Removes an entity placement from the current level.
   * Called when the delete button is clicked in the EntityPlacementView.
   *
   * @param placement the entity placement to remove
   */
  public void removeEntityPlacement(EntityPlacement placement) {
    if (placement == null) return;

    model.getCurrentLevel().removeEntityPlacement(placement);

    if (placement == selectedPlacement) {
      selectedPlacement = null;
    }

    updateCanvas();
  }

  /**
   * Returns the currently selected entity placement, if any.
   *
   * @return the currently selected placement, or null if none is selected
   */
  public EntityPlacement getSelectedPlacement() {
    return selectedPlacement;
  }

  /**
   * Returns the currently selected entity type, if any.
   *
   * @return the currently selected entity type, or null if none is selected
   */
  public EntityType getSelectedType() {
    return selectedType;
  }
}
