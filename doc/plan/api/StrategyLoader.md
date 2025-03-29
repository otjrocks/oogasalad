## StrategyLoader

### Overview

The StrategyLoader API is designed to allow easy access to strategy design patterns used in the game (MovementStrategy, CollisionStrategy, EdgeStrategy, GameOutcomeStrategy). Strategies are used in the GamePlayer to allow various interactions within the game. The AuthoringEnvironment needs access to these strategies to allow a user building a game to define these interactions in their game level. 

Since the StrategyLoader API already uses the strategy design pattern, it is naturally extensible.

* MovementStrategy \<Interface\>  
  * AIMovementStrategy  
    * PlayerControlled  
  * CollisionStrategy \<Interface\>: What to do when an entity collides with another entity (is in same point on grid/map)  
  * EdgeStrategy: What to do when an entity reaches the edge of the map.  
  * EffectStrategy \<Interface\>: how to handle a power up/down  
    * Effect: different changes that can occur in the game / on screen. (i.e. teleport, update speed, add life, etc)  
  * GameOutcomeStrategy \<Interface\>: A strategy design pattern which has methods like boolean hasGameEnded(GameState state) and String getGameOutcome(GameState state) which return if the game has ended in the current iteration of the game loop and if so what the outcome was.

SOLID / Object Oriented Design

- S: each strategy focuses on only one specific function, making the StrategyLoader easy to extend  
- O: since strategies are interfaces, they can be extended without modifying the StrategyLoader  
- L: all concrete strategies can be substituted for their interfaces without breaking the game  
- I: there are no unnecessary dependencies between strategies, as each strategy only exposes methods related to its specific functionality  
- D: this system focuses on abstractions as interfaces for strategies rather than concrete implementations

Extensible  / How to Think About Tasks

- Client code that uses StrategyLoader does not care or see how each strategy is implemented, only caring about what strategies there are

### Classes

**public class StrategyLoader**

- List\<Class\<? extends MovementStrategy\>\> getAvailableMovementStrategies()  
- List\<Class\<? extends CollisionStrategy\>\> getAvailableCollisionStrategies()  
- List\<Class\<? extends EdgeStrategy\>\> getAvailableEdgeStrategies()  
- List\<Class\<? extends EffectStrategy\>\> getAvailableEffectStrategies()  
- List\<Class\<? extends GameOutcomeStrategy\>\> getAvailableGameOutcomeStrategies()

**Custom exceptions**

- LoadStrategyException  
  - When no strategies available or some other error when loading strategies

### Details / Use Cases

This API will primarily be used by the game authoring to get the available strategies implemented in the game player.

Here are some specific Use Cases that will need to interact with/implement this API:

* Use Case: AUTH-2: dropdown to set movement strategy of entity  
* Use Case: AUTH-9: dropdown to set collision strategy between entities  
* Use Case: MAP-GENERATION-5: dropdown to set edge strategy of grid  
* Use Case: AUTH-9: dropdown to set effect strategy after entity interaction with powerup  
* Use Case: AUTH-11: dropdown to set game outcome strategy for win or loss

### Considerations

A consideration is what type we want to actually return from the get available strategy methods, whether we want to get a list of classes or instances of the concrete strategy implementations. This also makes the assumption that each type strategy is an interface that has multiple concrete implementations.