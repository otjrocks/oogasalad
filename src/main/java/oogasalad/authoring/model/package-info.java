/**
 * Contains the core data models and state management logic for the authoring environment.
 * <p>
 * This package defines structures used to represent user-created game configurations,
 * including entities, levels, settings, and rules. These models serve as the single source of truth
 * for the current authoring session and are synchronized with the UI through the controllers.
 * <p>
 * Responsibilities include:
 * <ul>
 *   <li>Storing and managing entity types, placements, and level metadata</li>
 *   <li>Tracking unsaved changes and current authoring context</li>
 *   <li>Providing access to game configuration data for serialization</li>
 *   <li>Supporting undo/redo or state updates driven by user interactions</li>
 * </ul>
 *
 * The model layer follows principles of separation of concerns and is designed to be UI-agnostic and testable.
 */
package oogasalad.authoring.model;
