package oogasalad.authoring.model;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.model.EntityTypeRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LevelDraftTest {

  private LevelDraft level;
  private EntityTypeRecord mockType;

  @BeforeEach
  public void setUp() {
    level = new LevelDraft("Test Level", "test_map.json");

    mockType = mock(EntityTypeRecord.class);
    when(mockType.type()).thenReturn("TestEntity");
  }

  @Test
  public void addEntityPlacement_ValidPlacement_PlacementAdded() {
    EntityPlacement placement = new EntityPlacement(mockType, 10, 20, "Default");
    assertTrue(level.addEntityPlacement(placement));
    assertEquals(1, level.getEntityPlacements().size());
  }

  @Test
  public void addEntityPlacement_NullPlacement_ReturnsFalse() {
    assertFalse(level.addEntityPlacement(null));
    assertEquals(0, level.getEntityPlacements().size());
  }

  @Test
  public void removeEntityPlacement_ExistingPlacement_RemovedSuccessfully() {
    EntityPlacement placement = new EntityPlacement(mockType, 10, 20, "Default");
    level.addEntityPlacement(placement);

    assertTrue(level.removeEntityPlacement(placement));
    assertEquals(0, level.getEntityPlacements().size());
  }

  @Test
  public void removeEntityPlacement_NonexistentPlacement_ReturnsFalse() {
    EntityPlacement placement = new EntityPlacement(mockType, 10, 20, "Default");
    assertFalse(level.removeEntityPlacement(placement));
  }

  @Test
  public void createAndAddEntityPlacement_ValidTemplate_ReturnsPlacement() {
    EntityPlacement placement = level.createAndAddEntityPlacement(mockType, 30, 40);

    assertNotNull(placement);
    assertEquals(mockType, placement.getType());
    assertEquals(30, placement.getX());
    assertEquals(40, placement.getY());
    assertEquals(1, level.getEntityPlacements().size());
  }

  @Test
  public void createAndAddEntityPlacement_NullTemplate_ReturnsNull() {
    EntityPlacement placement = level.createAndAddEntityPlacement(null, 10, 10);
    assertNull(placement);
    assertEquals(0, level.getEntityPlacements().size());
  }

  @Test
  public void findEntityPlacementAt_ExactMatch_FindsPlacement() {
    EntityPlacement placement = level.createAndAddEntityPlacement(mockType, 100, 200);
    Optional<EntityPlacement> result = level.findEntityPlacementAt(100, 200, 0.01);

    assertTrue(result.isPresent());
    assertEquals(placement, result.get());
  }

  @Test
  public void findEntityPlacementAt_WithinThreshold_FindsPlacement() {
    EntityPlacement placement = level.createAndAddEntityPlacement(mockType, 100, 200);
    Optional<EntityPlacement> result = level.findEntityPlacementAt(102, 198, 5);

    assertTrue(result.isPresent());
    assertEquals(placement, result.get());
  }

  @Test
  public void findEntityPlacementAt_OutsideThreshold_ReturnsEmpty() {
    level.createAndAddEntityPlacement(mockType, 100, 200);
    Optional<EntityPlacement> result = level.findEntityPlacementAt(150, 250, 10);

    assertFalse(result.isPresent());
  }

  @Test
  public void clearPlacements_HasPlacements_ClearsAll() {
    level.createAndAddEntityPlacement(mockType, 10, 10);
    level.createAndAddEntityPlacement(mockType, 20, 20);

    level.clearPlacements();
    assertTrue(level.getEntityPlacements().isEmpty());
  }

  @Test
  public void getEntityPlacements_ModifyReturnedList_ThrowsException() {
    level.createAndAddEntityPlacement(mockType, 10, 10);
    List<EntityPlacement> placements = level.getEntityPlacements();

    assertThrows(UnsupportedOperationException.class, () -> {
      placements.add(new EntityPlacement(mockType, 0, 0, "Default"));
    });
  }

  @Test
  public void gettersAndSetters_SetAndGetCorrectValues() {
    level.setName("New Level");
    level.setOutputFileName("new_map.json");
    level.setWidth(15);
    level.setHeight(20);
    level.setEdgePolicy("EDGE");

    assertEquals("New Level", level.getName());
    assertEquals("new_map.json", level.getOutputFileName());
    assertEquals(15, level.getWidth());
    assertEquals(20, level.getHeight());
    assertEquals("EDGE", level.getEdgePolicy());
  }
}
