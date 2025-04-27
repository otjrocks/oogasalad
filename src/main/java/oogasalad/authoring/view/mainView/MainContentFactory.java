package oogasalad.authoring.view.mainView;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Responsible for building the main content area layout of the AuthoringView.
 *
 * <p>This includes assembling the left panel (level controls), center canvas view,
 * and right panel (entity selection and editing tools).</p>
 *
 * <p>Encapsulates layout construction separately from AuthoringView to improve modularity
 * and maintainability.</p>
 *
 * <p>Typically used internally by {@link AuthoringLayoutBuilder}.</p>
 *
 * @author William He
 */
public class MainContentFactory {

  private final AuthoringView view;

  /**
   * Constructs a MainContentFactory to build content for the given AuthoringView.
   *
   * @param view the parent AuthoringView
   */
  public MainContentFactory(AuthoringView view) {
    this.view = view;
  }

  /**
   * Creates the full main content layout consisting of the left panel, canvas,
   * and right panel, and returns it as a BorderPane.
   *
   * @return the constructed main content pane
   */
  public BorderPane createMainContent() {
    BorderPane mainContent = new BorderPane();

    VBox left = new VBox(10, view.getLevelSelectorView().getRoot(), view.getLevelSettingsView().getNode());
    mainContent.setLeft(left);

    Node canvasNode = view.getCanvasView().getNode();
    VBox canvasWrapper = new VBox(canvasNode);
    VBox.setVgrow(canvasNode, Priority.ALWAYS);
    mainContent.setCenter(canvasWrapper);

    VBox rightPanel = new VBox(10, view.getEntitySelectorView().getRoot(), createEditorContainer());
    rightPanel.setFillWidth(true);
    mainContent.setRight(rightPanel);

    return mainContent;
  }

  /**
   * Creates and returns the container for the entity editing views (type editor and placement editor).
   *
   * @return the configured AnchorPane containing entity editors
   */
  private AnchorPane createEditorContainer() {
    AnchorPane container = new AnchorPane();
    container.setMaxHeight(400);
    container.getStyleClass().add("editor-container");

    view.addEntityTypeEditor(container);
    view.addEntityPlacementView(container);

    return container;
  }
}
