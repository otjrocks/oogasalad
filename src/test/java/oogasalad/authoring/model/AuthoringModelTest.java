package oogasalad.authoring.model;

import oogasalad.engine.model.EntityData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

class AuthoringModelTest {

    private AuthoringModel model;
    private EntityData mockTemplate1;
    private EntityData mockTemplate2;

    @BeforeEach
    public void setUp() {
        model = new AuthoringModel();

        mockTemplate1 = mock(EntityData.class);
        when(mockTemplate1.getType()).thenReturn("Player");

        mockTemplate2 = mock(EntityData.class);
        when(mockTemplate2.getType()).thenReturn("Enemy");
    }

    @Test
    public void testAddEntityTemplate() {
        assertTrue(model.addEntityTemplate(mockTemplate1), "Should return true when adding a valid template");
        assertEquals(1, model.getEntityTemplates().size(), "Template list should have one element");
        assertEquals(mockTemplate1, model.getEntityTemplates().get(0), "Added template should be in the list");

        assertFalse(model.addEntityTemplate(null), "Should return false when adding null");
    }

    @Test
    public void testRemoveEntityTemplate() {
        model.addEntityTemplate(mockTemplate1);
        model.addEntityTemplate(mockTemplate2);
        assertEquals(2, model.getEntityTemplates().size(), "Should have two templates");

        assertTrue(model.removeEntityTemplate(mockTemplate1), "Should return true when removing existing template");
        assertEquals(1, model.getEntityTemplates().size(), "Should have one template left");
        assertEquals(mockTemplate2, model.getEntityTemplates().get(0), "Remaining template should be mockTemplate2");

        EntityData nonExistentTemplate = mock(EntityData.class);
        assertFalse(model.removeEntityTemplate(nonExistentTemplate), "Should return false when template not found");
    }

    @Test
    public void testUpdateEntityTemplate() {
        model.addEntityTemplate(mockTemplate1);

        EntityData newTemplate = mock(EntityData.class);
        when(newTemplate.getType()).thenReturn("UpdatedPlayer");

        assertTrue(model.updateEntityTemplate(mockTemplate1, newTemplate), "Should return true on successful update");
        assertEquals(1, model.getEntityTemplates().size(), "Should still have one template");
        assertEquals(newTemplate, model.getEntityTemplates().get(0), "Template should be updated to new one");

        EntityData nonExistentTemplate = mock(EntityData.class);
        assertFalse(model.updateEntityTemplate(nonExistentTemplate, newTemplate), "Should return false when template not found");
    }

    @Test
    public void testUpdateEntityTemplateAffectsPlacements() {
        model.addEntityTemplate(mockTemplate1);
        EntityPlacement placement = model.createAndAddEntityPlacement(mockTemplate1, 10, 20);

        EntityData newTemplate = mock(EntityData.class);
        when(newTemplate.getType()).thenReturn("UpdatedPlayer");

        model.updateEntityTemplate(mockTemplate1, newTemplate);

        assertEquals(newTemplate, placement.getEntityData(), "Placement should reference the new template");
    }

    @Test
    public void testFindEntityTemplateByType() {
        model.addEntityTemplate(mockTemplate1);
        model.addEntityTemplate(mockTemplate2);

        Optional<EntityData> result = model.findEntityTemplateByType("Player");
        assertTrue(result.isPresent(), "Should find existing template");
        assertEquals(mockTemplate1, result.get(), "Should return the correct template");

        result = model.findEntityTemplateByType("NonExistent");
        assertFalse(result.isPresent(), "Should return empty Optional for non-existent template");
    }

    @Test
    public void testAddEntityPlacement() {
        EntityPlacement placement = new EntityPlacement(mockTemplate1, 10, 20);

        assertTrue(model.addEntityPlacement(placement), "Should return true when adding valid placement");
        assertEquals(1, model.getEntityPlacements().size(), "Should have one placement");
        assertEquals(placement, model.getEntityPlacements().get(0), "Added placement should be in the list");

        assertFalse(model.addEntityPlacement(null), "Should return false when adding null");
    }

    @Test
    public void testCreateAndAddEntityPlacement() {
        EntityPlacement placement = model.createAndAddEntityPlacement(mockTemplate1, 15, 25);

        assertNotNull(placement, "Should return a non-null placement");
        assertEquals(1, model.getEntityPlacements().size(), "Should have one placement");
        assertEquals(mockTemplate1, placement.getEntityData(), "Placement should have the correct template");
        assertEquals(15, placement.getX(), "Placement should have the correct X coordinate");
        assertEquals(25, placement.getY(), "Placement should have the correct Y coordinate");

        assertNull(model.createAndAddEntityPlacement(null, 30, 40), "Should return null for null template");
        assertEquals(1, model.getEntityPlacements().size(), "Placement list size should not change");
    }

    @Test
    public void testRemoveEntityPlacement() {
        EntityPlacement placement1 = model.createAndAddEntityPlacement(mockTemplate1, 10, 20);
        EntityPlacement placement2 = model.createAndAddEntityPlacement(mockTemplate2, 30, 40);
        assertEquals(2, model.getEntityPlacements().size(), "Should have two placements");

        assertTrue(model.removeEntityPlacement(placement1), "Should return true when removing existing placement");
        assertEquals(1, model.getEntityPlacements().size(), "Should have one placement left");
        assertEquals(placement2, model.getEntityPlacements().get(0), "Remaining placement should be placement2");

        EntityPlacement nonExistentPlacement = new EntityPlacement(mockTemplate1, 50, 60);
        assertFalse(model.removeEntityPlacement(nonExistentPlacement), "Should return false when placement not found");
    }

    @Test
    public void testFindEntityPlacementAt() {
        EntityPlacement placement1 = model.createAndAddEntityPlacement(mockTemplate1, 100, 200);
        EntityPlacement placement2 = model.createAndAddEntityPlacement(mockTemplate2, 300, 400);

        Optional<EntityPlacement> result = model.findEntityPlacementAt(100, 200, 0.1);
        assertTrue(result.isPresent(), "Should find exact match");
        assertEquals(placement1, result.get(), "Should return correct placement");

        result = model.findEntityPlacementAt(102, 198, 5);
        assertTrue(result.isPresent(), "Should find placement within threshold");
        assertEquals(placement1, result.get(), "Should return correct placement");

        result = model.findEntityPlacementAt(110, 210, 5);
        assertFalse(result.isPresent(), "Should not find placement outside threshold");

        EntityPlacement placement3 = model.createAndAddEntityPlacement(mockTemplate1, 101, 201);
        result = model.findEntityPlacementAt(100, 200, 5);
        assertTrue(result.isPresent(), "Should find a placement");
        assertEquals(placement1, result.get(), "Should return the first matching placement");
    }

    @Test
    public void testClearEntityPlacements() {
        model.addEntityTemplate(mockTemplate1);
        model.createAndAddEntityPlacement(mockTemplate1, 10, 20);
        model.createAndAddEntityPlacement(mockTemplate1, 30, 40);
        assertEquals(2, model.getEntityPlacements().size(), "Should have two placements");

        model.clearEntityPlacements();
        assertTrue(model.getEntityPlacements().isEmpty(), "Placement list should be empty");
        assertFalse(model.getEntityTemplates().isEmpty(), "Template list should still have elements");
    }

    @Test
    public void testClearAll() {
        model.addEntityTemplate(mockTemplate1);
        model.addEntityTemplate(mockTemplate2);
        model.createAndAddEntityPlacement(mockTemplate1, 10, 20);

        model.clearAll();
        assertTrue(model.getEntityTemplates().isEmpty(), "Template list should be empty");
        assertTrue(model.getEntityPlacements().isEmpty(), "Placement list should be empty");
    }

    @Test
    public void testGettersReturnUnmodifiableCollections() {
        model.addEntityTemplate(mockTemplate1);
        model.createAndAddEntityPlacement(mockTemplate1, 10, 20);

        List<EntityData> templates = model.getEntityTemplates();
        List<EntityPlacement> placements = model.getEntityPlacements();

        assertThrows(UnsupportedOperationException.class, () -> {
            templates.add(mockTemplate2);
        }, "Should throw exception when trying to modify templates list");

        assertThrows(UnsupportedOperationException.class, () -> {
            placements.clear();
        }, "Should throw exception when trying to modify placements list");
    }

}