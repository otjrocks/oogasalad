package oogasalad.authoring.view.mainView;

import javafx.scene.Node;
import javafx.scene.layout.*;

class MainContentFactory {

  private final AuthoringView view;

  public MainContentFactory(AuthoringView view) {
    this.view = view;
  }

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

  private AnchorPane createEditorContainer() {
    AnchorPane container = new AnchorPane();
    container.setMaxHeight(400);
    container.setBorder(new Border(new BorderStroke(
        javafx.scene.paint.Color.BLUE, BorderStrokeStyle.DASHED, null, new BorderWidths(1)
    )));

    view.addEntityTypeEditor(container);
    view.addEntityPlacementView(container);

    return container;
  }
}
