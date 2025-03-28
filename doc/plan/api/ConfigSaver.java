/**
 * ConfigSaver
 *
 * Overview
 * This API is responsible for converting an in-memory representation of a game (`ConfigModel`) into a persistent format, typically a JSON file. It acts as the saving mechanism for both the Authoring Environment and Game Player to write out configurations for user-authored games. The design separates the act of serializing configuration data from the business logic of the oogasalad.engine or the UI.
 *
 * SOLID / Object-Oriented Design
 * S: Responsible solely for saving `ConfigModel` objects to a file.
 * O: Easily extended to support new file formats (e.g., XML, YAML) without modifying the interface.
 * L: Any implementation of `ConfigSaver` can be substituted if it adheres to the same interface.
 * I: Cleanly interfaces with only what’s necessary (i.e., `ConfigModel`, not game oogasalad.engine internals).
 * D: Depends only on the abstract `ConfigModel`, not on any implementation-specific internals.
 *
 * Extensible / How to Think About Tasks
 * The goal is to allow developers to easily persist full game configurations — titles, entities, settings, collision rules, etc. This API does not need to understand the content of the config — only how to serialize it safely. Developers should be able to swap the saver for different formats or storage mediums (e.g., cloud, database) with minimal friction.
 *
 * Classes
 *
 * ConfigSaver
 * void saveToFile(ConfigModel config, String filepath)
 *   - Saves a complete configuration to disk at the given file path
 *   - Throws IOException if the file cannot be written
 *
 * List<String> getSupportedFileExtensions()
 *   - Returns a list of file extensions this saver can handle (e.g., [".json"])
 *
 * Custom Exceptions
 * IOException (standard)
 *   - Thrown when the file write fails (e.g., permissions issue, disk full)
 *
 * Details / Use Cases
 *
 * Works closely with:
 *   - ConfigModel (core model of configuration)
 *   - File IO services
 *
 * Considerations
 * - Format must be compatible with what `ConfigLoader` can read
 * - Currently assumes synchronous file IO (can consider async later)
 * - Should support versioning in case the config structure changes in future
 * - Could later include schema validation or file name sanitization
 * - May want to restrict file types/extensions depending on platform (e.g., .json only)
 */