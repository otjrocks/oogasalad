package oogasalad.engine;

import java.util.List;
import oogasalad.engine.utility.LanguageManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LanguageManagerTest {

  @Test
  void getMessage_ValidKeyEnglish_ReturnCorrectMessage() {
    LanguageManager.setLanguage("English");
    String message = LanguageManager.getMessage("TITLE");
    Assertions.assertEquals("Pac-Man", message);
  }

  @Test
  void getMessage_InvalidKeyEnglish_ReturnMissingMessageString() {
    LanguageManager.setLanguage("English");
    String message = LanguageManager.getMessage("NOT_A_KEY");
    Assertions.assertEquals("Missing key", message);
  }

  @Test
  void getAvailableLanguages_EnsureCurrentLanguagesAvailable_ReturnCorrectLanguagesList() {
    Assertions.assertEquals(List.of("English", "Italian"), LanguageManager.getAvailableLanguages());
  }

  @Test
  void setLanguage_EnsureLanguageChanged_ReturnNewLanguageMessage() {
    LanguageManager.setLanguage("English");
    String message = LanguageManager.getMessage("TITLE");
    Assertions.assertEquals("Pac-Man", message);
    LanguageManager.setLanguage("Italian");
    message = LanguageManager.getMessage("TITLE");
    Assertions.assertEquals("Pac-Man: Versione Italiano", message);
  }

  @Test
  void setLanguage_EnsureLanguageChanged_NewLanguageNameChanged() {
    LanguageManager.setLanguage("English");
    LanguageManager.setLanguage("Italian");
    Assertions.assertEquals("Italian", LanguageManager.getLanguage());
  }
}