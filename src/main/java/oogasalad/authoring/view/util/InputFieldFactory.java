package oogasalad.authoring.view.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class InputFieldFactory {

  private final Map<String, ParameterInputFactory> byName = new HashMap<>();
  private final Map<Class<?>, ParameterInputFactory> byType = new HashMap<>();

  private final Supplier<List<String>> typeSupplier;
  private final Function<String, List<String>> modeSupplierForType;

  public InputFieldFactory(Supplier<List<String>> typeSupplier,
      Function<String, List<String>> modeSupplierForType) {
    this.typeSupplier = typeSupplier;
    this.modeSupplierForType = modeSupplierForType;
    registerDefaults();
  }

  private void registerDefaults() {
    registerByName("type", (param, paramType, inputs) -> {
      ComboBox<String> combo = new ComboBox<>();
      combo.getItems().addAll(typeSupplier.get());
      if (!combo.getItems().isEmpty()) {
        combo.setValue(combo.getItems().get(0));
      }
      return new InputField(combo, () -> combo.getValue());
    });

    registerByName("newMode", (param, paramType, inputs) -> {
      ComboBox<String> combo = new ComboBox<>();

      Runnable updateOptions = () -> {
        InputField typeField = inputs.get("type");
        String typeName = typeField != null ? typeField.getValue() : null;
        List<String> modes = typeName != null ? modeSupplierForType.apply(typeName) : List.of();
        combo.getItems().setAll(modes);
        if (!modes.isEmpty()) {
          combo.setValue(modes.get(0));
        }
      };

      InputField typeField = inputs.get("type");
      if (typeField != null && typeField.getNode() instanceof ComboBox typeBox) {
        ((ComboBox<?>) typeBox).valueProperty().addListener((obs, oldVal, newVal) -> updateOptions.run());
      }

      updateOptions.run();
      return new InputField(combo, () -> combo.getValue());
    });

    registerByType(int.class, (param, paramType, inputs) -> defaultTextField());
    registerByType(double.class, (param, paramType, inputs) -> defaultTextField());
    registerByType(String.class, (param, paramType, inputs) -> defaultTextField());
  }

  private static InputField defaultTextField() {
    TextField tf = new TextField();
    return new InputField(tf, tf::getText);
  }

  public void registerByName(String name, ParameterInputFactory factory) {
    byName.put(name, factory);
  }

  public void registerByType(Class<?> type, ParameterInputFactory factory) {
    byType.put(type, factory);
  }

  public InputField create(String name, Class<?> type, Map<String, InputField> allInputs) {
    if (byName.containsKey(name)) return byName.get(name).createInput(name, type, allInputs);
    return byType.getOrDefault(type, (n, t, m) -> defaultTextField()).createInput(name, type, allInputs);
  }
}
