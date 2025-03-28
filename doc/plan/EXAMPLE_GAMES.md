## Example Game 1: Classic Pac-Man (Basic Game)

**Overview:**
- The player controls Pac-Man using the keyboard.
- Ghosts use different AI movement strategies.
- Power pellets grant a temporary speed boost.
- Pac-Man loses a life on collision with a ghost.
- The game ends when all pellets are eaten or Pac-Man loses all lives.

**Features:**
- Uses `KeyboardMovementStrategy` for Pac-Man
- Uses various `AIMovementStrategy` subclasses for ghosts
- Defines collision rules:
    - `"Pacman,Ghost"` → `LoseLife`
    - `"Pacman,Pellet"` → `AddScore`
- Uses `SpeedBoostEffect` on power-up collision
- Uses `AllPelletsCollectedStrategy` as the win condition

---

## Example Game 2: Mouse-Escape Maze (Complex Variation)

**Overview:**
- The player controls a mouse character that follows the cursor (`FollowMouseMovementStrategy`).
- The maze contains patrolling guards (ghost-like entities) with randomized or cyclical movement.
- When a mouse collides with a cheese item, it teleports to a random location (`TeleportEffect`).
- The player must reach a goal tile while avoiding guards.
- The game ends when the player reaches the goal or gets caught.

**Features:**
- Uses `FollowMouseMovementStrategy` for the player
- Guards use `PatrolPathMovementStrategy` or `RandomMovementStrategy`
- CollisionStrategy:
    - `"Mouse,Cheese"` → `TriggerEffectOnA` → `TeleportEffect`
- Goal tile defined as a special static entity:
    - `"Mouse,Goal"` → `WinGame`
- Uses `EntityReachesGoalStrategy` for the win condition  