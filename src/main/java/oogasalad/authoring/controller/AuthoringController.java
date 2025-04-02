package oogasalad.authoring.controller;

import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.view.AuthoringView;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.config.ModeConfig;

import java.util.*;

/**
 * Coordinates updates between the AuthoringModel and AuthoringView.
 */
public class AuthoringController {

  private final AuthoringModel model;
  private final AuthoringView view;

  private EntityType selectedType;

  public AuthoringController(AuthoringModel model, AuthoringView view) {
    this.model = model;
    this.view = view;
  }

  /**
   * Called when user clicks "+ Add Entity Type"
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
   * Called when a tile in the EntitySelectorView is clicked
   */
  public void selectEntityType(String typeName) {
    model.findEntityTemplateByType(typeName).ifPresent(type -> {
      selectedType = type;
      view.getEntityEditorView().setEntityType(type);
    });
  }

  /**
   * Called when a tile is dragged and dropped onto the canvas
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
   * Updates the EntitySelectorView grid
   */
  public void updateEntitySelector() {
    view.getEntitySelectorView().updateEntities(model.getEntityTemplates());
  }

  // Optional: if you want to update visual canvas after editing mode image
  public void updateCanvas() {
    view.getCanvasView().reloadFromPlacements(model.getEntityPlacements());
  }

  private Map<String, ModeConfig> defaultModeMap() {
    ModeConfig defaultMode = new ModeConfig();
    defaultMode.setImagePath("assets/images/pacman.png");
    defaultMode.setMovementSpeed(100);
    Map<String, ModeConfig> map = new HashMap<>();
    map.put("Default", defaultMode);
    return map;
  }
}
