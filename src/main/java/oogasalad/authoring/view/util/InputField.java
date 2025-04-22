package oogasalad.authoring.view.util;

import java.util.function.Supplier;
import javafx.scene.Node;

public class InputField {
  private final Node node;
  private final Supplier<String> extractor;

  public InputField(Node node, Supplier<String> extractor) {
    this.node = node;
    this.extractor = extractor;
  }

  public Node getNode() {
    return node;
  }

  public String getValue() {
    return extractor.get();
  }
}
