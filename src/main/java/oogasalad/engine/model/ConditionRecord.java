package oogasalad.engine.model;

import java.util.Map;

public record ConditionRecord(String type, Map<String, Object> parameters) { }
