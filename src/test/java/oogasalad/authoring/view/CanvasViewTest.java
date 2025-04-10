package oogasalad.authoring.view;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.model.EntityPlacement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import java.util.List;

/**
 * Tests for CanvasView interactions.
 * Uses mocked controller and fake EntityPlacement objects.
 * Verifies UI state changes after actions like add or click.
 */
public class CanvasViewTest extends DukeApplicationTest {

  private CanvasView canvasView;
  private AuthoringController mockController;

  @Override
  public void start(Stage stage) {
    mockController = mock(AuthoringController.class);
    canvasView = new CanvasView(mockController);
    Scene scene = new Scene(canvasView.getNode(), 800, 600);
    stage.setScene(scene);
    stage.show();
  }

  @BeforeEach
  public void resetCanvas() {
    interact(() -> {
      canvasView.resetEntityTracking();
      canvasView.reloadFromPlacements(List.of());
    });
  }

  @Test
  public void addEntityVisual_ValidPlacement_VisualAdded() {
    EntityPlacement placement = mock(EntityPlacement.class);
    when(placement.getX()).thenReturn(80.0);
    when(placement.getY()).thenReturn(120.0);
    when(placement.getEntityImagePath()).thenReturn(getClass().getResource("/mock.png").toExternalForm());

    interact(() -> canvasView.addEntityVisual(placement));

    boolean found = canvasView.getNode().getChildren().stream()
        .filter(n -> n instanceof ImageView)
        .anyMatch(n -> ((ImageView) n).getX() == 80.0 && ((ImageView) n).getY() == 120.0);

    assertTrue(found, "Expected entity visual at (80, 120) to be added");
  }

  @Test
  public void reloadFromPlacements_ValidList_ClearsAndReplacesVisuals() {
    EntityPlacement p1 = mock(EntityPlacement.class);
    when(p1.getX()).thenReturn(0.0);
    when(p1.getY()).thenReturn(0.0);
    when(p1.getEntityImagePath()).thenReturn(getClass().getResource("/mock.png").toExternalForm());

    EntityPlacement p2 = mock(EntityPlacement.class);
    when(p2.getX()).thenReturn(40.0);
    when(p2.getY()).thenReturn(0.0);
    when(p2.getEntityImagePath()).thenReturn(getClass().getResource("/mock.png").toExternalForm());

    interact(() -> canvasView.reloadFromPlacements(List.of(p1, p2)));

    long count = canvasView.getNode().getChildren().stream().filter(n -> n instanceof ImageView).count();
    assertEquals(2, count, "Expected exactly 2 entity visuals after reload");
  }

  @Test
  public void clickOnEntity_EntityClicked_SelectionHighlightAppears() {
    EntityPlacement p = mock(EntityPlacement.class);
    when(p.getX()).thenReturn(40.0);
    when(p.getY()).thenReturn(40.0);
    when(p.getEntityImagePath()).thenReturn(getClass().getResource("/mock.png").toExternalForm());

    interact(() -> canvasView.addEntityVisual(p));

    ImageView image = (ImageView) canvasView.getNode().getChildren().stream()
        .filter(n -> n instanceof ImageView)
        .findFirst()
        .orElseThrow();

    clickOn(image);

    assertTrue(canvasView.getSelectionHighlightVisible(), "Selection highlight should be visible after click");
    assertEquals(40.0, canvasView.getSelectionHighlightX(), 0.1);
    assertEquals(40.0, canvasView.getSelectionHighlightY(), 0.1);
  }

  @Test
  public void handleEntityMouseDragged_NullSelection_NoCrash() {
    assertDoesNotThrow(() -> interact(() -> canvasView.handleEntityMouseDragged(null)));
  }
}
