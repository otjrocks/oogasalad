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
  public static void saveGame(SaveConfigRecord saveConfig, String gameFolder) throws IOException {
    File folder = new File("data/games/" + gameFolder + "/saves");
    if (!folder.exists()) {
      folder.mkdirs();
    }
    File saveFile = new File(folder, saveConfig.saveName() + ".json");
    MAPPER.writeValue(saveFile, saveConfig);
  }

  /**
   * Loads a SaveConfig from a save name.
   */
  public static SaveConfigRecord loadGame(String gameFolder, String saveName) throws IOException {
    File saveFile = new File("data/games/" + gameFolder + "/saves", saveName + ".json");
    return MAPPER.readValue(saveFile, SaveConfigRecord.class);
  }
}
