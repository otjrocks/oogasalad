/**
 * Provides functionality for managing game save states during a session.
 * <p>
 * This package includes tools for:
 * <ul>
 *   <li>Creating and initializing new game sessions</li>
 *   <li>Saving and loading game progress using JSON</li>
 *   <li>Managing session state data such as current level, lives, score, and level order</li>
 * </ul>
 *
 * The primary classes include:
 * <ul>
 *   <li>{@link oogasalad.player.model.save.GameSessionManager} — coordinates session-level data and lifecycle</li>
 *   <li>{@link oogasalad.player.model.save.SaveManager} — handles the actual maintenance of save files</li>
 * </ul>
 *
 * Save data is serialized as JSON using Jackson and stored within a designated "saves" directory.
 *
 * @author Luke Fu
 * @author Owen Jennings
 */
package oogasalad.player.model.save;