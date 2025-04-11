package oogasalad.authoring.view;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.records.newconfig.ImageConfig;
import oogasalad.engine.records.newconfig.ModeConfig;
import oogasalad.engine.records.newconfig.model.ControlType;
import oogasalad.engine.records.newconfig.model.ControlTypeConfig;
import oogasalad.engine.records.newconfig.model.EntityProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EntitySelectorViewTest extends DukeApplicationTest {

  private EntitySelectorView view;
  private AuthoringController mockController;
  private ModeConfig mode;

  @Override
  public void start(javafx.stage.Stage stage) {
    mockController = mock(AuthoringController.class);
    view = new EntitySelectorView(mockController);
    javafx.scene.Scene scene = new javafx.scene.Scene((VBox) view.getRoot(), 500, 400);
    stage.setScene(scene);

    // --- Create ImageConfig ---
    ImageConfig imageConfig = new ImageConfig(
        "mock.png", // Should be a relative path or valid URI
        14,
        14,
        List.of(0, 1, 2),
        1.0
    );

    // --- Create EntityProperties ---
    ControlTypeConfig controlConfig = new ControlTypeConfig("None", 0);
    ControlType controlType = new ControlType("Keyboard", controlConfig);

    EntityProperties props = new EntityProperties(
        "Mode 1",
        controlType,
        100.0,
        List.of()
    );

    // --- Create ModeConfig ---
    mode = new ModeConfig("Default", props, imageConfig);

    stage.show();
  }

  @BeforeEach
  public void setup() {
    reset(mockController);
  }

  @Test
  public void addButton_Click_CallsCreateNewEntityType() {
    Button addButton = lookup("+ Add Entity Type").queryButton();
    runAsJFXAction(() -> clickOn(addButton));
    verify(mockController).createNewEntityType();
  }

  @Test
  public void updateEntities_CreatesCorrectTiles() {

    // --- Create EntityType ---
    EntityType entity = new EntityType(
        "Ghost",
        "Keyboard",
        "",
        Map.of("Default", mode),
        List.of(),
        Map.of()
    );

    // --- Run and verify ---
    runAsJFXAction(() -> view.updateEntities(List.of(entity)));

    FlowPane tileGrid = (FlowPane) lookup(".flow-pane").query();
    assertEquals(1, tileGrid.getChildren().size());
    VBox tile = (VBox) tileGrid.getChildren().get(0);
    assertTrue(tile.getChildren().get(0) instanceof ImageView);
  }


  @Test
  public void selectEntityType_ClickTile_CallsSelectEntityType() {
    EntityType entity = new EntityType("Pacman", "Keyboard", "", Map.of("Default", mode), List.of(), Map.of());
    runAsJFXAction(() -> view.updateEntities(List.of(entity)));

    VBox tile = (VBox) ((FlowPane) lookup(".flow-pane").query()).getChildren().get(0);
    runAsJFXAction(() -> clickOn(tile));

    verify(mockController).selectEntityType("Pacman");
  }

  @Test
  public void highlightEntityTile_HighlightsCorrectTile() {
    EntityType entity = new EntityType("Wall", "None", "", Map.of("Default", mode), List.of(), Map.of());
    runAsJFXAction(() -> view.updateEntities(List.of(entity)));
    runAsJFXAction(() -> view.highlightEntityTile("Wall"));

    VBox tile = (VBox) ((FlowPane) lookup(".flow-pane").query()).getChildren().get(0);
    assertTrue(tile.getStyleClass().contains("selected-tile"));
  }
}
