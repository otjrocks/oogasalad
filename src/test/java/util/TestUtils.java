package util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.scene.text.Text;
import oogasalad.engine.config.JsonConfigParser;
import oogasalad.engine.records.config.ConfigModelRecord;

/**
 * Static helper methods for common testing tasks
 *
 * @author Owen Jennings
 */
public class TestUtils extends DukeApplicationTest {

  /**
   * Helper method to verify if a specific Text node matches expected text
   *
   * @param fxId    the FX ID of the Text node
   * @param message the message you are expecting
   */
  public void verifyText(String fxId, String message) {
    Text textNode = lookup(fxId).queryAs(Text.class);
    assertEquals(message, textNode.getText());
  }

  public static ConfigModelRecord loadMockConfig(String pathToJson) throws Exception {
    String actualPath = TestUtils.class.getClassLoader().getResource(pathToJson).getPath();
    JsonConfigParser configParser = new JsonConfigParser();
    return configParser.loadFromFile(actualPath);
  }

}
