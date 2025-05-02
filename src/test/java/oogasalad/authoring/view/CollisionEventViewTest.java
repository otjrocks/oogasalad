package oogasalad.authoring.view;

import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.Scene;
import javafx.stage.Stage;
import oogasalad.engine.records.config.model.collisionevent.ConsumeCollisionEventRecord;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

class CollisionEventViewTest extends DukeApplicationTest {

  private CollisionEventView collisionEventView;

  public void start(Stage stage) {
    collisionEventView = new CollisionEventView("CollisionEventViewTest");
    Scene scene = new Scene(collisionEventView.getRoot(), 600, 400);
    stage.setScene(scene);
    stage.show();
  }

  @Test
  void getCollisionEvent_SelectConsume_ReturnConsumeCollisionEvent() {
    clickOn("#collision-rule-selector");
    clickOn("Consume");
    assertEquals(new ConsumeCollisionEventRecord(), collisionEventView.getCollisionEvent());
  }
}