# Initial Project Backlog

## Features We Want to Demonstrate in Sprints 1 & 2

### Common Features:

* Language Handling (Loading messages from current language file)
* Theme Handling (Load current theme)
* Splash Screen View
* FileHandler interface (JSON concrete implementation)
* FileWriter interface (JSON concrete implementation)
* LoggingHandler (handles all logging of info and warnings for the program)

### Authoring Environment (Will, Angela, Ishan):

1. Entity Selector
    1. DynamicEntity Selector: Selector for PacMan, Ghost, Powerup, other controllable characters.
    2. StaticEntity Selector: Selector for Wall, Brick, Background, Ladder, Dot
2. Place entities on the map
    1. Allow multiple layers for a single “square” on the map. Allow user to reorder elements (i.e. put background static element in back, then a dot element, then controllable entity on top) allow user to move elements order
3. Save into config
4. Adjust metadata
    1. title, author, etc
5. Adjust what entities look like
6. Game Settings
    1. Change meta settings (speed, powerups, etc)
    2. Set game rules (game interactions)
    3. Set interaction types (ex: pacman controlled by mouse clicked)
    4. Set win condition
    5. Determine order of advancement

### Game Player (Jessica, Austin, Luke, Owen, Troy):

1. DynamicEntity: Character Object
    1. Moving PacMan
        1. Edge cases, walls, wrap around for grid
    2. Interactions between entities
        1. Pacman and ghost
        2. pacman and powerup
        3. pacman and dots
2. DynamicEntity: Ghosts Object
    1. Ghost algorithms
3. GameStatistics: Top Info/Game State Bar (score)
    1. track high scores
    2. win or loss conditions
    3. Tracking lives
    4. GameState (object that stores all the state from the game) to allow for easy saving to local storage)
4. Map generation / Game Window
    1. initial state
    2. Level loading
5. Power-ups
6. Pause the game during play and save progress to save later / load multiple simulations at the same time \+ other misc stuff
    1. loading game info

## Use Cases

Confine the use cases to the one you are doing

### Template

**Use Case:**   
\- Goal:   
\- Steps:

1.

### Authoring Environment (Will, Angela, Ishan):

**Use Case: AUTH-1: Create Dynamic Entity**  
\- Goal: Allow the user to create a new dynamic entity (PacMan, Ghosts, Powerups) that can be customized with properties.  
\- Steps:

1. User selects “Create Dynamic Entity” option
2. System displays configuration form with attributes
3. User inputs the entity name, initial position, and movement pattern
4. System validates and creates the dynamic entity
5. Entity is added to the game's entity library

**Use Case: AUTH-2: Configure Dynamic Entity Behavior**  
\- Goal: Customize movement, collision, and interaction rules for dynamic entities  
\- Steps:

1. User selects a specific dynamic entity
2. System open behavior configuration panel
3. User selects the movement algorithm, collision detection rule, and interaction triggers
4. System saves and validates the behavior configuration

**Use Case: AUTH-3: Dynamic Entity Layering and Rendering**

- Goal: Manage rendering order and visual layers for dynamic entities.
- Steps:
1. Open layer management interface
2. View current entity rendering order
3. Drag and reorder entities across layers
4. System updates rendering priority dynamically
5. Save layer configuration

**Use Case: AUTH-4: Dynamic Entity State Management**

- Goal: Track and modify dynamic entity states during gameplay.
- Steps:
1. System tracks entity states (active/inactive, power-ups, etc.)
2. User can manually trigger state changes
3. System updates the entity behavior based on the state
4. Interactions and rendering are updated dynamically

**Use Case: AUTH-5: Bulk Dynamic Entity Operations**

- Goal: Perform operations on multiple dynamic entities simultaneously.
- Steps:
1. User selects multiple entities
2. System enables bulk edit options
3. User can apply common properties
4. Changes are applied to all the selected entities
5. System logs bulk modification

**Use Case: AUTH-6: Create Static Entry Library**

- Goal: Build a comprehensive library of static game elements.
- Steps:
1. User opens static entity creation panel
2. Select from predefined types (Wall, Brick, Background, Ladder, Dot)
3. Customize the properties and textures
4. Save the entity to project library

**Use Case: AUTH-7: Update entity appearance**

- Goal: Allow users to customize what the entity looks like
- Steps:
1. User selects an existing entity from the map or entity library
2. System opens visual customization panel
3. User uploads new image
4. user can adjust image (rotate, etc)
5. Preview changes
6. Confirm changes
7. Update appearance and save in level config

**Use Case: AUTH-8: Change meta settings**

- Goal: Allow users to configure game-level settings like speed or power-up duration
- Steps:
1. User opens game settings panel
2. System displays editable fields for speed, power-up, etc
3. user changes values
4. system validates values
5. System applies changes and updates preview

**Use Case: AUTH-9: Define Interactions**

- Goal: Allow users to customize how entities interact
- Steps:
1. User opens game rules editor
2. System shows list of possible entity interactions
3. User selects a pair of entities
4. User chooses effect
5. System updates level config


**Use Case: AUTH-10: Set Play method**

- Goal: Allow users to customize how the user controls a specific entity
- Steps:
1. Users selects a controllable entity
2. System opens a control method panel
3. user selects type (keyboard, mouse, etc)
4. user configures details (ex: keybinds)
5. system stores in config

**Use Case: AUTH-11: Define Win Condition**

- Goal: Allow users to customize how the game is won
- Steps:
1. User navigates to win condition section of game settings
2. system has list of possible win conditions
3. user selects and adds needed parameters
4. system validates input and stores

**Use Case: AUTH-12: Set Level Advancement order**

- Goal: Allow users to customize order of levels
- Steps:
1. User has a view of all the levels in project
2. They can make an order of levels
3. System updates

**Use Case: AUTH-13: Load level from config**

- Goal: Allow user to upload a premade level for editing
- Steps
1. User selects a config file from directory
2. System parses config file and displays it in the editor

**Use Case: AUTH-14: Save level to config file**

- Goal: Allow user to save their edited level to a config file to play later
- Steps
1. User hits the save new button
2. System takes all relevant game info from editor and compiles it into a configuration file
3. System saves file to directory and sends user back to home splash screen

**Use Case: AUTH-15: Modify existing config file**

- Goal: Allow the user to modify an existing config file without having to save a new one
- Steps
1. User his the save button
2. System compiles relevant info into config fil
3. System deletes old config file and saves new config file under same name

**Use Case: AUTH-16: Modify Author name**

- Goal: allow user to modify the author name for the config file
- Steps
1. User selects the author has the field they want to edit
2. User types in new name for author
3. System saves this new name into config file

**Use Case: AUTH-17: Modify Title of level**

- Goal: allow user to modify the title name for the level in to config file
- Steps
1. User selects the level title has the field they want to edit
2. User types in new name for level
3. System saves this new name into config file

**Use Case: AUTH-18: Modify level description**

- Goal: allow user to modify the level description in the config file
- Steps
1. User selects the description has the field they want to edit
2. User types in new description for level
3. System saves this new description into config file

### Game Player (Jessica, Austin, Luke, Owen, Troy)

**Use Case: CHARACTER-1: Load PacMan**

- Goal: Load Controllable(s) based on predefined control scheme, appearance, location and other configuration values
- Steps:
1. Read character configuration values from JSON
2. Initialize Controllable object based on JSON data
3. Add Controllable to collection of dynamic entities
4. Generate graphic at starting position

**Use Case: CHARACTER-MOVT-1: WASD/Arrow Keys**

- Goal: Allow users to control PacMan by indicating movement direction via the W, A, S and D keys or the arrow keys on the keyboard
- Steps:
1. Define movement method within a grid space to update PacMan’s position
2. Write the logic differentiating between these key presses utilizing the movement method
3. Apply movement strategy to the Controllable within the game space
4. Dynamically update PacMan’s position graphically

**Use Case: CHARACTER-MOVT-2: Mouse Click**

- Goal: Allow users to control PacMan by indicating which tile you’d like to move to with the mouse.
- Steps:
1. Define movement method that will accept a location that will use a search algorithm to find the shortest path from PacMan to the requested grid space
2. Assign action handlers for all grid spaces that will indicate PacMan needs to move
3. Apply movement strategy to the Controllable within the game space
4. Dynamically update PacMan’s position graphically

**Use Case: CHARACTER-INT-1: WALL**

- Goal: Ensure PacMan is not able to move through walls
- Steps:
1. Differentiate between wall and other dynamic objects potentially by using an enum
2. When moving PacMan, check to see what object he would be moving onto before performing the actual update
3. If the destination is a wall, cancel movement or change destination to PacMan’s current location if update must be handled

**Use Case: CHARACTER-INT-2: GHOST**

- Goal: Ensure PacMan properly interacts with ghosts, either losing a life or eating them
- Steps:
1. Differentiate between ghosts and other dynamic objects potentially by using an enum
2. If PacMan is currently intersecting with a ghost, check to see if PacMan is “powered up”
3. If he is, the ghost’s eyes should float back to some “ghost cell” before spawning back in
4. If he is not, a death animation should play and PacMan will lose a life.
5. If PacMan is out of lives, an end splash screen should show

**Use Case: CHARACTER-INT-3: POWER UP**

- Goal: Ensure PacMan properly interacts with power ups by updating ghost behavior and appearance
- Steps:
1. Differentiate between ghosts and other dynamic objects potentially by using an enum
2. If PacMan is currently intersecting with a power up, queue dynamic changes.
3. Change ghost appearance to be distinctly different so the player knows the ghosts can be eaten
4. Change ghost movement behavior to run away from PacMan (or follow a predefined “run” behavior)
5. Start timer for how long PacMan should remain “powered up”

**Use Case: CHARACTER-INT-4: DOT**

- Goal: Ensure PacMan properly interacts with dots, increasing his score or ending the level
- Steps:
1. Differentiate between ghosts and other dynamic objects potentially by using an enum
2. If PacMan is currently intersecting with a dot, remove it and increase the player’s score
3. If there are no dots remaining on the map, show a level splash screen and load the next stage

**Use Case: CHARACTER-INT-5: EDGE OF SCREEN TUNNEL**

- Goal: Ensure PacMan is able to move to the other side of the stage using the edge of screen tunnel
- Steps:
1. Determine and label which locations on the map should trigger the tunnel effect
2. Ensure any defined tunnel spaces are linked to another space on the map
3. If PacMan is current intersecting with a tunnel, update PacMan’s position to be the other side of that tunnel

**Use Case: GHOST-1: Based on configuration, create ghost**

- Goal: Create and initialize the ghost class (whichever one it ends up being) using predefined configuration values
- Steps:
1. Load ghost configuration data from a JSON
2. Parse properties / (make an api to do it and then use it to call values)
3. Instantiate a ghost with those properties
4. Add the ghost object to the entities (or whatever it ends up being called)
5. Render the ghost on the game board at its initial position

**Use Case: GHOST-2: Based on the configuration, assign the correct movement strategy to the ghost (algorithm \+ target position) / move the ghost**

- Goal: Assign a movement algorithm / mode / target position
- Steps:
1. Read the starting mode
2. Load the movement algorithm based on the mode / set its target position based on the target position calculation method
3. Update the ghost movement based on the algorithm ensuring it follows game boundaries

**Use Case: GHOST-3: Based on the configuration for modes a ghost has, change the mode if the condition is applied, update for the correct movement strategy assigned.**

- Goal: Dynamically update a ghost’s mode (if its a mode it has) based on the condition
- Steps:
1. Monitor timer / action / nearby entities
2. Check all conditions the ghost has to change its mode (so these are the conditions and the mode assigned to them a ghost has.
3. If condition is met, update the ghost’s mode
4. Reset any mode specific timers / counters
5. Retrieve the movement strategy associated with the new mode
6. Update the ghost’s movement strategy / target position logic so it is using the new movement strategy in the game loop

**Use Case: GHOST-MOVT-1: UCS**

- Goal: Move the ghost using UCS to find lowest cost path (say for this example as defined by the yellow dots) to the target
- Steps:
1. Define movement cost for each tile (tiles with the yellow stuff have less cost then empty tiles)
2. Use UCS to search for the path with minimum cost from target position to its current position
3. Store the resulting path
4. Move ghost one step along the computed path
5. Recompute path (either based on say a counter trigger or if the target calculated is triggered)

**Use Case: GHOST-MOVT-2: DFS**

- Goal: Move the ghost using DFS
- Steps:
1. Construct graph of possible valid moves from the current position
2. Use DFS to explore paths until the target node is found
3. Store the resulting path
4. Move ghost one step along the computed path
5. Recompute path (either based on say a counter trigger or if the target calculated is triggered)

**Use Case: GHOST-MOVT-3: A\* Search**

- Goal: Move the ghost using an A\* algorithm
- Steps:
1. Maybe based on a property, define the ghost function and heuristic
2. Perform A\* search from current position to target
3. Once path is found, reconstruct it and update ghost’s direction
4. Move ghost one step along the computed path
5. Recompute path (either based on say a counter trigger or if the target calculated is triggered)

**Use Case: GAMESTATISTICS-SCORE-1: Maintain the game score**

- Goal: Maintain the score of the game and allow for easy adjustments to the score
- Steps:
1. Create a ScoreManager class which stores and updates the current score
2. The game score should be easily updated

**Use Case: GAMESTATISTICS-SCORE-2: Track high scores**

- Goal: Track the current high scores of the game
- Steps:
1. Within the ScoreManager check for any new high scores when game is over or when a player’s score is incremented.
2. Record the top 5-10 scores in a properties file, local storage, or a web server.
3. When a new high score is achieved, allow player to input their initials or name to display on the leaderboard
4. The list of high scores should consist of a name/initials and the score value

**Use Case: GAMESTATISTICS-SCORE-3: Dynamically Updating Score**

- Goal: Display the current score and high score in the Top Info/State Bar View
- Steps:
1. Using the score controller class, consistently update the top info view of the game with the players current score.
2. Ensure that high score leaderboard view is updated when a new leader is added to the storage/web server

**Use Case: GAMESTATISTICS-SCORE-4: Track Score Multiplier**

- Goal: Implement a scoring system that incorporates a score multiplier which can be invoked at various points in the program
- Steps:
1. Modify the existing ScoreManager to include a score multiplier field and getter/setter methods.
2. Allow the user to update or increment the score multiplier during specific events such as consecutive ghost capture (multiply), killing all ghosts (multiply), eating power up/down (multiply or divide), etc.
3. Create a way to reset the score multiplier when the game ends, when a life is lost, or after a predetermined time limit (i.e. some multipliers may only last a certain number of seconds)

**Use Case: GAMESTATISTICS-LIVES-1: Maintain Player Lives**

- Goal: Maintain and update the lives of the player
- Steps:
1. Create a LifeManager to store and update the player(s) lives
2. When a player dies, remove one life from their count.

**Use Case: GAMESTATISTICS-GAMESTATE-1: Create GameState object**

- Goal: Create an object that stores all the current game state. This object can be used by various strategies and classes to customize the actions and interactions in the game
- Steps:
1. Create a GameState object that stores a LifeManager, ScoreManager, and any other current game state for the current player.
2. Score other pertinent information such as the current level information and state, player locations, etc.

**Use Case: GAMESTATISTICS-GAMESTATE-2: Save and load GameState**

- Goal: Allow the program to save and load the game state to local storage, database, or web server. This is to allow for pausing and saving of progress in the game allowing the user to return later from a save menu option or after quitting the program
- Steps:
1. Create methods that allow game state to be read or wrote to a file or data base
2. Ensure that all the proper game statistics are stored and read after reload of a program.

**Use Case: GAMESTATISTICS-GAME-END-1: Handle Win and Loss Conditions**

- Goal: Handle the winning and losing conditions of the game
- Steps:
1. Create a GameOutcomeStrategy which is used to determine when the game ends.
2. In the game loop, consistently use the strategy to determine if the game has ended
3. Example Game Outcome Strategies could be:
4. A player loses if they run out of lives
5. A player wins if they eat all the dots/power ups on the board

**Use Case: MAPGENERATION-1: Load Map**

- Goal: Create and initialize the map and all of its entities using the predefined JSON values
- Steps:
1. Read JSON config data for subsequent map.
2. Parse entities and layout defined in the JSON file.
3. Create entities (PacMan, Ghosts, Powerups, walls) according to specifications.
4. Render the map based on the parsed data.
5. Display the initial game state on the game window.

**Use Case: MAPGENERATION-2: Level Loading**

- Goal: Load various levels dynamically based on user progress or selection.
- Steps:
1. Read JSON or configuration file for level data.
2. Parse level layout and entities.
3. Initialize dynamic entities and static objects.
4. Render the level layout.
5. Display the new level on the game window

**Use Case: MAPGENERATION-3: Power-Up Handling**

- Goal: Manage power-ups on the map and their effects on the game state.
- Steps:
1. Define power-up entities within the map data.
2. Track when PacMan interacts with a power-up.
3. Update the game state accordingly (e.g., ghosts become vulnerable).
4. Display visual indicators of power-up effects.
5. Reset the power-up status after a specific duration.

**Use Case: MAPGENERATION-4: Save Game**

- Goal: Allow users to pause game and save progress.
- Steps:
1. User selects pause button to stop the game loop
2. Display options to save, continue, quit
3. Save current game state to local storage or a file.
4. Allow user to resume the game from the saved state.
5. Display the paused or resumed state on the game window.

**Use Case: MAPGENERATION-5: Handle Edge Cases**

- Goal: Handle special cases in map generation and entity interactions.
- Steps:
1. Define wrap-around edges for the grid.
2. Prevent entities from moving through walls.
3. Allow PacMan to pass through tunnels to the other side.
4. Verify interactions between PacMan, ghosts, power-ups, and dots.
5. Apply special conditions when applicable.

**Use Case: MAPGENERATION-6: Loading Game Info**

- Goal: Load existing game data to resume from a previous state.
- Steps:
1. Read saved game data from storage.
2. Parse all relevant game state information (score, level, lives, etc.).
3. Render the game according to the saved state.
4. Allow user to continue the game from where it was left off.

**Use Case: POWERUP-1: Based on configuration, create powerup**

- Goal: Create and initialize the powerup class using predefined configuration values
- Steps:
1. Load powerup configuration data from JSON
2. Parse properties (make api to do it and use it to call values)
3. Instantiate a powerup with those properties
4. Add the powerup object to the entities
5. Render the powerup on the game board at specified position in JSON

**Use Case: POWERUP-2: Power Pellet: Eat ghost**

- Goal: Enable Pacman to eat ghosts
- Steps:
1. Make Pacman “powered up”
2. Change Pacman interaction with ghost, allowing it to now eat the ghost
3. Have timer that counts down after interaction with powerup
4. Each ghost eaten results in extra 400 points

**Use Case: CONTROL-PANEL-1: Create Control Panel**

- Goal: Create a control panel to allow users to start, pause, reset, save game progress
- Steps:
1. Create control panel on the screen with the following buttons:
    1. Start
    2. Pause
    3. Reset
    4. Save game state
    5. Return to home screen

**Use Case: CONTROL-PANEL-2: Start Game**

- Goal: Allow user to start Pacman gameplay
- Steps:
1. On pressing start game button, starts the game loop
2. Enables user control of controllables

**Use Case: CONTROL-PANEL-3: Pause Game**

- Goal: Allow user to pause Pacman gameplay
- Steps:
1. On pressing pause game button, stops the game loop
2. Disables user control of controllables
3. Display current game score

**Use Case: CONTROL-PANEL-4: Reset Game**

- Goal: Allow user to reset game to initial state
- Steps:
1. On pressing reset game button, stop game loop
2. Returns game to initial state specified by config file

**Use Case: CONTROL-PANEL-5: Save Game State/Progress**

- Goal: Allow user to save game state
- Steps:
1. On pressing save button, stop game loop
2. Have save game dialogue box pop up
3. In dialogue box, can save progress into existing save file or new save file (config file)
4. Be able to name these save files

**Use Case: CONTROL-PANEL-6: Return to Home Screen**

- Goal: Allow user to return to home screen, where they can see levels
- Steps:
1. On pressing return home button, stop game loop
2. Return user to home screen

