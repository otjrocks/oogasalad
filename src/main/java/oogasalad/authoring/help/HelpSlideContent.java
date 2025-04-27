package oogasalad.authoring.help;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import oogasalad.engine.utility.LanguageManager;
import java.util.ArrayList;

public class HelpSlideContent {

  private final ObjectMapper objectMapper;
  private List<HelpSlide> helpSlides;

  public HelpSlideContent() {
    this.objectMapper = new ObjectMapper();
    loadHelpSlides();
  }

  private void loadHelpSlides() {
    try {
      String jsonContent = new String(Files.readAllBytes(Paths.get("data/helpslides.json")));

      JsonNode rootNode = objectMapper.readTree(jsonContent);

      JsonNode slidesJson = rootNode.get("slides");

      helpSlides = new ArrayList<>();

      for (JsonNode slideJson : slidesJson) {
        String key = slideJson.get("key").asText();
        String title = LanguageManager.getMessage(key + "_TITLE");
        String content = LanguageManager.getMessage(key + "_CONTENT");
        String image = slideJson.has("image") ? slideJson.get("image").asText() : null;

        HelpSlide slide = new HelpSlide(title, content, image);
        helpSlides.add(slide);
      }
    } catch (Exception e) {
      throw new HelpException("Failed to load help content", e);
    }
  }

  public List<HelpSlide> getHelpSlides() {
    return helpSlides;
  }

}
