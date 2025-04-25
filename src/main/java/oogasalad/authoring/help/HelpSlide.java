package oogasalad.authoring.help;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Objects;
import oogasalad.engine.utility.LanguageManager;

/**
 * A class representing a single help slide with title, content, and optional image.
 *
 * @author Angela Predolac
 */
public class HelpSlide {
  private static final double SLIDE_WIDTH = 760;
  private static final double SLIDE_HEIGHT = 540;
  private static final double MAX_IMAGE_WIDTH = 600;

  private final String title;
  private final String description;
  private final String imagePath;

  /**
   * Constructs a help slide.
   *
   * @param title       the slide title
   * @param description the slide content text
   * @param imagePath   the path to an optional image (can be null)
   */
  public HelpSlide(String title, String description, String imagePath) {
    this.title = title;
    this.description = description;
    this.imagePath = imagePath;
  }

  /**
   * Creates a JavaFX node representing this slide.
   *
   * @return the slide node
   */
  public Node createSlideNode() {
    VBox slide = new VBox(20);
    slide.setAlignment(Pos.TOP_CENTER);
    slide.setPrefWidth(SLIDE_WIDTH);
    slide.setMaxWidth(SLIDE_WIDTH);
    slide.setMinHeight(SLIDE_HEIGHT);
    slide.setPadding(new Insets(10));
    slide.getStyleClass().add("help-slide");

    // Title
    Label titleLabel = new Label(title);
    titleLabel.getStyleClass().add("title");
    titleLabel.setWrapText(true);
    titleLabel.setMaxWidth(SLIDE_WIDTH);


    // Content
    Label contentLabel = new Label(description);
    contentLabel.getStyleClass().add("game-name");
    contentLabel.setWrapText(true);
    contentLabel.setMaxWidth(SLIDE_WIDTH);

    slide.getChildren().addAll(titleLabel, contentLabel);

    // Optional image
    if (imagePath != null) {
      try {
        ImageView imageView = new ImageView(new Image(
            Objects.requireNonNull(getClass().getResourceAsStream("/images/help/" + imagePath))));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(MAX_IMAGE_WIDTH);

        // Add some padding around the image
        VBox imageBox = new VBox(imageView);
        imageBox.setPadding(new Insets(10));
        imageBox.setAlignment(Pos.CENTER);

        slide.getChildren().add(imageBox);
      } catch (Exception e) {
        // If image loading fails, add a placeholder
        Rectangle placeholder = new Rectangle(MAX_IMAGE_WIDTH, 300);
        placeholder.setFill(Color.LIGHTGRAY);
        placeholder.setStroke(Color.GRAY);

        VBox placeholderBox = new VBox(placeholder);
        placeholderBox.setPadding(new Insets(10));
        placeholderBox.setAlignment(Pos.CENTER);

        Label placeholderLabel = new Label(LanguageManager.getMessage("IMAGE") + imagePath);
        placeholderLabel.getStyleClass().add("game-name");
        placeholderBox.getChildren().add(placeholderLabel);

        slide.getChildren().add(placeholderBox);
      }
    }

    ScrollPane scrollPane = new ScrollPane(slide);
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefViewportWidth(SLIDE_WIDTH);
    scrollPane.setPrefViewportHeight(SLIDE_HEIGHT);
    scrollPane.setStyle("-fx-background-color: transparent;");

    return scrollPane;
  }
}
