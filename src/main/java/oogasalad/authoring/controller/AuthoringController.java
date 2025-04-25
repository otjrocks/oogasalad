package oogasalad.authoring.controller;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.authoring.view.mainView.AuthoringView;
import oogasalad.authoring.view.EntityPlacementView;
import oogasalad.authoring.view.canvas.CanvasView;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.config.JsonConfigParser;
import oogasalad.engine.exceptions.ConfigException;
import oogasalad.engine.records.config.ConfigModelRecord;
import oogasalad.engine.records.config.ImageConfigRecord;
import oogasalad.engine.records.config.ModeConfigRecord;
import oogasalad.engine.records.config.model.EntityPropertiesRecord;
import oogasalad.engine.records.config.model.ParsedLevelRecord;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;
import oogasalad.engine.records.config.model.controlConfig.NoneControlConfigRecord;
import oogasalad.engine.records.model.EntityTypeRecord;

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


  private EntityTypeRecord selectedType;
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
   * Creates a new {@link EntityTypeRecord} with a default mode and registers it in the model.
   * Updates the entity selector and opens the entity editor for the newly created type.
   * <p>
   * Triggered when the user clicks the "+ Add Entity Type" button.
   * </p>
   */
  public void createNewEntityType() {

    String newTypeName = "NewEntity" + UUID.randomUUID().toString().substring(0, 4);

    EntityTypeRecord newType = new EntityTypeRecord(newTypeName,
        defaultModeMap(),
        new ArrayList<>()); // Update this to use no hardcoded speed value.
    model.addEntityType(newType);
    updateEntitySelector();
    selectEntityType(newTypeName);
  }

  /**
   * Selects an existing {@link EntityTypeRecord} by its name and updates the entity editor with its
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
    model.findEntityType(typeName).ifPresent(template -> {
      EntityPlacement placement = model.getCurrentLevel()
          .createAndAddEntityPlacement(template, x, y);
      view.getCanvasView().placeEntity(placement);
    });
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

    // Re-select the placement by object identity (not equals)
    if (selectedPlacement != null) {
      for (EntityPlacement p : placements) {
        if (p == selectedPlacement) {
          selectEntityPlacement(p);
          return;
        }
      }
    }

    // Otherwise, clear selection
    selectedPlacement = null;
    view.getEntityPlacementView().setVisible(false);
  }


  private Map<String, ModeConfigRecord> defaultModeMap() {
    try {
      File fallbackFile = new File(
          Objects.requireNonNull(getClass().getResource("/assets/images/pacman.png")).toURI());

      ImageConfigRecord imageConfig = new ImageConfigRecord(
          fallbackFile.getAbsolutePath(),
          28,
          28,
          6,
          1.0
      );

      return Map.of(DEFAULT_MODE, createDefaultMode(imageConfig));
    } catch (URISyntaxException e) {
      throw new RuntimeException("Failed to resolve internal default image", e);
    }
  }

  private static ModeConfigRecord createDefaultMode(ImageConfigRecord imageConfig) {
    ControlConfigInterface defaultControlConfig = new NoneControlConfigRecord();

    EntityPropertiesRecord entityProperties = new EntityPropertiesRecord(
        DEFAULT_MODE,
        List.of() // No blocks
    );

    return new ModeConfigRecord(DEFAULT_MODE, entityProperties, defaultControlConfig, imageConfig,
        1.0);
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

  /**
   * Get canvas view
   *
   * @return canvas view object
   */
  public CanvasView getCanvasView() {
    return view.getCanvasView();
  }

  /**
   * Deletes an entity type and all its placements from the model and updates the UI.
   *
   * @param typeName the name of the entity type to delete
   */
  public void deleteEntityType(String typeName) {
    // Remove visual representations using the actual EntityPlacements tied to ImageViews
    view.getCanvasView().removeAllPlacementsOfType(typeName);

    // Delete from model
    if (model.deleteEntityType(typeName)) {
      clearSelectionIfDeleted(typeName);
      updateEntitySelector();
    }
  }


  /**
   * Gets all entity placements of a specific type in the current level.
   */
  private List<EntityPlacement> getPlacementsOfType(String typeName) {
    return model.getCurrentLevel().getEntityPlacements().stream()
        .filter(p -> typeName.equals(p.getTypeString()))
        .toList();
  }


  /**
   * Clears selection references if they match the deleted entity type.
   */
  private void clearSelectionIfDeleted(String typeName) {
    // Clear entity type editor if needed
    if (selectedType != null && selectedType.type().equals(typeName)) {
      selectedType = null;
      view.getEntityEditorView().setEntityType(null);
      view.getEntityEditorView().getRoot().setVisible(false);
    }

    // Clear entity placement view if needed
    if (selectedPlacement != null && selectedPlacement.getTypeString().equals(typeName)) {
      selectedPlacement = null;
      view.getEntityPlacementView().setVisible(false);
    }
  }

  /**
   * Loads an existing project in from a gameConfig file
   *
   * @param gameConfigFile file to read
   * @throws ConfigException Config parsing error
   */
  public void loadProject(File gameConfigFile) throws ConfigException {

    String projectFolder = gameConfigFile.getParent();
    JsonConfigParser parser = new JsonConfigParser();
    ConfigModelRecord config = parser.loadFromFile(gameConfigFile.getAbsolutePath());

    List<EntityTypeRecord> fixedEntityTypes = fixEntityTypesWithAbsolutePaths(projectFolder,
        config.entityTypes());

    ConfigModelRecord fixedConfig = new ConfigModelRecord(
        config.metadata(),
        config.settings(),
        fixedEntityTypes,
        config.levels(),
        config.collisionRules(),
        config.winCondition(),
        config.loseCondition(),
        config.currentLevelIndex()
    );

    populateModelFromConfig(fixedConfig, projectFolder);
  }


  private void populateModelFromConfig(ConfigModelRecord config, String projectFolder) {
    model.clearAll();

    // Set metadata
    model.setGameTitle(config.metadata().title());
    model.setAuthor(config.metadata().author());
    model.setGameDescription(config.metadata().description());

    // Set default game settings
    model.setDefaultSettings(config.settings());

    // Set entity types
    Map<String, EntityTypeRecord> entityMap = new LinkedHashMap<>();
    for (EntityTypeRecord entity : config.entityTypes()) {
      entityMap.put(entity.type(), entity);
    }
    model.setEntityTypeMap(entityMap);

    List<LevelDraft> levelDrafts = new ArrayList<>();
    int levelIndex = 1;
    for (ParsedLevelRecord parsed : config.levels()) {
      LevelDraft draft = new LevelDraft("Level " + levelIndex, "level" + levelIndex + ".json");

      String backgroundImagePath = parsed.mapInfo().backgroundImagePath();
      if (backgroundImagePath != null) {
        File resolvedFile = new File(backgroundImagePath).isAbsolute()
            ? new File(backgroundImagePath)
            : new File(projectFolder, backgroundImagePath);
        draft.setBackgroundImage(resolvedFile);

      }
      draft.setEntityPlacements(parsed.placements());
      draft.setWidth(parsed.mapInfo().width());
      draft.setHeight(parsed.mapInfo().height());
      draft.getSpawnEvents().addAll(parsed.spawnEvents());
      draft.getModeChangeEvents().addAll(parsed.modeChangeEvents());

      levelDrafts.add(draft);
      levelIndex++;
    }
    model.setLevels(levelDrafts);
    model.setCurrentLevelIndex(config.currentLevelIndex());
    for (LevelDraft draft : model.getLevels()) {
      draft.refreshEntityTypes(model.getEntityTypeMap());
    }

    // Set collision rules
    model.setCollisionRules(config.collisionRules());
    view.refreshUI();
  }


  private List<EntityTypeRecord> fixEntityTypesWithAbsolutePaths(String basePath,
      Collection<EntityTypeRecord> originalEntityTypes) {
    List<EntityTypeRecord> result = new ArrayList<>();

    for (EntityTypeRecord entity : originalEntityTypes) {
      Map<String, ModeConfigRecord> fixedModes = new HashMap<>();

      for (Map.Entry<String, ModeConfigRecord> entry : entity.modes().entrySet()) {
        ModeConfigRecord fixedMode = getModeConfigRecord(basePath, entry);

        fixedModes.put(entry.getKey(), fixedMode);
      }

      EntityTypeRecord fixedEntity = new EntityTypeRecord(entity.type(), fixedModes,
          entity.blocks());
      result.add(fixedEntity);
    }

    return result;
  }

  private static ModeConfigRecord getModeConfigRecord(String basePath,
      Entry<String, ModeConfigRecord> entry) {
    ModeConfigRecord mode = entry.getValue();
    String path = mode.image().imagePath();

    File resolvedFile = new File(path).isAbsolute()
        ? new File(path)
        : new File(basePath, path);

    ImageConfigRecord fixedImage = new ImageConfigRecord(
        resolvedFile.getAbsolutePath(),
        mode.image().tileWidth(),
        mode.image().tileHeight(),
        mode.image().tilesToCycle(),
        mode.image().animationSpeed()
    );

    return new ModeConfigRecord(
        mode.name(),
        mode.entityProperties(),
        mode.controlConfig(),
        fixedImage,
        mode.movementSpeed()
    );
  }


}