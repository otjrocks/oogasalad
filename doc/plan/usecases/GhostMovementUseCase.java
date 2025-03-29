/**
 * Use Case: GHOST-2: Based on the configuration, assign the correct movement strategy to the ghost
 * (algorithm + target position) / move the ghost
 */
class GhostMovementUseCase {
  public static void main(String[] args) {
    // Step 1: Read the starting mode
    ConfigParser configParser = new ConfigParser();
    String jsonFilePath = "path/to/character_config.json";

    ConfigModel configModel = configParser.parse(jsonFilePath);

    EntityFactory entityFactory = new EntityFactory();
    Ghost ghost = entityFactory.createEntity(GHOST);

    MovementStrategy ghostMovementStrategy = configModel.getGhostMovementStrategy();

    // Step 2: Load the movement algorithm based on the mode / set its target position based on the
    // target position calculation method
    ghost.computeNextPosition(ghostMovementStrategy);

    // step 3: Update the ghost movement based on the algorithm ensuring it follows game boundaries
    ghost.updatePosition();

  }
}


/**
 * Use Case: CHARACTER-1: Load PacMan Goal: Load Controllable(s) based on predefined control scheme,
 * appearance, location and other configuration values
 */
class DefineInteractionUseCase {

  public static void main(String[] args) {
    // Step 1: Read character configuration values from JSON

    ConfigParser configParser = new ConfigParser();
    String jsonFilePath = "path/to/character_config.json";

    ConfigModel configModel = configParser.parse(jsonFilePath);

    // Step 2: Initialize Entity based on JSON data
    EntityFactory entityFactory = new EntityFactory();
    List<EntityData> entityConfigs = configModel.getEntityConfigs();

    // Step 3: Add Entity to GameMap
    GameMap gameMap = new GameMap();
    for (EntityData entityData : entityConfigs) {
      try {
        Entity entity = entityFactory.createEntity(entityData);
        gameMap.addEntity(entity);
      } catch (EntityFactory.EntityCreationException e) {
        System.err.println("Error creating entity: " + e.getMessage());
      }
    }
  }
}