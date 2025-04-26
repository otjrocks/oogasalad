package oogasalad.authoring.view.gameSettings;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.view.components.FormattingUtil;

public class MetadataEditor {

  private final TextField titleField;
  private final TextField authorField;
  private final TextField descriptionField;
  private final GridPane root;

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

  public Node getNode() {
    return root;
  }

  public String getTitle() {
    return titleField.getText();
  }

  public String getAuthor() {
    return authorField.getText();
  }

  public String getDescription() {
    return descriptionField.getText();
  }

  public void update(String title, String author, String description) {
    titleField.setText(title);
    authorField.setText(author);
    descriptionField.setText(description);
  }

}
