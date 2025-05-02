/**
 * <h1>Overview</h1>
 * This package contains enumerations used by the player module to define specific gameplay behaviors
 * and states. These enums enable centralized, extensible control of high-level game features such as cheats.
 *
 * <h2>CheatType</h2>
 * Defines a set of predefined cheat code actions that can be triggered by player input. Each cheat is
 * associated with a specific behavior, such as adding lives, changing levels, or modifying game speed.
 * The {@link oogasalad.player.model.enums.CheatType#execute} method allows each enum constant to perform
 * its corresponding action when the associated input is detected.
 *
 * <ul>
 *   <li><b>ADD_LIFE</b>: Adds an extra life to the player's total.</li>
 *   <li><b>PAUSE_GAME</b>: Temporarily halts the game loop.</li>
 *   <li><b>NEXT_LEVEL</b>: Immediately skips to the next level.</li>
 *   <li><b>RESET_GAME</b>: Resets the game to the first level.</li>
 *   <li><b>SPEED_UP</b>: Increases the current game speed by 10%.</li>
 * </ul>
 *
 * These actions depend on real-time player inputs processed by the {@link oogasalad.player.controller.GameInputManager}
 *
 * @author Will He
 */
package oogasalad.player.model.enums;