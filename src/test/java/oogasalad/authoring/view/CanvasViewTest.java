package oogasalad.authoring.view;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;

/**
 * TestFX-based tests for CanvasView that simulate actual user interactions
 */
@ExtendWith(ApplicationExtension.class)
public class CanvasViewTest {

    private CanvasView canvasView;
    private AuthoringController mockController;
    private EntityPlacement testEntity1;
    private EntityPlacement testEntity2;
    private List<EntityPlacement> testEntities;

    private static final int TILE_SIZE = 40;
    private static final String DEFAULT_IMAGE_PATH = "defaultImage.png";

    private double entity1X, entity1Y;
    private double entity2X, entity2Y;

    /**
     * Will be called with {@code @Before} semantics, i.e. before each test.
     *
     * @param stage - Will be injected by the test runner.
     */
    @Start
    public void start(Stage stage) {
        mockController = mock(AuthoringController.class);
        setupTestEntities();

        canvasView = new CanvasView(mockController);

        BorderPane root = new BorderPane(canvasView);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();

        doAnswer(invocation -> {
            EntityPlacement placement = invocation.getArgument(1);

            ImageView imageView = new ImageView();
            imageView.setX(placement.getX());
            imageView.setY(placement.getY());
            imageView.setFitWidth(32);
            imageView.setFitHeight(32);

            return imageView;
        }).when(canvasView).createImageViewForEntity(anyString(), any(EntityPlacement.class));

        canvasView.reloadFromPlacements(testEntities);

        WaitForAsyncUtils.waitForFxEvents();
    }

    private void setupTestEntities() {
        testEntities = new ArrayList<>();

        entity1X = 2 * TILE_SIZE;
        entity1Y = 2 * TILE_SIZE;
        testEntity1 = createTestEntityPlacement(entity1X, entity1Y);
        testEntities.add(testEntity1);

        entity2X = 5 * TILE_SIZE;
        entity2Y = 3 * TILE_SIZE;
        testEntity2 = createTestEntityPlacement(entity2X, entity2Y);
        testEntities.add(testEntity2);
    }

    private EntityPlacement createTestEntityPlacement(double x, double y) {
        EntityType testType = mock(EntityType.class);

        EntityPlacement placement = mock(EntityPlacement.class);
        when(placement.getType()).thenReturn(testType);
        when(placement.getX()).thenReturn(x);
        when(placement.getY()).thenReturn(y);

        when(placement.getEntityImagePath()).thenReturn(DEFAULT_IMAGE_PATH);

        return placement;
    }

    /**
     * Test that clicking on an entity selects it and shows the selection highlight
     */
    @Test
    public void testSelectEntity(FxRobot robot) {
        robot.moveTo(entity1X + 16, entity1Y + 16)
                .clickOn(MouseButton.PRIMARY);

        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(canvasView.getSelectionHighlightVisible(),
                "Selection highlight should be visible after clicking on entity");

        assertEquals(entity1X, canvasView.getSelectionHighlightX(),
                "Selection highlight X should match entity position");
        assertEquals(entity1Y, canvasView.getSelectionHighlightY(),
                "Selection highlight Y should match entity position");
    }

    /**
     * Test that dragging an entity moves it to a new position
     */
    @Test
    public void testDragEntity(FxRobot robot) {
        double targetX = entity1X + (2 * TILE_SIZE);
        double targetY = entity1Y + (1 * TILE_SIZE);

        robot.moveTo(entity1X + 16, entity1Y + 16)
                .press(MouseButton.PRIMARY)
                .moveTo(targetX + 16, targetY + 16)
                .release(MouseButton.PRIMARY);

        WaitForAsyncUtils.waitForFxEvents();

        verify(mockController).moveEntity(eq(testEntity1), eq(targetX), eq(targetY));

        assertEquals(targetX, canvasView.getSelectionHighlightX(),
                "Selection highlight X should be at the new position");
        assertEquals(targetY, canvasView.getSelectionHighlightY(),
                "Selection highlight Y should be at the new position");
    }

    /**
     * Test that dragging an entity to an invalid position (outside grid) is blocked
     */
    @Test
    public void testDragEntityOutsideGrid(FxRobot robot) {
        double outsideX = -40; // Negative position
        double outsideY = 40;

        robot.moveTo(entity1X + 16, entity1Y + 16)
                .press(MouseButton.PRIMARY)
                .moveTo(outsideX, outsideY)
                .release(MouseButton.PRIMARY);

        WaitForAsyncUtils.waitForFxEvents();

        verify(mockController, never()).moveEntity(any(), eq(outsideX), eq(outsideY));

        assertEquals(entity1X, canvasView.getSelectionHighlightX(),
                "Selection highlight X should revert to original position");
        assertEquals(entity1Y, canvasView.getSelectionHighlightY(),
                "Selection highlight Y should revert to original position");
    }

    /**
     * Test that dragging an entity to an occupied cell is prevented
     */
    @Test
    public void testDragEntityToOccupiedCell(FxRobot robot) {
        robot.moveTo(entity1X + 16, entity1Y + 16)
                .press(MouseButton.PRIMARY)
                .moveTo(entity2X + 16, entity2Y + 16)
                .release(MouseButton.PRIMARY);

        WaitForAsyncUtils.waitForFxEvents();

        verify(mockController, never()).moveEntity(eq(testEntity1), eq(entity2X), eq(entity2Y));

        assertEquals(entity1X, canvasView.getSelectionHighlightX(),
                "Selection highlight X should revert to original position");
        assertEquals(entity1Y, canvasView.getSelectionHighlightY(),
                "Selection highlight Y should revert to original position");
    }

    /**
     * Test that clicking outside entities deselects the current entity
     */
    @Test
    public void testDeselectEntity(FxRobot robot) {
        robot.moveTo(entity1X + 16, entity1Y + 16)
                .clickOn(MouseButton.PRIMARY);

        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(canvasView.getSelectionHighlightVisible(),
                "Selection highlight should be visible after selecting entity");

        robot.moveTo(10 * TILE_SIZE, 10 * TILE_SIZE)
                .clickOn(MouseButton.PRIMARY);

        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(canvasView.getSelectionHighlightVisible(),
                "Selection highlight should remain visible after deselection");

        assertFalse(canvasView.hasSelectedEntity(),
                "Entity should no longer be selected after clicking empty space");
    }

    /**
     * Test that reloading placements clears and repopulates the canvas
     */
    @Test
    public void testReloadPlacements(FxRobot robot) {
        List<EntityPlacement> newPlacements = List.of(testEntity1);

        canvasView.reloadFromPlacements(newPlacements);

        WaitForAsyncUtils.waitForFxEvents();

        robot.moveTo(entity2X + 16, entity2Y + 16)
                .clickOn(MouseButton.PRIMARY);

        WaitForAsyncUtils.waitForFxEvents();

        assertNotEquals(entity2X, canvasView.getSelectionHighlightX(),
                "Entity 2 should not be selectable after reload");
        assertNotEquals(entity2Y, canvasView.getSelectionHighlightY(),
                "Entity 2 should not be selectable after reload");
    }
}