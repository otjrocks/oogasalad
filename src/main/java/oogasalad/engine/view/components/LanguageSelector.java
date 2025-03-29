package oogasalad.engine.view.components;

import oogasalad.engine.LanguageManager;

public class LanguageSelector {

  private final Selector mySelector;
  private final LanguageManager myLanguageManager = new LanguageManager();

  public LanguageSelector() {
    mySelector = new Selector(myLanguageManager.getAvailableLanguages(),
        myLanguageManager.getLanguage(),
        "language-selector", myLanguageManager.getMessage("LANGUAGE_SELECTOR_TITLE"),
        e -> handleLanguageSelection());
  }

  public Selector getSelector() {
    return mySelector;
  }

  private void handleLanguageSelection() {
    myLanguageManager.setLanguage(mySelector.getValue());
  }
}
