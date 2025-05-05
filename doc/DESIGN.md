# DESIGN Document for Oogasalad

## Team 1 (PacOne)

### Ishan Madan (im121), Owen Jennings (otj4), Jessica Chen (jc939), Angela Predolac (ap721), William He (wwh15), Luke Fu (lmf59), Austin Huang (ash110), Troy Ludwig (tdl21)

## Team Roles and Responsibilities

- Ishan Madan

- Owen Jennings
    - I worked on various parts of the game engine and authoring environment. Initially, I worked on the game map engine API, ensuring that it encapsulated all the information we needed for a game map across multiple types of games.  One of my major responsibilities was getting the collision strategies interface to work correctly across both the authoring and game player environments. Towards the end of the project, I refactored our code to ensure that all of the view components were properly using language strings and external CSS styling. I also implemented the background image on both the authoring and game player environments. Finally, I did a lot of refactoring to ensure that control strategies and collisions accounted for different modes, so that a collision strategy and control strategy applied to a specific mode rather than the global entity. This enabled us to do some complex game features like switching how the AI ghosts moved while the game was running and allowing the frightened ghost to behave differently when it collided with Pac-Man compared to a normal ghost.
- Jessica Chen
    - I worked mainly on implementing the code for making intelligent AI for enemies or non-playable characters focusing on the control, target, and path finding strategies. I also worked on designing the refactor for the configParser and ensuring this new structure worked with the current implementation of configParser (so the config parser refactoring minus level implementation). I also mainly just outside of that worked on completing tasks for refactoring/picking up smaller tasks to clean and polish the project. Additionally I did a lot with planning and designing what is needed to make the hierarchy of the project and what strategies are needed in the config. For example, I worked on adding tests, refactoring languages and styling, etc.

- Angela Predolac

- William He
    - I worked mainly on implementing the authoring environment and making it consistent with the config data structure.I also built configParser and configBuilder, implemented cheat keys, and helped with thinking through how we wanted to implement control strategies. My main contribution was importing a file on the authoring environment.

- Luke Fu
    - I worked on generating the config structure and level handling, which includes level progression, saving, parsing, and

- Austin Huang
    - I worked on creating strategy interfaces and implementations, specifically the collision and gameOutcome strategies, and integrating some of this logic into the game. I implemented the logic for detecting win and loss conditions from config and displaying the game outcome label and buttons for level progression. I created the power pellet entity and strategy for mode changes. I built the game selector screen, including the game cards to select games made by the team and the upload file button. I also worked on small miscellaneous tasks like styling and language management.

- Troy Ludwig
    - I worked on implementing entity sprites/animations and developing mode change events for shifting entity behaviors mid-game. I developed several collision strategies including all of those that shift the mode of an entity type or individual entity, and the addTailStrategy for my snake implementation. I also developed the initial input manager for controllable entities and refactored to avoid having JavaFX aspects within model packages. I took on miscellaneous tasks like removing hard coded values.

## Design Goals

Make it Easy to Add New Games

- The system should support rapid creation and deployment of new games with minimal setup. Designers and developers should be able to define the basic structure and behavior of a game without needing to modify core engine logic in order to test it. The games that can be created should be a wide variety to show its flexibility while still following some nature of a pacman styled game.

Support Iterative Testing and Development (Go end to end)

- Users should be able to create games within the authoring environment, easily save them, upload them to the game player to test, and reupload in the authoring environment for easy testing


Make it Easy to Add New Strategies

- The engine should be extensible with different gameplay behaviors, such as:
    - Win/Loss Conditions: Easily swap out or introduce new rules that define how the player wins or loses.
    - Control Strategies: Add different methods for controlling entities (e.g., keyboard, different AI strats with various levels of “smartness”).
    - Collision Strategies: Customize how objects interact when they collide, or events that should occur on object collision

Comprehensive and Understandable User Interface

- The authoring environment should be intuitive and informative, enabling an easy environment to create entities and their interactions.

#### How were Specific Features Made Easy to Add

- New Game
    - create a new game in the authoring environment, as long as it uses the existing choices in the authoring environment, can drag the game folder into games in order for it to show up in the home screen or just upload it in order to play a new game.
- New Themes/Languages
    - create the respected file in the resources folder with the correct name and information similar to other themes/languages
- New strategy choices (i.e. control strategies, win/loss conditions, collision strategies)
    - Create a record that extends the appropriate interface for the new strategy you are making with the parameters you want it to have.
    - Implement the strategy with the expected interface and place it in the appropriate package. The name should be the same as that of the record but with Strategy instead of Record (or else how the factory defines it.
    - Ensure that the factory constructing this strategy parses this constructor setup.
    - The authoring environment automatically detects and presents the new strategy using reflection, including any parameters the strategy requires, so no change to UI on authoring environment. Additionally the game engine as long as the naming and constructors are set up as required by the factory will run the new strategy.

## High-level Design

#### Core Classes and Abstractions, their Responsibilities and Collaborators

The core design of the project was built around modularity, flexibility, and data-driven architecture. The overall goal was to deconstruct a game like Pacman into a set of reusable, interchangeable components that allow for dynamic behavior and game customization without hardcoding logic.

Thus we split it into the following parts

- the level map, where entities are intialized at the start of the game, in what mode, and for game player how the game map is structured
- game objects all being entities with different modes, thus entities of the same type can have different behaviors based on the mode they are in; thus all things in the game need to have an overarching entity type, but also for each entity specifics of where that entity is, and what mode it is in as well as during game information
- strategies, strategies affect all the core logic of the game from main events being driven by collision strategy and then for other game context events spawn and mode change strategies and also how entities move through control strategies
- finally, the config parser/builder is how everything is connected between the authoring and player section.

The central abstraction is the Entity. Everything visible or interactive in the game world—player, ghost, wall, pellet—is represented as an Entity. Each entity:

- Has a type (EntityType) which defines which modes it has and its behavior within those modes
- Is positioned on a grid (EntityPlacement) with game time information
- Is configured with one or more strategy behaviors such as control, and are context in other strategy behaviors such as collision and game outcome strategies

Entities are defined in configuration files, and these definitions are parsed at runtime by the configuration system to dynamically construct the game.

The game map works with the player level controller as well as the config parser to parse out and create these entity placements at runtime. Then based on these different control/outcome strategies, the game map is responsible for providing information on what entities are currently on the map and for pathfinding strategies where.

The main driving factor of the player focuses on strategies. Essentially, pacman’s control strategies and collision strategies need to be dynamically updated at runtime as pacman has different interactions with ghost based on if he is powered up or not. Thus the main classes in player focus on creating this interfaces

- control strategy interface
    - path finding strategy interface
    - target calculation strategy interface
- game outcome strategies
- collision strategies
- spawn event strategies
- mode change event strategies

These strategies enable runtime flexibility. For instance, when Pacman becomes powered up, the CollisionStrategy changes dynamically to allow him to "eat" ghosts rather than be defeated by them simply by changing the mode needed to trigger the event.

The configuration system bridges the gap between authoring environment and game engine as well as allowing you to create and reuse created games. For the most part the config parser parses out the different information required to create the player implementations of levels, entities, and strategies.

Lastly, authoring environment mirrors the player environment, however while player implements all of these strategies, entity types/entities, and levels in how they behave during playing the game, for each of these authoring environment has a view in order to chose between the strategy options and define these levels and entity types.

Thus, its strategies are in form views where based on the strategy and specific implementation of the strategy chosen you can configure parameters such that the Config builder can build it. Similarly for the Entity types and modes it lets you define their attributes and place it on the level map which the authoring environment stores and displays in a way that can be editable by the person using the environment but easily convertible to a configuration structure the level controller and game map in the player use to setup the initial game state.

## Assumptions or Simplifications

- Simplifications from the normal pacman game
    - Pacman for target ahead based on the way they encode math and coordinate vectors, has a "bit error" where you target above while moving up the entity targets the position up and right, this was not implemented
    - Pacman's scatter timings do not work exactly like the timings of the original games
- For the challenging extension "Play Modes" we assumed "Allow users to play a challenge mode, through any number of randomly selected levels" that keeping the same number of levels to win the game but randomizing the levels you play to fufill this number of levels to count.

## Changes from the Original Plan

* Configuration File Organization: changed from a single file structure in to multiple files in order for the game levels to be easier to see
    * Level Map Parsing: changed all entities to be array string implemented
    * Parameter Management: went from regex matching parameters to parameters depending on names within the json, this allows for better validation of types

* Sprites: instead of a single image migrated to a directional based sprite sheet

* In addition to collision events, time/score based spawn and mode change events
    * In order to support pacman events are not just based on condition but also some time based on time (i.e. scatter and leaving the home)

* Switch from dynamic game rules to help system for basic extension, just worked out better as we added different game rules

## How to Add New Features

#### Features Designed to be Easy to Add

* (this was done above in design goals)

#### Features Not Yet Done

For the most part, the main feature not implemented is running multiple game instances, the other remaining features are features that are done partially but were not fully fleshed out and polished features

* Running multiple game instances
    * make the game view a factory so you can run multiple instances for each one
* Allow for keys to be configurable for say keyboard strategy and cheat codes
    * cheat keys
        * for cheat keys in addition to having a string to enable the cheat also have it have a key
        * then change the input manager to instead of checking for the hard coded key to check it from the config
    * keyboard / jump
        * likewise for this, instead of being like up is  the up key … have keyboard have parameters (just add it in the keyboard record / jump record), then use that in the input manager instead fo the currently hard coded keys
    * might need to make enums to make string names to the input keycode
* More in depth version of play modes
    * add new file called data/tournaments, in these have a tournament folder with tournament config and save, tournament config would just be a list of gamefolders/gameConfig and the specific level index to run in order
    * to support in authoring environment, would be more in depth as would need a different upload/save system, then would use the fileManager to see what games are available and which levels in the game, then can select those to add and rearrange in the order
    * however loading in the game and saving would be similar since now the level index corresponds to game/level, and score is still cumulative
    * can also create a similar screen to game selector with tournaments by using fileManager to see available tournaments

 

