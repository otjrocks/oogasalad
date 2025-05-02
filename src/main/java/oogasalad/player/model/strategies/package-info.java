/**
 * Contains core strategy interfaces and implementations for dynamic, configurable game behavior.
 *
 * <p>This package serves as the top-level container for all game strategies, enabling modular and
 * extensible logic for how entities interact, move, spawn, change state, and determine game outcomes.</p>
 *
 * <p>The Strategy design pattern is used throughout this package to decouple game mechanics from
 * hardcoded logic, allowing behavior to be customized through configuration files or runtime decisions.</p>
 *
 * <p>Included subpackages:</p>
 * <ul>
 *   <li><b>{@code collision}</b> – Defines entity interaction behavior on collision events (e.g., teleport, stop, reset).</li>
 *   <li><b>{@code control}</b> – Defines how entities are controlled (keyboard input, AI target following, etc.).</li>
 *   <li><b>{@code gameoutcome}</b> – Determines when a game ends and whether the result is a win or loss.</li>
 *   <li><b>{@code modechangeevent}</b> – Controls when and how entities transition between different modes or types.</li>
 *   <li><b>{@code spawnevent}</b> – Handles conditional logic for spawning and despawning entities during gameplay.</li>
 * </ul>
 *
 * <p>These strategies are used throughout the engine to support flexible gameplay design, driven
 * by data rather than hardcoded rules.</p>
 */
package oogasalad.player.model.strategies;