package oogasalad.authoring.view.util;

import java.util.Map;

@FunctionalInterface
public interface ParameterInputFactory {
  InputField createInput(String paramName, Class<?> paramType, Map<String, InputField> allInputs);
}
