/**
 * <h1>
 * ConfigSaver
 * </h1>
 * <p>
 * This API is responsible for converting an in-memory representation of a game
 * ({@code ConfigModel}) into a persistent format, implemented for a JSON file in our program. It
 * acts as the saving mechanism for both the Authoring Environment to write out configurations for
 * user-authored games. The design separates the act of serializing configuration data from the
 * logic of the engine or the UI.
 * <p>
 * Considerations:
 * <ul>
 *   <li>Format must be compatible with what {@code ConfigParser} can read</li>
 *   <li>Could later include schema validation or file name sanitization</li>
 *   <li>May want to restrict file types/extensions depending on platform (e.g., .json only)</li>
 * </ul>
 *
 * <h1>
 * ConfigParser
 * </h1>
 * <p>
 * The Configuration Parser API is designed to provide an extensible mechanism for reading and
 * interpreting configuration files that define various aspects of the generated PacMan game,
 * such as metadata, initial states, entities, and outcome strategies.
 * <p>
 * Details / Use Cases:
 * <ul>
 *   <li>Interacts directly with both the Authoring Environment and the Game Player for loading
 *       game state representations for editing or playing configuration files</li>
 *   <li>Parses configuration files that define maze structure, ghost AI strategies, and initial
 *       locations of dynamic entities like power-ups. The parser will parse all the configuration for a game.</li>
 *   <li>Validates configuration data before game initialization</li>
 *   <li>Provides a flexible framework for adding new configuration types</li>
 * </ul>
 * <p>
 * Considerations:
 * <ul>
 *   <li>We want a flexible parsing structure since future customization needs are unknown</li>
 *   <li>Generic parsing and validating methods allow for easy extensibility</li>
 *   <li>Assumes users provide well-structured JSON files, though more robust error handling could
 *       be added</li>
 * </ul>
 *
 * @author Will He
 * @version 1.0
 * @since 1.0
 */
package oogasalad.engine.config.api;
