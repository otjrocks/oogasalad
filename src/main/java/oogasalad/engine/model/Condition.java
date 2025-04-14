package oogasalad.engine.model;

import java.util.Map;

public record Condition(String type, Map<String, Object> parameters) { }
