/**
 * Use Case: AUTH-9 - Define Interactions
 * Goal: Allow users to customize how entities interact
 */
class DefineInteractionUseCase {
  public static void main(String[] args) {
    // Step 1: Create model with sample templates
    AuthoringModel model = new AuthoringModel();
    model.addEntityTemplate(new EntityTemplate("Pacman", "pacman.png", "Keyboard", null));
    model.addEntityTemplate(new EntityTemplate("Ghost", "ghost.png", "ChasePlayer", null));

    // Step 2: Open game rules editor → get possible interactions
    Set<String> entityTypes = model.getEntityTypes();
    System.out.println("Possible interactions: " + entityTypes);

    // Step 3: User selects a pair: Pacman and Ghost
    String typeA = "Pacman";
    String typeB = "Ghost";

    // Step 4: User chooses effect: LoseLife
    String strategy = "LoseLife";
    model.getCollisionRuleEditor().setRule(typeA, typeB, strategy);

    // Step 5: Save updated config
    ConfigSaver saver = new JsonConfigSaver();
    try {
      saver.saveToFile(model.toConfigModel(), "data/configs/interaction_pacman_ghost.json");
    } catch (IOException e) {
      System.err.println("Failed to save updated config: " + e.getMessage());
    }
  }
}