package com.gameconfig.rules;

import java.util.Optional;
import java.util.UUID;

/**
 * The {@code RuleSaveManager} interface provides methods for managing game configuration rules,
 * including saving, loading, updating, and deleting rules.
 *
 * @param <T> the type of rule being managed, which must extend BaseRule
 * @author Game Configuration Team
 */
public interface RuleSaveManager<T extends BaseRule> {

    /**
     * Saves a single rule to the configuration file.
     *
     * @param rule the rule to be saved
     * @return a unique identifier for the saved rule
     * @throws RuleSaveException if an error occurs during the rule saving process
     */
    String saveRule(T rule) throws RuleSaveException;

    /**
     * Retrieves a specific rule by its identifier.
     *
     * @param ruleId the unique identifier of the rule to retrieve
     * @return an Optional containing the rule if found, or an empty Optional if not found
     * @throws RuleLoadException if an error occurs while loading the rule
     */
    Optional<T> loadRule(String ruleId) throws RuleLoadException;

    /**
     * Updates an existing rule in the configuration file.
     *
     * @param ruleId the unique identifier of the rule to update
     * @param updatedRule the updated rule content
     * @return true if the rule was successfully updated, false otherwise
     * @throws RuleUpdateException if an error occurs during the rule update process
     */
    Boolean updateRule(String ruleId, T updatedRule) throws RuleUpdateException;

    /**
     * Removes a specific rule from the configuration file.
     *
     * @param ruleId the unique identifier of the rule to delete
     * @return true if the rule was successfully deleted, false otherwise
     * @throws RuleDeletionException if an error occurs during rule deletion
     */
    Boolean deleteRule(String ruleId) throws RuleDeletionException;

    // Custom Exception Classes
    class RuleSaveException extends Exception {
        public RuleSaveException(String message) {
            super(message);
        }

        public RuleSaveException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    class RuleLoadException extends Exception {
        public RuleLoadException(String message) {
            super(message);
        }

        public RuleLoadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    class RuleUpdateException extends Exception {
        public RuleUpdateException(String message) {
            super(message);
        }

        public RuleUpdateException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    class RuleDeletionException extends Exception {
        public RuleDeletionException(String message) {
            super(message);
        }

        public RuleDeletionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}

/**
 * Abstract base class representing a configurable game rule.
 */
public abstract class BaseRule {
    private String id;
    private String authorName;

    /**
     * Constructs a new BaseRule with an optional ID and author name.
     *
     * @param id the unique identifier for the rule
     * @param authorName the name of the rule's author
     */
    public BaseRule(String id, String authorName) {
        this.id = (id != null) ? id : UUID.randomUUID().toString();
        this.authorName = authorName;
    }

    /**
     * Gets the rule's unique identifier.
     *
     * @return the rule's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the rule's unique identifier.
     *
     * @param id the new ID for the rule
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of the rule's author.
     *
     * @return the author's name
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Sets the name of the rule's author.
     *
     * @param authorName the new author name
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * Serializes the rule to a string representation.
     *
     * @return a string representation of the rule
     */
    public abstract String serialize();
}
