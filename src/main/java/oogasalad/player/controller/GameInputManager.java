package oogasalad.player.controller;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import oogasalad.player.view.GameScreenView;

/**
 * Class for handling user input for controllable entities in the game player
 */
public class GameInputManager {

  private final Group root;
  private final Set<KeyCode> activeKeys = new HashSet<>();
  private KeyCode lastActiveKey = null;
  private GameScreenView gameScreenView;

  /**
   * Constructor for the input manager that records what keys are actively being pressed by the
   * user.
   *
   * @param scene: The scene currently displaying the controllable entities
   * @param root:  The content root the game player is a child of
   */
  public GameInputManager(Scene scene, Group root) {
    this.root = root;
    scene.setOnKeyPressed(event -> {
      activeKeys.remove(lastActiveKey);
      activeKeys.add(event.getCode());
      lastActiveKey = event.getCode();
    });
  }

  /**
   * Returns the content root referenced by the content root.
   */
  public Group getRoot() {
    return root;
  }

  /**
   * Checks if the user wants the entity to go up.
   */
  public boolean isMovingUp() {
    return activeKeys.contains(KeyCode.UP);
  }

  /**
   * Checks if the user wants the entity to go down.
   */
  public boolean isMovingDown() {
    return activeKeys.contains(KeyCode.DOWN);
  }

  /**
   * Checks if the user wants the entity to go left.
   */
  public boolean isMovingLeft() {
    return activeKeys.contains(KeyCode.LEFT);
  }

  /**
   * Checks if the user wants the entity to go right.
   */
  public boolean isMovingRight() {
    return activeKeys.contains(KeyCode.RIGHT);
  }

  /**
   * Checks if the user wants to skip to the next level
   */
  public boolean shouldGoToNextLevel(){
    if(activeKeys.contains(KeyCode.N)){
      activeKeys.remove(KeyCode.N);
      return true;
    }
    return false;
  }

  /**
   * Checks if the user wants to reset the game
   */
  public boolean shouldResetGame(){
    if(activeKeys.contains(KeyCode.R)){
      activeKeys.remove(KeyCode.R);
      return true;
    }
    return false;
  }

  /**
   * Checks if the user wants to add a life to their life counter
   */
  public boolean shouldAddLife(){
    if(activeKeys.contains(KeyCode.EQUALS)){
      activeKeys.remove(KeyCode.EQUALS);
      return true;
    }
    return false;
  }

  /**
   * Checks if the user wants to speed up the game
   */
  public boolean shouldSpeedUpGame(){
    if(activeKeys.contains(KeyCode.S)){
      activeKeys.remove(KeyCode.S);
      return true;
    }
    return false;
  }

  /**
   * Checks if the user wants to pause the game
   */
  public boolean shouldPauseGame() {
    if(activeKeys.contains(KeyCode.P)){
      activeKeys.remove(KeyCode.P);
      return true;
    }
    return false;
  }

  /**
   * Sets GameScreenView object so necessary visual elements can be stored and updated
   */
  public void setGameScreenView(GameScreenView view) {
    gameScreenView = view;
  }

  /**
   * Gets GameScreenView object for purposes of updating levels and resetting game
   */
  public GameScreenView getGameScreenView() {
    return gameScreenView;
  }


  /**
   * Remove up key to allow for non constant direction
   */
  public void removeUpKey() {
    activeKeys.remove(KeyCode.UP);
  }
}

