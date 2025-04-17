# OOGASalad Stand Up and Retrospective Discussion

## PacOne

### Ishan Madan (im121), Owen Jennings (otj4), Jessica Chen (jc939), Angela Predolac (ap721), William He (wwh15), Luke Fu (lmf59), Austin Huang (ash110), Troy Ludwig (tdl21)

## Stand Up Meeting

### Ishan Madan

* Work done this Sprint  
  * In this sprint, I have worked on the authoring environment view and on some project-wide refactoring.  
  * In the authoring environment, I worked on theme selection for the authoring environment. I am working on making it so that when you open the authoring environment, it sets the theme from the program that is currently in place. It also gives you the dropdown to change the theme  
  * I am also working on refactoring across the program to make sure that out classes dont extend the javafx components

* Plan for next Sprint?  
  * Continue refactoring to prevent extension of javafx components  
  * Finish up the themes to make sure it correctly extends the already built in functionality

* Blockers/Issues in your way  
  * none

### Owen Jennings

* Work done this Sprint:  
  * Fixed collision strategies in authoring environment to create new configuration file correct.  
  * Added spawn events for different fruits and pacman to allow for spawning/despawning based on different conditions  
  * Updated image uploading in the authoring environment to ensure they load properly in the game player  
* Plan for next Sprint?  
  * Handling of different entity mode types  
  * Refactoring of collision events and ensuring reflection when possible  
  * Ensure that the authoring environment is properly interacting with the game player  
* Blockers/Issues in your way:  
  * Waiting for the completion of the game selector view to ease testing new games.  
  * Moving control strategies to modes will update existing configuration file format, preventing any final games from being created.

 

### Jessica Chen

* Work done this Sprint  
  * Helped refactor the new config with parameters and the new control type system  
  * Added the needed ghost strategies to do  all pacman ghost and modes  
  * Implemented control type form choosing system on the frontend

* Plan for next Sprint?  
  * Polish up any control strategies needed to implement any other game type  
  * Help out wherever I can to get the project done

* Blockers/Issues in your way  
  * N/A

 

### Angela Predolac

*  Work done this Sprint: I worked on adding functionality to delete entity types and all placements of that type, refactoring setUpSubViews method, and updating GameSettingsView to take all the correct fields from the config files and connecting it to the backend as well.

* Plan for next Sprint? I plan to work more on testing and editing config classes.

* Blockers/Issues in your way: N/A 

 

### William He

* Work done this Sprint  
  * Worked on adding more mode fields, debugging a lot of things that had slight issues in the authoring environment, refactoring to adapt to the new config, refactoring the control strategy portion of the config files, and allowing users to resize the grid per level

* Plan for next Sprint?  
  * Finish the authoring environment  
* Blockers/Issues in your way  
  * none this week

### Luke Fu

* Work done this Sprint:  
  * Completed Level loading and saving for current level by writing into the gameconfig file. Incorporated pause/play. Completed refactoring for new config. Incorporated and set up for different win strategies.

* Plan for next Sprint?  
  * Create games with custom rules.   
  * Incorporate new win strategies  
  * Incorporate lose strategies  
  * Create a timer  
  * Incorporate tracking of current level and high score on the game select screen.

* Blockers/Issues in your way  
  * Authoring environment to create games.

### Austin Huang

* Work done this Sprint  
  * Created game selector screen  
  * Implemented lose condition from config file  
  * Refactored win and lose condition outcomes in GameOutcomeFactory   
  * Implemented reset level, next level buttons  
  * Add collision strategy to change mode of entity type

* Plan for next Sprint?  
  * Upload file in game selector screen  
  * Ghost timer  
  * Time based outcome strategy

* Blockers/Issues in your way  
  * None

 

### Troy Ludwig

* Work done this Sprint  
  * Refactored sprites to be assigned to single entity and also updated modes to have individual sprites  
  * Implemented mode change collision event for ghosts when a powerpellet is consumed  
* Plan for next Sprint?  
  * Update mode collaboration for any interactions that have yet to be implemented  
  * Add more sprites for those entities yet to be added  
* Blockers/Issues in your way  
  * N/A

 

## Project's current progress

* The project is going well. We hope to start adding extension features and finalizing the basic functionality of our program. We hope to have a more rigid structure for planning the final sprint.

## Current level of communication

* There is good use of meetings and text communication. People are good at communicating when they are doing things, but not so much when they are falling behind. We are encouraging each other to reach out earlier in the week if they have problems and not wait until the night of a milestone deadline to reveal their task won’t be done. We also plan to hold group members more accountable for their assigned tasks. We also want to have more in-person group/pair programming sessions as it is much easier to communicate and collaborate in the same physical space.

## Satisfaction with team roles

* There are concerns with the current division of work where a couple people are having to carry a lot more responsibility. We plan to divide the work more evenly at the start of the week and encourage everyone to have a more substantive status update for their area of implementation at our Monday meeting. 

## Teamwork that worked well

* Thing \#1  
  * Active group chat communication and in-person programming sessions

* Thing \#2  
  * Group members being willing to help when someone is falling behind or struggling on an assigned issue

## Teamwork that could be improved

* Thing \#1  
  * Working and communicating continuously throughout the week instead of just the day of the deadline

* Thing \#2  
  * Working on tasks that are known to be blocking others or communicating when others have tasks that are blocking your progress

## Teamwork to improve next Sprint

* Moving forward, we want to structure more rigid deadlines to ensure progress can be made at a steady pace. We will attempt to break the week into multiple stages with some tasks due on Monday and others due on Thursday. We have encouraged less active members to take initiative and pick up more tasks to contribute to the completion of the project and encouraged those more active members to act as managers to hold them accountable. We are also encouraging more communication between the two sides of our project (Game Player and Authoring Environment) to ensure the entire team is on the same page when it comes to the big picture. 

