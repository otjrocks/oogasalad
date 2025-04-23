package oogasalad.player.model.save;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import oogasalad.engine.records.config.model.SaveConfigRecord;

/**
 * Manages Save Files
 * @author Luke Fu
 */
public class SaveManager {

  private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
  private static final String SAVE_FOLDER = "data/saves/"; // 📁 You can change this

  private static File getSaveFile(String gameFolderName, String saveName) {
    return new File("data/games/" + gameFolderName + "/saves/" + saveName + ".json");
  }

  /**
   * Saves the provided saveConfig object to a JSON file named after its saveName.
   */
  public static void saveGame(SaveConfigRecord saveConfig, String gameFolderName) throws IOException {
    File folder = new File("data/games/" + gameFolderName + "/saves/");
    if (!folder.exists()) {
      folder.mkdirs();
    }
    MAPPER.writeValue(getSaveFile(gameFolderName, saveConfig.saveName()), saveConfig);
  }

  /**
   * Loads a SaveConfig from a save name.
   */
  public static SaveConfigRecord loadGame(String gameFolderName, String saveName) throws IOException {
    return MAPPER.readValue(getSaveFile(gameFolderName, saveName), SaveConfigRecord.class);
  }
}
