package oogasalad.authoring.controller;

import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.view.AuthoringView;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.config.ModeConfig;

import java.util.*;

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
    ModeConfig defaultMode = new ModeConfig();
    defaultMode.setImagePath("file:/C:/Users/willi/OneDrive/Documents/College/CS308/oogasalad_team01/src/main/resources/assets/images/pacman.png");
    defaultMode.setMovementSpeed(100);


    String newTypeName = "NewEntity" + UUID.randomUUID().toString().substring(0, 4);
    EntityType newType = new EntityType(newTypeName, "", "", defaultModeMap(), null, null);
    // TODO: update this to include all required fields such as control type and effect type instead of providing null.
    newType.modes().put("Default", defaultMode);

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
      view.getEntityEditorView().setVisible(true);
      view.getEntitySelectorView().highlightEntityTile(typeName); // 🔥 New!
    }, () -> {
      selectedType = null;
      view.getEntityEditorView().setEntityType(null);
      view.getEntityEditorView().setVisible(false);
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
  }


  // Private helper to provide default values for a new entity's mode config
  private Map<String, ModeConfig> defaultModeMap() {
    ModeConfig defaultMode = new ModeConfig();
    defaultMode.setImagePath("assets/images/pacman.png");
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
}
