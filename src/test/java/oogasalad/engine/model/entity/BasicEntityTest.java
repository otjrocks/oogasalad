package oogasalad.engine.model.entity;

import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicEntityTest {

    private static final double TOLERANCE = 1e-6;

    private BasicEntity basicEntity;

    @BeforeEach
    void setUp() {
        EntityType fruitType = new EntityType("fruit", "wall", null, null, null, null);
        EntityPlacement fruitPlacement = new EntityPlacement(fruitType, 5, 5, "Default");
        basicEntity = new BasicEntity(fruitPlacement);
    }

    @Test
    void getEntityPlacement_getXandYCoordinates_returnXandYCoordinatesCorrectly() {
        assertEquals(5, basicEntity.getEntityPlacement().getX(), TOLERANCE);
        assertEquals(5, basicEntity.getEntityPlacement().getY(), TOLERANCE);
    }

    @Test
    void getAndSetEntityDirection_ChangeAndGetEntityDirectionMultipleTimes_CorrectlyChangesEachTime() {
        basicEntity.setEntityDirection('U');
        assertEquals('U', basicEntity.getEntityDirection());

        basicEntity.setEntityDirection('R');
        assertEquals('R', basicEntity.getEntityDirection());
    }

    @Test
    void setEntityDirection_setDirectionToUp_correctlyShowsUpDirection() {
        basicEntity.setEntityDirection('U');
        assertEquals(0.0, basicEntity.getDx(), TOLERANCE);
        assertEquals(-Entity.ENTITY_SPEED, basicEntity.getDy(), TOLERANCE);
    }

    @Test
    void setEntityDirection_setDirectionToDown_correctlyShowsDownDirection() {
        basicEntity.setEntityDirection('D');
        assertEquals(0.0, basicEntity.getDx(), TOLERANCE);
        assertEquals(Entity.ENTITY_SPEED, basicEntity.getDy(), TOLERANCE);
    }

    @Test
    void setEntityDirection_setDirectionToLeft_correctlyShowsLeftDirection() {
        basicEntity.setEntityDirection('L');
        assertEquals(-Entity.ENTITY_SPEED, basicEntity.getDx(), TOLERANCE);
        assertEquals(0.0, basicEntity.getDy(), TOLERANCE);
    }

    @Test
    void setEntityDirection_setDirectionToRight_correctlyShowsRightDirection() {
        basicEntity.setEntityDirection('R');
        assertEquals(Entity.ENTITY_SPEED, basicEntity.getDx(), TOLERANCE);
        assertEquals(0.0, basicEntity.getDy(), TOLERANCE);
    }

    @Test
    void setEntityDirection_setDirectionToNoDirection_velocityEqualsZero() {
        basicEntity.setEntityDirection(' ');
        assertEquals(0.0, basicEntity.getDx(), TOLERANCE);
        assertEquals(0.0, basicEntity.getDy(), TOLERANCE);
    }

    @Test
    void setDxAndSetDy_changeEachValue_returnsUpdatedValues() {
        basicEntity.setDx(1.25);
        basicEntity.setDy(-0.75);
        assertEquals(1.25, basicEntity.getDx(), TOLERANCE);
        assertEquals(-0.75, basicEntity.getDy(), TOLERANCE);
    }

    @Test
    void canMove_setEntityCloseToIntegerRight_returnsTrue() {
        basicEntity.getEntityPlacement().setY(5.01);
        assertTrue(basicEntity.canMove('R'));
    }

    @Test
    void canMove_setEntityFarFromIntegerRight_returnsFalse() {
        basicEntity.getEntityPlacement().setY(5.6);
        assertFalse(basicEntity.canMove('R'));
    }

    @Test
    void canMove_setEntityCloseToIntegerUp_returnsTrue() {
        basicEntity.getEntityPlacement().setX(3.01);
        assertTrue(basicEntity.canMove('U'));
    }

    @Test
    void canMove_setEntityFarFromIntegerUp_returnsFalse() {
        basicEntity.getEntityPlacement().setX(3.6);
        assertFalse(basicEntity.canMove('U'));
    }

    @Test
    void update_callUpdate_shouldNotDoAnything() {
        double originalX = basicEntity.getEntityPlacement().getX();
        double originalY = basicEntity.getEntityPlacement().getY();
        basicEntity.update();
        assertEquals(originalX, basicEntity.getEntityPlacement().getX(), TOLERANCE);
        assertEquals(originalY, basicEntity.getEntityPlacement().getY(), TOLERANCE);
    }
}
