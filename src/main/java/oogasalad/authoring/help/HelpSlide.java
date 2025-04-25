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
    slide.setPadding(new Insets(10));
    slide.setStyle("-fx-background-color: white; -fx-border-color: #dddddd; -fx-border-radius: 5;");

    // Title
    Label titleLabel = new Label(title);
    titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

    // Content
    Label contentLabel = new Label(description);
    contentLabel.setWrapText(true);
    contentLabel.setStyle("-fx-font-size: 16px;");

    slide.getChildren().addAll(titleLabel, contentLabel);

    // Optional image
    if (imagePath != null) {
      try {
        ImageView imageView = new ImageView(new Image(
            Objects.requireNonNull(getClass().getResourceAsStream("/images/help/" + imagePath))));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(600);

        // Add some padding around the image
        VBox imageBox = new VBox(imageView);
        imageBox.setPadding(new Insets(10));
        imageBox.setAlignment(Pos.CENTER);

        slide.getChildren().add(imageBox);
      } catch (Exception e) {
        // If image loading fails, add a placeholder
        Rectangle placeholder = new Rectangle(600, 300);
        placeholder.setFill(Color.LIGHTGRAY);
        placeholder.setStroke(Color.GRAY);

        VBox placeholderBox = new VBox(placeholder);
        placeholderBox.setPadding(new Insets(10));
        placeholderBox.setAlignment(Pos.CENTER);

        Label placeholderLabel = new Label(LanguageManager.getMessage("IMAGE") + imagePath);
        placeholderBox.getChildren().add(placeholderLabel);

        slide.getChildren().add(placeholderBox);
      }
    }

    return new ScrollPane(slide);
  }
}
