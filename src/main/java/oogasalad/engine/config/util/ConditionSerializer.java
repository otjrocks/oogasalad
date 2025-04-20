package oogasalad.engine.config.util;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.lang.reflect.RecordComponent;

public class ConditionSerializer {

  public static JsonNode serialize(Object condition, ObjectMapper mapper) {
    ObjectNode root = mapper.createObjectNode();
    String typeName = condition.getClass().getAnnotation(JsonSubTypes.Type.class) != null
        ? condition.getClass().getAnnotation(JsonSubTypes.Type.class).name()
        : condition.getClass().getSimpleName().replace("ConditionRecord", "");

    root.put("type", typeName);

    ObjectNode parameters = mapper.createObjectNode();
    for (RecordComponent comp : condition.getClass().getRecordComponents()) {
      try {
        Object value = comp.getAccessor().invoke(condition);
        if (value instanceof Integer i) parameters.put(comp.getName(), i);
        else if (value instanceof Double d) parameters.put(comp.getName(), d);
        else if (value instanceof Boolean b) parameters.put(comp.getName(), b);
        else parameters.putPOJO(comp.getName(), value);
      } catch (Exception e) {
        throw new RuntimeException("Failed to serialize condition parameter: " + comp.getName(), e);
      }
    }

    root.set("parameters", parameters);
    return root;
  }
}
