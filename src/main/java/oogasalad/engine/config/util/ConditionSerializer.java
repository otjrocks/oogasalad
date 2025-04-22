package oogasalad.engine.config.util;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.lang.reflect.RecordComponent;

/**
 * Utility class for serializing condition records (win/lose/etc.) into JSON format.
 * This class uses reflection to extract record field names and values,
 * and formats them into a structure like:
 *
 * {
 *   "type": "SurviveForTime",
 *   "parameters": {
 *     "amount": 60
 *   }
 * }
 *
 * This avoids instanceof checks and keeps model classes decoupled from serialization.
 *
 * @author Will
 */
public class ConditionSerializer {

  private static final String TYPE = "type";
  private static final String PARAMETERS = "parameters";

  /**
   * Serializes a record-like condition object (e.g., WinConditionInterface) into a JSON node
   * with a "type" and "parameters" block using reflection.
   *
   * @param condition the condition object to serialize
   * @param mapper    Jackson ObjectMapper instance
   * @return a structured JsonNode with "type" and "parameters"
   */
  public static JsonNode serialize(Object condition, ObjectMapper mapper) {
    ObjectNode root = mapper.createObjectNode();
    root.put(TYPE, resolveTypeName(condition));
    root.set(PARAMETERS, extractParameters(condition, mapper));
    return root;
  }

  /**
   * Serialize flat, no parameters object
   * @param condition the condition object to serialize
   * @param mapper Jackson ObjectMapper instance
   * @return a structured JsonNode with "type" and any other additional fields
   */
  public static JsonNode serializeFlat(Object condition, ObjectMapper mapper) {
    ObjectNode root = mapper.createObjectNode();
    root.put(TYPE, resolveTypeName(condition));

    ObjectNode parameters = extractParameters(condition, mapper);
    parameters.fields().forEachRemaining(entry -> root.set(entry.getKey(), entry.getValue()));

    return root;
  }


  private static String resolveTypeName(Object condition) {
    JsonSubTypes.Type annotation = condition.getClass().getAnnotation(JsonSubTypes.Type.class);
    return annotation != null
        ? annotation.name()
        : condition.getClass().getSimpleName().replace("ConditionRecord", "");
  }

  private static ObjectNode extractParameters(Object condition, ObjectMapper mapper) {
    ObjectNode parameters = mapper.createObjectNode();
    for (RecordComponent comp : condition.getClass().getRecordComponents()) {
      try {
        Object value = comp.getAccessor().invoke(condition);
        parameters.set(comp.getName(), mapper.valueToTree(value));
      } catch (ReflectiveOperationException e) {
        throw new RuntimeException("Failed to serialize condition parameter: " + comp.getName(), e);
      }
    }
    return parameters;
  }
}
