/**
 * Contains the main view classes used to construct and display the game's visual interface to the player.
 *
 * <p>This package defines JavaFX-based views that render game content, manage user interaction,
 * and integrate with the game state and controller layers. It supports dynamic layout, entity rendering,
 * heads-up display, and UI transitions for game screens.</p>
 *
 * <p>Major view classes:</p>
 * <ul>
 *   <li>{@link oogasalad.player.view.GameSelectorView}
 *   – Displays a UI for selecting and loading games or uploading game files.</li>
 *   <li>{@link oogasalad.player.view.GamePlayerView}
 *   – Manages a playable game session, including loading levels and restoring saves.</li>
 *   <li>{@link oogasalad.player.view.GameScreenView}
 *   – Wraps the game board and HUD into a single interactive screen.</li>
 *   <li>{@link oogasalad.player.view.GameView}
 *   – Displays the actual game canvas and background, and handles end-game messages.</li>
 *   <li>{@link oogasalad.player.view.GameMapView}
 *   – A canvas-based renderer for game entities and background.</li>
 *   <li>{@link oogasalad.player.view.EntityView}
 *   – Draws individual game entities using sprite-based graphics.</li>
 * </ul>
 *
 * <p>Subpackages:</p>
 * <ul>
 *   <li>{@link oogasalad.player.view.components} – Contains reusable JavaFX UI components, such as HUD elements.</li>
 * </ul>
 *
 * <p>These classes coordinate closely with the controller and model layers to render the current game state and
 * respond to player actions in real time.</p>
 */
package oogasalad.player.view;
