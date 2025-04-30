package oogasalad.authoring.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javafx.scene.layout.Pane;
import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.authoring.view.EntityPlacementView;
import oogasalad.authoring.view.EntitySelectorView;
import oogasalad.authoring.view.EntityTypeEditorView;
import oogasalad.authoring.view.canvas.CanvasView;
import oogasalad.authoring.view.mainView.AuthoringView;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.records.model.EntityTypeRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AuthoringControllerTest {

  // ChatGPT wrote many of the tests and backbone for these tests.
  private AuthoringController controller;

  @Mock
  private AuthoringModel mockModel;
  @Mock
  private AuthoringView mockView;
  @Mock
  private MainController mockMainController;
  @Mock
  private CanvasView mockCanvasView;
  @Mock
  private EntitySelectorView mockEntitySelectorView;
  @Mock
  private EntityTypeEditorView mockEntityEditorView;
  @Mock
  private EntityPlacementView mockEntityPlacementView;
  @Mock
  private LevelDraft mockLevelDraft;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(mockView.getCanvasView()).thenReturn(mockCanvasView);
    when(mockView.getEntitySelectorView()).thenReturn(mockEntitySelectorView);
    when(mockEntityEditorView.getRoot()).thenReturn(new Pane());
    when(mockView.getEntityEditorView()).thenReturn(mockEntityEditorView);
    when(mockView.getEntityPlacementView()).thenReturn(mockEntityPlacementView);
    when(mockModel.getCurrentLevel()).thenReturn(mockLevelDraft);

    controller = new AuthoringController(mockModel, mockView, mockMainController);
  }

  @Test
  void createNewEntityType_whenCalled_addsNewEntityTypeAndUpdatesSelector() {
    controller.createNewEntityType();
    verify(mockModel).addEntityType(any(EntityTypeRecord.class));
    verify(mockEntitySelectorView).updateEntities(anyList());
  }

  @Test
  void selectEntityType_existingEntityType_setsEntityEditorAndHighlightsTile() {
    String typeName = "TestEntity";
    EntityTypeRecord typeRecord = new EntityTypeRecord(typeName, null, List.of());

    when(mockModel.findEntityType(typeName)).thenReturn(Optional.of(typeRecord));
    Pane mockRoot = new Pane();
    when(mockEntityEditorView.getRoot()).thenReturn(mockRoot);

    controller.selectEntityType(typeName);

    verify(mockEntityEditorView).setEntityType(typeRecord);
    assertTrue(mockRoot.isVisible());
    verify(mockEntitySelectorView).highlightEntityTile(typeName);
    verify(mockEntityPlacementView).setVisible(false);
  }

  @Test
  void selectEntityType_nonExistentEntityType_clearsEntityEditorAndHidesEditor() {
    String typeName = "MissingEntity";

    when(mockModel.findEntityType(typeName)).thenReturn(Optional.empty());
    Pane mockRoot = new Pane();
    when(mockEntityEditorView.getRoot()).thenReturn(mockRoot);

    controller.selectEntityType(typeName);

    verify(mockEntityEditorView).setEntityType(null);
    assertFalse(mockRoot.isVisible());
  }

  @Test
  void placeEntity_validEntityType_placesEntityOnCanvas() {
    String typeName = "PlaceableEntity";
    EntityTypeRecord typeRecord = new EntityTypeRecord(typeName, null, List.of());
    EntityPlacement mockPlacement = mock(EntityPlacement.class);

    when(mockModel.findEntityType(typeName)).thenReturn(Optional.of(typeRecord));
    when(mockLevelDraft.createAndAddEntityPlacement(eq(typeRecord), anyDouble(), anyDouble()))
        .thenReturn(mockPlacement);

    controller.placeEntity(typeName, 100, 200);

    verify(mockCanvasView).placeEntity(mockPlacement);
  }

  @Test
  void moveEntity_validPlacement_movesPlacement() {
    EntityPlacement mockPlacement = mock(EntityPlacement.class);

    controller.moveEntity(mockPlacement, 50, 75);

    verify(mockPlacement).moveTo(50, 75);
  }

  @Test
  void moveEntity_nullPlacement_doesNothing() {
    // Should do nothing (no exception)
    controller.moveEntity(null, 50, 75);
  }

  @Test
  void updateEntitySelector_whenCalled_updatesEntitySelectorView() {
    controller.updateEntitySelector();
    verify(mockEntitySelectorView).updateEntities(anyList());
  }

  @Test
  void updateCanvas_whenCalled_reloadPlacementsInCanvas() {
    EntityPlacement placement1 = mock(EntityPlacement.class);
    EntityPlacement placement2 = mock(EntityPlacement.class);

    when(mockLevelDraft.getEntityPlacements()).thenReturn(List.of(placement1, placement2));

    controller.updateCanvas();

    verify(mockCanvasView).reloadFromPlacements(List.of(placement1, placement2));
  }

  @Test
  void selectEntityPlacement_nonNullPlacement_showsPlacementEditor() {
    EntityPlacement placement = mock(EntityPlacement.class);
    Pane mockRoot = new Pane();
    when(mockEntityEditorView.getRoot()).thenReturn(mockRoot);

    controller.selectEntityPlacement(placement);

    verify(mockEntityPlacementView).setEntityPlacement(placement);
    assertFalse(mockRoot.isVisible());
    verify(mockEntityPlacementView).setVisible(true);
  }

  @Test
  void selectEntityPlacement_nullPlacement_hidesPlacementEditor() {
    controller.selectEntityPlacement(null);
    verify(mockEntityPlacementView).setVisible(false);
  }

  @Test
  void deleteEntityType_existingType_removesPlacementsAndUpdatesSelector() {
    String typeName = "EntityToDelete";
    when(mockModel.deleteEntityType(typeName)).thenReturn(true);

    controller.deleteEntityType(typeName);

    verify(mockCanvasView).removeAllPlacementsOfType(typeName);
    verify(mockEntitySelectorView).updateEntities(anyList());
  }

  @Test
  void testLoadFromFile_ensureLoadUpdatesModel_Success() throws URISyntaxException {
    AuthoringModel myModel = new AuthoringModel();
    controller = new AuthoringController(myModel, mockView, mockMainController);
    File configFile = new File(getClass().getResource("/BasicPacMan/gameConfig.json").toURI());
    assertDoesNotThrow(() -> controller.loadProject(configFile));
    assertEquals("Advanced Ghost AI Demo", controller.getModel().getGameTitle());
    assertEquals("CS308 Team", controller.getModel().getAuthor());
    assertEquals("Shows different controlTypes with strategyConfig and collision/mode logic.",
        controller.getModel().getGameDescription());

  }

}
