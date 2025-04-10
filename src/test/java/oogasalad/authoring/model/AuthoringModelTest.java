package oogasalad.authoring.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthoringModelTest {

    private AuthoringModel model;
    private EntityType mockTemplate1;
    private EntityType mockTemplate2;
    private LevelDraft level;

    @BeforeEach
    public void setUp() {
        model = new AuthoringModel();
        level = new LevelDraft("Level 1", "level1_map.json");
        model.addLevel(level);

        mockTemplate1 = mock(EntityType.class);
        when(mockTemplate1.type()).thenReturn("Player");

        mockTemplate2 = mock(EntityType.class);
        when(mockTemplate2.type()).thenReturn("Enemy");
    }

    @Test
    public void addEntityType_NewEntityType_EntityAdded() {
        model.addEntityType(mockTemplate1);
        assertEquals(1, model.getEntityTypes().size());
        assertTrue(model.getEntityTypes().contains(mockTemplate1));
    }

    @Test
    public void findEntityType_EntityExists_ReturnsEntity() {
        model.addEntityType(mockTemplate1);
        Optional<EntityType> result = model.findEntityType("Player");

        assertTrue(result.isPresent());
        assertEquals(mockTemplate1, result.get());
    }

    @Test
    public void findEntityType_EntityMissing_ReturnsEmptyOptional() {
        assertFalse(model.findEntityType("Ghost").isPresent());
    }

    @Test
    public void updateEntityType_EntityExists_PlacementsUpdated() {
        model.addEntityType(mockTemplate1);
        EntityPlacement placement = level.createAndAddEntityPlacement(mockTemplate1, 10, 20);

        EntityType updated = mock(EntityType.class);
        when(updated.type()).thenReturn("Player");

        model.updateEntityType(mockTemplate1.type(), updated);

        assertEquals(updated, placement.getType());
    }

    @Test
    public void addEntityPlacement_ValidPlacement_PlacementAdded() {
        EntityPlacement placement = new EntityPlacement(mockTemplate1, 10, 20, "Default");
        assertTrue(level.addEntityPlacement(placement));
        assertEquals(1, level.getEntityPlacements().size());
    }

    @Test
    public void addEntityPlacement_NullPlacement_ReturnsFalse() {
        assertFalse(level.addEntityPlacement(null));
    }

    @Test
    public void removeEntityPlacement_ExistingPlacement_PlacementRemoved() {
        EntityPlacement placement = level.createAndAddEntityPlacement(mockTemplate1, 10, 20);
        assertTrue(level.removeEntityPlacement(placement));
        assertTrue(level.getEntityPlacements().isEmpty());
    }

    @Test
    public void removeEntityPlacement_NonexistentPlacement_ReturnsFalse() {
        EntityPlacement placement = new EntityPlacement(mockTemplate1, 10, 20, "Default");
        assertFalse(level.removeEntityPlacement(placement));
    }

    @Test
    public void createAndAddEntityPlacement_ValidTemplate_ReturnsPlacement() {
        EntityPlacement placement = level.createAndAddEntityPlacement(mockTemplate1, 15, 25);
        assertNotNull(placement);
        assertEquals(mockTemplate1, placement.getType());
        assertEquals(15, placement.getX());
        assertEquals(25, placement.getY());
    }

    @Test
    public void createAndAddEntityPlacement_NullTemplate_ReturnsNull() {
        EntityPlacement placement = level.createAndAddEntityPlacement(null, 15, 25);
        assertNull(placement);
    }

    @Test
    public void findEntityPlacementAt_ExactMatch_FindsPlacement() {
        EntityPlacement placement = level.createAndAddEntityPlacement(mockTemplate1, 100, 200);
        Optional<EntityPlacement> found = level.findEntityPlacementAt(100, 200, 0.1);

        assertTrue(found.isPresent());
        assertEquals(placement, found.get());
    }

    @Test
    public void findEntityPlacementAt_WithinThreshold_FindsPlacement() {
        EntityPlacement placement = level.createAndAddEntityPlacement(mockTemplate1, 100, 200);
        Optional<EntityPlacement> found = level.findEntityPlacementAt(102, 198, 5);

        assertTrue(found.isPresent());
        assertEquals(placement, found.get());
    }

    @Test
    public void findEntityPlacementAt_OutsideThreshold_ReturnsEmpty() {
        level.createAndAddEntityPlacement(mockTemplate1, 100, 200);
        Optional<EntityPlacement> found = level.findEntityPlacementAt(110, 210, 5);

        assertFalse(found.isPresent());
    }

    @Test
    public void clearPlacements_HasPlacements_ClearsAll() {
        level.createAndAddEntityPlacement(mockTemplate1, 10, 20);
        level.clearPlacements();

        assertTrue(level.getEntityPlacements().isEmpty());
    }

    @Test
    public void clearAll_HasEntitiesAndLevels_ClearsAll() {
        model.addEntityType(mockTemplate1);
        level.createAndAddEntityPlacement(mockTemplate1, 10, 20);

        model.clearAll();
        assertTrue(model.getEntityTypes().isEmpty());
        assertTrue(model.getLevels().isEmpty());
    }

    @Test
    public void getEntityTypes_ModifyReturnedCollection_ThrowsException() {
        model.addEntityType(mockTemplate1);
        Collection<EntityType> types = model.getEntityTypes();

        assertThrows(UnsupportedOperationException.class, types::clear);
    }

    @Test
    public void getEntityPlacements_ModifyReturnedList_ThrowsException() {
        level.createAndAddEntityPlacement(mockTemplate1, 10, 20);
        List<EntityPlacement> placements = level.getEntityPlacements();

        assertThrows(UnsupportedOperationException.class, () -> {
            placements.add(new EntityPlacement(mockTemplate1, 0, 0, "Default"));
        });
    }
}
