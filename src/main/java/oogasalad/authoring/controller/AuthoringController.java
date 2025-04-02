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
 *
 * Responsibilities include:
 * <ul>
 *   <li>Creating and registering new entity types</li>
 *   <li>Responding to entity selection and placement actions</li>
 *   <li>Syncing view components with model data</li>
 * </ul>
 *
 * @author Will He
 */
public class AuthoringController {

  private final AuthoringModel model;
  private final AuthoringView view;

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
    EntityType newType = new EntityType();
    newType.setType("NewEntity" + UUID.randomUUID().toString().substring(0, 4));
    newType.setModes(defaultModeMap());
    model.addEntityTemplate(newType);

    selectedType = newType;
    updateEntitySelector();
    view.getEntityEditorView().setEntityType(newType);
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
    model.findEntityTemplateByType(typeName).ifPresent(type -> {
      selectedType = type;
      view.getEntityEditorView().setEntityType(type);
    });
  }

  /**
   * Places a new instance of an entity on the canvas at the given coordinates.
   * If the given type name is valid, a new {@link EntityPlacement} is created,
   * added to the model, and rendered visually.
   * <p>
   * Triggered when a tile is dragged and dropped onto the canvas.
   * </p>
   *
   * @param typeName the name of the entity type being placed
   * @param x        the X-coordinate of the placement (in pixels)
   * @param y        the Y-coordinate of the placement (in pixels)
   */
  public void placeEntity(String typeName, double x, double y) {
    EntityPlacement placement = model.findEntityTemplateByType(typeName)
        .map(template -> model.createAndAddEntityPlacement(template, x, y))
        .orElse(null);

    if (placement != null) {
      view.getCanvasView().addEntityVisual(placement); // visually display on canvas
    }
  }

  /**
   * Refreshes the entity selector view grid to reflect the current
   * set of entity templates stored in the model.
   */
  public void updateEntitySelector() {
    view.getEntitySelectorView().updateEntities(model.getEntityTemplates());
  }

  /**
   * Reloads and re-renders all entity visuals on the canvas based on the
   * model's list of current {@link EntityPlacement}s.
   * <p>
   * This may be called after editing an entity’s visual properties.
   * </p>
   */
  public void updateCanvas() {
    view.getCanvasView().reloadFromPlacements(model.getEntityPlacements());
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
}
