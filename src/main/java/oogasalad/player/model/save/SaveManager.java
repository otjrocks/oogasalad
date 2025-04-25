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
  private static final String SAVE_FOLDER = "data/saves/";
  private static final String SAVE_FILE_EXTENSION = ".json";
  private static final String PARENT_FOLDER = "data/games/";

  private static File getSaveFile(String gameFolderName, String saveName) {
    return new File(PARENT_FOLDER + gameFolderName + "/saves/" + saveName + SAVE_FILE_EXTENSION);
  }

  /**
   * Saves the provided saveConfig object to a JSON file named after its saveName.
   */
  public static void saveGame(SaveConfigRecord saveConfig, String gameFolder) throws IOException {
    File folder = new File(PARENT_FOLDER + gameFolder + "/saves");
    if (!folder.exists()) {
      folder.mkdirs();
    }
    File saveFile = new File(folder, saveConfig.saveName() + SAVE_FILE_EXTENSION);
    MAPPER.writeValue(saveFile, saveConfig);
  }

  /**
   * Loads a SaveConfig from a save name.
   */
  public static SaveConfigRecord loadGame(String gameFolder, String saveName) throws IOException {
    File saveFile = new File(PARENT_FOLDER + gameFolder + "/saves", saveName + SAVE_FILE_EXTENSION);
    return MAPPER.readValue(saveFile, SaveConfigRecord.class);
  }
}
