package oogasalad.authoring.view.gameSettings;

/**
 * Container class representing the selected win and lose condition settings.
 */
public record ConditionState(String winType, String winValue, String loseType, String loseValue) {

}
