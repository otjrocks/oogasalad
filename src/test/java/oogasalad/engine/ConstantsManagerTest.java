package oogasalad.engine;

import oogasalad.engine.exceptions.ConstantsManagerException;
import oogasalad.engine.utility.ConstantsManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConstantsManagerTest {

  @Test
  void getMessage_ValidKey_ReturnCorrectMessage() {
    try {
      String message = ConstantsManager.getMessage("Config", "ASSETS_FOLDER");
      Assertions.assertEquals("core/assets/", message);
    } catch (ConstantsManagerException e) {
      Assertions.fail("Exception should not be thrown for valid key");
    }
  }

  @Test
  void getMessage_InvalidKey_ThrowsException() {
    try {
      ConstantsManager.getMessage("Config", "NON_EXISTENT_KEY");
      Assertions.fail("Expected ConstantsManagerException for missing key");
    } catch (ConstantsManagerException e) {
      // Exception expected due to missing key
      Assertions.assertTrue(e.getMessage().contains("missing"));
    }
  }

  @Test
  void getMessage_InvalidFile_ThrowsException() {
    try {
      ConstantsManager.getMessage("NonExistentFile", "ASSETS_FOLDER");
      Assertions.fail("Expected ConstantsManagerException for missing file");
    } catch (ConstantsManagerException e) {
      // Exception expected due to missing file
      Assertions.assertTrue(e.getMessage().contains("missing"));
    }
  }

  @Test
  void getMessage_ValidKeys_CheckAllFolders() {
    try {
      Assertions.assertEquals("core/assets/",
          ConstantsManager.getMessage("Config", "ASSETS_FOLDER"));
      Assertions.assertEquals("core/entities/",
          ConstantsManager.getMessage("Config", "ENTITIES_FOLDER"));
      Assertions.assertEquals("core/maps/", ConstantsManager.getMessage("Config", "MAPS_FOLDER"));
      Assertions.assertEquals("saves/", ConstantsManager.getMessage("Config", "SAVES_FOLDER"));
    } catch (ConstantsManagerException e) {
      Assertions.fail("Should not throw exception for valid keys");
    }
  }
}
