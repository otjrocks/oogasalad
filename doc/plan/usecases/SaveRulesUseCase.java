package com.gameconfig.editor;

import com.gameconfig.rules.BaseRule;
import com.gameconfig.rules.RuleSaveManager;

/**
 * Represents a level configuration rule specific to game level settings.
 */
class LevelRule extends BaseRule {
    private String levelName;
    private String description;
    private int difficulty;

    /**
     * Constructs a new LevelRule with specific level details.
     *
     * @param authorName the name of the level's author
     * @param levelName the name of the game level
     * @param description a description of the level
     * @param difficulty the difficulty rating of the level
     */
    public LevelRule(String authorName, String levelName, String description, int difficulty) {
        super(null, authorName);
        this.levelName = levelName;
        this.description = description;
        this.difficulty = difficulty;
    }

    @Override
    public String serialize() {
        return String.format("LevelRule[id=%s,author=%s,name=%s,description=%s,difficulty=%d]", 
            getId(), getAuthorName(), levelName, description, difficulty);
    }

    // Getters and setters
    public String getLevelName() { return levelName; }
    public void setLevelName(String levelName) { this.levelName = levelName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }
}

/**
 * Simulates a level editor saving a new level configuration.
 * Demonstrates the use case of saving rules from the editor.
 */
public class LevelEditorRuleSaver {
    private RuleSaveManager<LevelRule> ruleSaveManager;

    /**
     * Constructs a LevelEditorRuleSaver with a specific RuleSaveManager.
     *
     * @param ruleSaveManager the manager responsible for saving rules
     */
    public LevelEditorRuleSaver(RuleSaveManager<LevelRule> ruleSaveManager) {
        this.ruleSaveManager = ruleSaveManager;
    }

    /**
     * Saves a new level configuration created in the editor.
     *
     * @param authorName the name of the level's author
     * @param levelName the name of the game level
     * @param description a description of the level
     * @param difficulty the difficulty rating of the level
     * @return the unique identifier of the saved level rule
     * @throws RuleSaveManager.RuleSaveException if the rule cannot be saved
     */
    public String saveLevelConfiguration(
        String authorName, 
        String levelName, 
        String description, 
        int difficulty
    ) throws RuleSaveManager.RuleSaveException {
        // Create a new level rule
        LevelRule levelRule = new LevelRule(authorName, levelName, description, difficulty);

        // Validate the rule (simple example of potential validation)
        validateLevelRule(levelRule);

        // Save the rule and return its ID
        return ruleSaveManager.saveRule(levelRule);
    }

    /**
     * Performs basic validation on the level rule.
     *
     * @param levelRule the rule to validate
     * @throws RuleSaveManager.RuleSaveException if the rule is invalid
     */
    private void validateLevelRule(LevelRule levelRule) throws RuleSaveManager.RuleSaveException {
        if (levelRule.getLevelName() == null || levelRule.getLevelName().trim().isEmpty()) {
            throw new RuleSaveManager.RuleSaveException("Level name cannot be empty");
        }

        if (levelRule.getDifficulty() < 1 || levelRule.getDifficulty() > 10) {
            throw new RuleSaveManager.RuleSaveException("Difficulty must be between 1 and 10");
        }
    }

    /**
     * Example usage of the level editor rule saving process.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // In a real scenario, this would be a concrete implementation of RuleSaveManager
            RuleSaveManager<LevelRule> mockRuleSaveManager = new MockRuleSaveManager<>();
            
            LevelEditorRuleSaver editorRuleSaver = new LevelEditorRuleSaver(mockRuleSaveManager);
            
            // Simulate saving a level configuration from the editor
            String levelRuleId = editorRuleSaver.saveLevelConfiguration(
                "GameDesigner1", 
                "Mystic Forest", 
                "A challenging forest level with multiple paths", 
                7
            );
            
            System.out.println("Level configuration saved with ID: " + levelRuleId);
        } catch (RuleSaveManager.RuleSaveException e) {
            System.err.println("Failed to save level configuration: " + e.getMessage());
        }
    }

    /**
     * Mock implementation of RuleSaveManager for demonstration purposes.
     */
    private static class MockRuleSaveManager<T extends BaseRule> implements RuleSaveManager<T> {
        @Override
        public String saveRule(T rule) throws RuleSaveException {
            // Simulate saving by returning the rule's ID
            return rule.getId();
        }

        // Other methods would have similar mock implementations
        @Override
        public Optional<T> loadRule(String ruleId) throws RuleLoadException {
            return Optional.empty();
        }

        @Override
        public Boolean updateRule(String ruleId, T updatedRule) throws RuleUpdateException {
            return false;
        }

        @Override
        public Boolean deleteRule(String ruleId) throws RuleDeletionException {
            return false;
        }
    }
}
