package oogasalad.authoring.controller;

import java.io.File;

import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.view.AuthoringView;
import oogasalad.authoring.view.EntityPlacementView;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import java.util.*;
import oogasalad.engine.model.controlConfig.ControlConfig;
import oogasalad.engine.model.controlConfig.KeyboardControlConfig;
import oogasalad.engine.records.newconfig.ImageConfig;
import oogasalad.engine.config.ModeConfig;
import oogasalad.engine.records.newconfig.model.ControlType;
import oogasalad.engine.records.newconfig.model.ControlTypeConfig;
import oogasalad.engine.records.newconfig.model.EntityProperties;

/**
 * Coordinates updates between the {@link AuthoringModel} and {@link AuthoringView}. This controller
 * responds to user actions in the authoring interface, managing entity templates and placements on
 * the canvas. Responsibilities include:
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

  private static final String DEFAULT_MODE = "Default";

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
   * Creates a new {@link EntityType} with a default mode and registers it in the model. Updates the
   * entity selector and opens the entity editor for the newly created type.
   * <p>
   * Triggered when the user clicks the "+ Add Entity Type" button.
   * </p>
   */
  public void createNewEntityType() {

    String newTypeName = "NewEntity" + UUID.randomUUID().toString().substring(0, 4);

    // TODO: Update to be NoneControlConfig
    EntityType newType = new EntityType(newTypeName, new KeyboardControlConfig(), defaultModeMap(),
        null);
    // TODO: update this to include all required fields such as control type and effect type instead of providing null.
    model.addEntityType(newType);
    updateEntitySelector();
    selectEntityType(newTypeName);
  }

  /**
   * Selects an existing {@link EntityType} by its name and updates the entity editor with its
   * details.
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
   * Places a new instance of an entity on the canvas at the given coordinates. If the given type
   * name is valid, a new {@link EntityPlacement} is created, added to the current level, and
   * rendered visually.
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
   * Moves an existing entity placement to a new position on the canvas. Updates both the model and
   * visual representation of the entity.
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
   * Refreshes the entity selector view grid to reflect the current set of entity types stored in
   * the model.
   */
  public void updateEntitySelector() {
    view.getEntitySelectorView().updateEntities(new ArrayList<>(model.getEntityTypes()));
  }


  /**
   * Reloads and re-renders all entity visuals on the canvas based on the current level’s list of
   * {@link EntityPlacement}s.
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


  private Map<String, ModeConfig> defaultModeMap() {
    // Default image file
    File imageFile = new File("src/main/resources/assets/images/pacman.png");
    String imagePath = imageFile.toURI().toString();

    ImageConfig imageConfig = new ImageConfig(
        imagePath,
        14,
        14,
        List.of(0, 1, 2, 3), // Default animation frames
        1.0
    );

    return Map.of(DEFAULT_MODE, createDefaultMode(imageConfig));
  }

  private static ModeConfig createDefaultMode(ImageConfig imageConfig) {
    // TODO: Switch to NoneControlConfig
    ControlConfig defaultControlConfig = new KeyboardControlConfig();

    EntityProperties entityProperties = new EntityProperties(
        DEFAULT_MODE,
        defaultControlConfig,
        100.0,
        List.of() // No blocks
    );

    return new ModeConfig(DEFAULT_MODE, entityProperties, imageConfig);
  }


  /**
   * Get Level Controller
   *
   * @return level controller
   */
  public LevelController getLevelController() {
    return levelController;
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
   * Gets the view for components to access
   *
   * @return the AuthoringView instance
   */
  public AuthoringView getView() {
    return view;
  }

  /**
   * Selects an entity placement on the canvas. This is called when a user clicks on an entity in
   * the canvas.
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

}