/**
 * <p>
 * Contains the core model components for the game, including game state representation, entity logic,
 * and various strategy subpackages. This package also includes subpackages for enums, exceptions, and
 * save management related to the game.
 * </p>
 *
 * <p><b>Core Classes:</b></p>
 * <ul>
 *   <li>{@code Entity} - Represents a dynamic object in the game world with associated properties and behavior.</li>
 *   <li>{@code GameMap} - Represents the game's spatial structure, including entities and static elements.</li>
 *   <li>{@code GameState} - Holds the current state of the game including score, lives, and entity positions.</li>
 * </ul>
 *
 * <p><b>Subpackages:</b></p>
 * <ul>
 *   <li>{@code enums} - Contains the enumerations used throughout the game.</li>
 *   <li>{@code exceptions} - Contains custom exception classes used across the game, such as strategy and save-related exceptions.</li>
 *   <li>{@code save} - Handles saving and loading of game data, including session management and save configuration.</li>
 *   <li>{@code collision} - Strategies for handling entity interactions such as consuming, teleporting, or scoring.</li>
 *   <li>{@code control} - Strategies and systems for controlling entity movement, including pathfinding and targeting logic.</li>
 *   <li>{@code gameoutcome} - Defines conditions for ending the game, based on lives, time, score, or entity presence.</li>
 *   <li>{@code modechangeevent} - Contains event triggers that cause entities to change behavior modes after time-based events.</li>
 *   <li>{@code spawnevent} - Strategies that determine when and how new entities are spawned during gameplay.</li>
 * </ul>
 */
package oogasalad.player.model;