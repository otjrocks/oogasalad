# DESIGN Document for Oogasalad

### Team 1 (PacOne)

### Ishan Madan (im121), Owen Jennings (otj4), Jessica Chen (jc939), Angela Predolac (ap721), William He (wwh15), Luke Fu (lmf59), Austin Huang (ash110), Troy Ludwig (tdl21)

## Team Roles and Responsibilities

* Ishan Madan

* Owen Jennings

* Jessica Chen
    * I worked mainly on implementing the code ofr making intelligent AI for enemies or non-playable
      characters focusing on the control, target, and path finding strategies. I also mainly just
      outside of that worked on completing tasks for refactoring/picking up smaller tasks to clean
      and polish the project. Additionally I did a lot with planning and designing what is needed to
      make the hierarchy of the project and what strategies are needed in the config. For example, I
      worked on adding tests, refactoring languages and styling, etc.

* Angela Predolac

* William He
  * I worked mainly on implementing the authoring environment and making it consistent with the config data structure.
I also built configParser and configBuilder, implemented cheat keys, and helped with thinking through how we wanted to implement control strategies. My main contribution
was importing a file on the authoring environment.

* Luke Fu

* Austin Huang

* Troy Ludwig

## Design Goals

* Make it easy to add new games

* Make it easy to add new strategies (different win conditions, different ways to control an entity,
  different collision strategies)

* Comprehensive and understandable user interface especially for using the authoring environment

* Make it easy to test games from the authoring and iterate after testing the game in the player

#### How were Specific Features Made Easy to Add

* New games

* New themes/languages

* New strategy choices (i.e. control strategies, win/loss conditions, collision strategies)
    * Just need to create the appropriate record for parsingk, then a strategy with a corresponding
      name in the appropriate package. Since the authoring environment polls the strategies and
      their parameters using reflection nothing else needs to change.

## High-level Design

#### Core Classes and Abstractions, their Responsibilities and Collaborators

* Class #1

* Class #2

* Class #3

* Class #4

## Assumptions or Simplifications

* Decision #1

* Decision #2

* Decision #3

* Decision #4

## Changes from the Original Plan

* Configuration File Organization
    * Level Map Parsing
    * Parameter Management

* Sprites

* In addition to collision events, time/score based spawn and mode change events

* Switch from dynamic game rules to help system for basic extension

## How to Add New Features

#### Features Designed to be Easy to Add

* Do these from the top ones

#### Features Not Yet Done

For the most part, the main feature not implemented is running multiple game instances, the other
remaining features are features that are done partially but were not fully fleshed out and polished
features

* Running multiple game instances

* Allow for keys to be configurable for say keyboard strategy and cheat codes

* More in depth version of play modes

 
