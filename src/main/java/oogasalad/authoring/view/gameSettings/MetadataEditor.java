package oogasalad.authoring.view.gameSettings;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.view.components.FormattingUtil;

/**
 * MetadataEditor provides UI fields for editing game metadata:
 * title, author name, and description.
 *
 * Fields are organized in a GridPane layout and can be updated dynamically.
 *
 * Designed to be a modular component within the GameSettingsView.
 *
 * @author
 * William He
 */
public class MetadataEditor {

  private final TextField titleField;
  private final TextField authorField;
  private final TextField descriptionField;
  private final GridPane root;

  /**
   * Constructs a MetadataEditor with initial metadata values.
   *
   * @param title       the game title
   * @param author      the author's name
   * @param description the game description
   */
  public MetadataEditor(String title, String author, String description) {
    root = new GridPane();
    root.setHgap(10);
    root.setVgap(10);
    root.setPadding(new Insets(10));

    titleField = FormattingUtil.createTextField(title);
    authorField = FormattingUtil.createTextField(author);
    descriptionField = FormattingUtil.createTextField(description);

    root.add(new Label(LanguageManager.getMessage("GAME_TITLE")), 0, 0);
    root.add(titleField, 1, 0);
    root.add(new Label(LanguageManager.getMessage("AUTHOR")), 0, 1);
    root.add(authorField, 1, 1);
    root.add(new Label(LanguageManager.getMessage("DESCRIPTION")), 0, 2);
    root.add(descriptionField, 1, 2);
  }

  /**
   * Returns the root Node containing the metadata fields.
   *
   * @return the Node representing the editor layout
   */
  public Node getNode() {
    return root;
  }

  /**
   * Gets the current game title entered by the user.
   *
   * @return the game title
   */
  public String getTitle() {
    return titleField.getText();
  }

  /**
   * Gets the current author name entered by the user.
   *
   * @return the author name
   */
  public String getAuthor() {
    return authorField.getText();
  }

  /**
   * Gets the current game description entered by the user.
   *
   * @return the game description
   */
  public String getDescription() {
    return descriptionField.getText();
  }

  /**
   * Updates the metadata fields with new values.
   *
   * @param title       the new title
   * @param author      the new author name
   * @param description the new description
   */
  public void update(String title, String author, String description) {
    titleField.setText(title);
    authorField.setText(author);
    descriptionField.setText(description);
  }
}
