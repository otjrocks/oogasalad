# Design Plan

## Project Layout (Packages and Example Classes)

* /editor
    * /model
    * /view
    * /controller
    * Main()
* /game
    * /model
    * /view
    * /controller
    * Main()
* /shared
    * /model
    * /components
        * Button
        * FileChooser
        * SelectorField
        * InputField
        * AlertField/Dialog
        * …
    * LanguageManager
    * ThemeManager
* /utility
    * FileHandler
        * JSONFileHandler
* Main()

## Game Player

### Model

* GameState: A collection of the current games statistics, such as score, lives, etc.
    * GameMetaData
    * ScoreManager: Class to handle the updating and accessing of the score of the game
    * LifeManager: Class to handle the updating and accessing of the player’s lives
    * Various information such as map state, and player locations
    * Can be wrote to file/database to allow for game progress save and load.

### View

* GameView
    * GameMap
    * GameEntities
* HUDView
* SettingsView
* HighScoreView
* StartScreenView
* LevelSelectorView / SaveSelector / GameModeSelector

### Controller

* MainController
* AnimationController: Control game animation, play, pause, speed, etc.
* FileController: Control saving state to file / and opening of new game file
* GameController: Control lives, end of game  
  Game Update Loop:
* Updates all Updatable entities
* Handles input and routes to Controllable entity
* Checks collisions using CollisionManager
* Checks win/loss via GameGoal
* Triggers UI updates

## **Authoring Environment (AE)**

### Model

#### **AuthoringModel**

* Top-level model that manages all authoring data

#### **EntityTemplate**

* `String type`
* `String imagePath`
* `String controlType`
* `String effectStrategy`

#### **EntityPlacement**

* `EntityTemplate template`
* `double x`
* `double y`

#### **CollisionRuleEditorModel**

* Stores and manages all (entityA, entityB) → strategy mappings

#### **GameSettingsModel**

* `double gameSpeed`
* `int startingLives`
* `String edgePolicy`

### View

#### **EntitySelectorView**

* Shows all `EntityTemplate`s available for placement

#### **CanvasView**

* Visual placement of entities

#### **EntityEditorView**

* Allows changes to an `EntityTemplate` (image, movement, effect)

#### **CollisionRuleEditorView**

* Grid/table UI to define `CollisionStrategy` between entity pairs

#### **GameSettingsView**

* Form-style UI for everything in `GameSettings`

### Controller

#### **AuthoringController**

* Manages user actions and updates the `AuthoringModel`

## Shared

### Model

* Entity \<Interface\>
    * StaticEntity (Background elements)
    * DynamicEntity
        * MoveableDynamicEntity: i.e. ghost, Pacman (has MovementStrategy, CollisionStrategy))
        * ImmobileDynamicEntity: i.e. power up, big dot (has CollisionStrategy)
* EntityFactory
* Strategy
    * MovementStrategy \<Interface\>
        * AIMovementStrategy
        * PlayerControlled
    * CollisionStrategy \<Interface\>: What to do when an entity collides with another entity (is in same point on grid/map)
    * EdgeStrategy: What to do when an entity reaches the edge of the map.
    * EffectStrategy \<Interface\>: how to handle a power up/down
        * Effect: different changes that can occur in the game / on screen. (i.e. teleport, update speed, add life, etc)
    * GameOutcomeStrategy \<Interface\>: A strategy design pattern which has methods like boolean hasGameEnded(GameState state) and String getGameOutcome(GameState state) which return if the game has ended in the current iteration of the game loop and if so what the outcome was.
* GameMap
* ConfigModel
* ConfigParser
* ConfigLoader