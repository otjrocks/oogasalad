# OOGASalad Stand Up and Retrospective Discussion

## PacOne

### Ishan Madan (im121), Owen Jennings (otj4), Jessica Chen (jc939), Angela Predolac (ap721), William He (wwh15), Luke Fu (lmf59), Austin Huang (ash110), Troy Ludwig (tdl21)

## Stand Up Meeting

### Ishan Madan

* Work done this Sprint
    * I worked on the authoring environment this week
    * I worked on refactoring all of the view classes in the AE to avoid extended javafx components
    * I removed extensions and set up the javafx components inside of the classes

* Plan for next Sprint?
    * For next sprint, I plan to finish up any issues I didn’t get to this week (addin default entity types) and complete any other issues that get assigned to me
    * One thing i could work on is shorten some of the AE methods/classes to be more single-purpose and clear
    * I also want to write more junit tests to increase our line coverage in the AE view.

* Blockers/Issues in your way
    * No blockers for now

### Owen Jennings

* Work done this Sprint
    * I worked on improving our game player program's memory and cpu requirements by removing dependencies on large javafx classes and refactoring our code to use a Canvas for real-time updates instead of Groups and ImageViews.
    * I also refactored our code to use the Java record type instead of classes to enforce explicit immutability.
    * I worked to remove hard-coded values for collision strategies and to use the configuration files instead.

* Plan for next Sprint?
    * Continue removing any hard-coded checks and logic in our program and replace them with strategy design patterns and configuration files.
    * Ensure that game player is properly following configuration set by the authoring environment.

* Blockers/Issues in your way
    * I am waiting for the finalized version of the configuration file and configuration file parser. I also need to better understand how we are going to create strategies from the configuration file strings.



### Jessica Chen

* Work done this Sprint
    * Configuration file refactoring for the most part as well as helping for the start of map parsing
    * Added tests for engine
    * Fixed ghost path finding, and refactored the factory to use reflection

* Plan for next Sprint?
    * add the strategies for the different ghost modes
        * scatter mode (go to an edge and circle)
        * frightened mode (randomly chose a direction where movement is possible, then travel in that direction till cannot)

* Blockers/Issues in your way
    * more clarity on when the modes are triggered



### Angela Predolac

*  Work done this Sprint
* I implemented movement for entity placements, GameSettingsView and CollisionRulesView for the authoring environment with the correct backend support, and EntityPlacementView with the backend support.

* Plan for next Sprint?
    * Add support to edit level settings, delete entity types, and customize parameters from select control types.

* Blockers/Issues in your way
    * None



### William He

* Work done this Sprint
    * I worked in the authoring environment. I implemented levels, adding and editing modes, saving into Json, and improving test coverage

* Plan for next Sprint?
    * Adding missing fields in a lot of authoring environment fields and improving design in AE

* Blockers/Issues in your way
    * Need the new config parsing structure for some things in the AE
    * Also need to determine any additional fields that will be added to any of the Config

### Luke Fu

* Work done this Sprint
    * Worked on implementing Pause/Play, refactoring codebase for implementation of the new config structure, and added in a basic level loading system that can be expanded on in the next sprint.

* Plan for next Sprint?
    * Incorporate level loading for multiple levels in a single game. Removing hard coded configurations

* Blockers/Issues in your way
    * Need more levels for testing

### Austin Huang

* Work done this Sprint
    * Worked on game outcome end label on win and lose conditions
    * Refactored parameters for strategies to be records

* Plan for next Sprint?
    * Work on game outcome in generalized (non-hard-coded) code with respect to config
    * Work on power pellets and power ups

* Blockers/Issues in your way
    * win/loss conditions in config



### Troy Ludwig

* Work done this Sprint 2
    * I updated the sprites for entities to allow for animations
        * This includes different sprites for directional movement
    * I implemented fruit spawning
    * I allowed users to return to menu from the game player
* Plan for next Sprint?
    * Change sprite handling to have one sprite sheet for each entity/mode
    * Look at entity class and remove hardcoded characters
* Blockers/Issues in your way
    * N/A



## Project's current progress

* The project is going well. We hope to start adding extension features and finalizing the basic functionality of our program.

## Current level of communication

* Good use of meetings and text communication. We could improve the amount of in person communication and pair programming.
* We hope to have more small group programming sessions.

## Satisfaction with team roles

* Most people are satisfied, and some people want more.
* We hope to improve the division of tasks and hold individuals accountable for work.

## Teamwork that worked well

* Thing \#1
    * Active group chat for communication of progress and issues.

* Thing \#2
    * Planned and unplanned team meetings to discuss issues.

## Teamwork that could be improved

* Thing \#1
    * Work continuously through the week.
    * Notify others early on if you are confused about part of the project or your responsibilities.

* Thing \#2
    * Be punctual with work so others are not blocked and feel they need to complete tasks themselves.

## Teamwork to improve next Sprint

- Having more specific deadlines. Holding individuals accountable for their contribution.

