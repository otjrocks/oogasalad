## GameState

### Overview

The GameState API is designed to manage the states of HUD elements and other graphical interfaces while the game is running. Serves as a way to store the side bar, info, question, pause buttons, score, and other hud items.  
**SOLID / Object Oriented Design**

- Single Responsibility Principle: Each method of this API handles a specific part of the HUD  
- Open-Closed Principle: New buttons and features can be added and extended without modifying the core interface  
- Liskov Substitution Principle: Buttons and boxes stored in the GameState data structure can be changed and replaced without breaking the API  
- Integration Segregation Principle: The API defines small, specific interfaces for various parts of the game (HUD management, saving/loading, rendering) to ensure that clients do not depend on unnecessary functionalities.  
- Dependency Inversion Principle: Behavior depends on abstract interfaces as opposed to concrete implementations, promoting flexible integration with various components

### Classes

GameState

* Stores all HUD elements and graphical interfaces.  
* Provides methods for retrieving, updating, and saving HUD states.  
* Handles all interactions related to game progress and player interactions.  
* Manages game statistics (score, lives, level, etc.).

### Details / Use Cases

Use Case: MAPGENERATION-1: Load Map Goal: Create and initialize the map and all of its entities using the predefined JSON values Steps:

1. Read JSON config data for subsequent map.  
2. Parse entities and layout defined in the JSON file.  
3. Create dynamic entities (PacMan, Ghosts, Powerups) according to specifications.  
4. Render the map based on the parsed data.  
5. Display the initial game state on the game window.

Use Case: MAPGENERATION-2: Level Loading Goal: Load various levels dynamically based on user progress or selection. Steps:

1. Read JSON or configuration file for level data.  
2. Parse level layout and entities.  
3. Initialize dynamic entities and static objects.  
4. Render the level layout.  
5. Display the new level on the game window.

Use Case: MAPGENERATION-3: Power-Up Handling Goal: Manage power-ups on the map and their effects on the game state. Steps:

1. Define power-up entities within the map data.  
2. Track when PacMan interacts with a power-up.  
3. Update the game state accordingly (e.g., ghosts become vulnerable).  
4. Display visual indicators of power-up effects.  
5. Reset the power-up status after a specific duration.

Use Case: MAPGENERATION-4: Pause and Save Game Goal: Allow users to pause the game and save progress. Steps:

1. Press the pause button to stop the game loop.  
2. Display pause menu options (Save, Resume, Quit).  
3. Save current game state to local storage or a file.  
4. Allow user to resume the game from the saved state.  
5. Display the paused or resumed state on the game window.

Use Case: MAPGENERATION-5: Edge Cases Handling Goal: Handle special cases in map generation and entity interactions. Steps:

1. Define wrap-around edges for the grid.  
2. Prevent entities from moving through walls.  
3. Allow PacMan to pass through tunnels to the other side.  
4. Verify interactions between PacMan, ghosts, power-ups, and dots.  
5. Apply special conditions when applicable.

Use Case: MAPGENERATION-6: Initialize Game Window Goal: Render the game window and display all game components properly. Steps:

1. Initialize the game window size and layout.  
2. Render the map and all entities.  
3. Display top bar with score, lives, and level information.  
4. Continuously update the display as the game progresses.

Use Case: MAPGENERATION-7: Loading Multiple Simulations Goal: Support running multiple game simulations simultaneously. Steps:

1. Allow user to select or create multiple simulations.  
2. Load each simulation from its configuration file.  
3. Display each simulation in separate game windows.  
4. Allow users to pause, save, and interact with each simulation independently.

Use Case: MAPGENERATION-8: Loading Game Info Goal: Load existing game data to resume from a previous state. Steps:

1. Read saved game data from storage.  
2. Parse all relevant game state information (score, level, lives, etc.).  
3. Render the game according to the saved state.  
4. Allow user to continue the game from where it was left off.

Use Case: GAMESTATE-1: Create GameState Object Goal: Create an object that stores all the current game state for easy access and modification. Steps:

1. Create a GameState object that stores:  
   * Player’s current score  
   * Number of lives remaining  
   * Current level information  
   * Position and status of all dynamic entities (PacMan, Ghosts, Powerups, Dots)  
   * Game timer (if applicable)  
2. Provide methods to update, retrieve, and reset game state values.  
3. Allow interaction with various parts of the program to read/write state information.

Use Case: GAMESTATE-2: Save GameState Goal: Save the current game state to local storage or a server for future access. Steps:

1. Trigger save mechanism from a menu or hotkey.  
2. Serialize GameState object into a JSON format or other storable structure.  
3. Write the serialized data to a file, local storage, or a database.  
4. Provide user feedback about successful saving.

Use Case: GAMESTATE-3: Load GameState Goal: Load a saved game state to resume from a previous session. Steps:

1. Read saved game data from a file, local storage, or a server.  
2. Deserialize the saved data into a GameState object.  
3. Restore all relevant game elements to their previous states.  
4. Allow the player to continue gameplay from the saved state.

Use Case: GAMESTATE-4: Handle Win and Loss Conditions Goal: Track and update win/loss conditions based on game rules. Steps:

1. Monitor game state during gameplay.  
2. Trigger win condition when all dots are eaten.  
3. Trigger loss condition when PacMan runs out of lives.  
4. Display appropriate end screens (Win/Loss).  
5. Optionally allow user to restart or save progress.

### Considerations

* Handling updates to HUD elements should not interfere with the main game loop.  
* Serialization and deserialization should be robust and efficient.  
* Ensure compatibility between versions of saved game states.  
* Provide accessibility options for visual and input controls.
