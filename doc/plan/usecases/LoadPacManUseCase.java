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