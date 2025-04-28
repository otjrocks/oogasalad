package oogasalad.engine.view.components;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

class VMenuTest extends DukeApplicationTest {

  @Test
  void VMenu_Create_Success() {
    List<String> values = List.of("Test");
    List<EventHandler<ActionEvent>> actions = List.of(e -> {
    });
    assertDoesNotThrow(() -> new VMenu(values, actions));
  }

  @Test
  void VMenu_CreateWithInvalidParameters_FailureThrowError() {
    List<String> values = List.of("Test", "Test", "Test");
    List<EventHandler<ActionEvent>> actions = List.of(e -> {
    });
    assertThrows(IllegalArgumentException.class, () -> new VMenu(values, actions));
  }

}