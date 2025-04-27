package oogasalad.authoring.help;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import oogasalad.engine.utility.LanguageManager;
import java.util.ArrayList;

/**
 * The HelpSlideContent class is responsible for loading and managing help slides
 * from a JSON file. It uses the Jackson library to parse the JSON content and
 * constructs a list of HelpSlide objects. Each slide contains a title, content,
 * and optionally an image.
 * 
 * <p>This class relies on the LanguageManager to retrieve localized messages
 * for the slide titles and content based on keys provided in the JSON file.</p>
 * 
 * <p>Usage:</p>
 * <pre>
 * HelpSlideContent helpSlideContent = new HelpSlideContent();
 * List<HelpSlide> slides = helpSlideContent.getHelpSlides();
 * </pre>
 */
public class HelpSlideContent {

  private final ObjectMapper objectMapper;
  private List<HelpSlide> helpSlides;

  /**
   * Constructs a HelpSlideContent object and initializes the ObjectMapper instance.
   * Automatically loads the help slides upon instantiation.
   */
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

  /**
   * Retrieves the list of help slides.
   *
   * @return a list of HelpSlide objects representing the help slides.
   */
  public List<HelpSlide> getHelpSlides() {
    return helpSlides;
  }

}
