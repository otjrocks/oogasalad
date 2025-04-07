package oogasalad.authoring.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;

public class LevelDraft {
  private String name;
  private String outputFileName;
  private int width;
  private int height;
  private int edgePolicy;

  private List<EntityPlacement> entityPlacements;

  public LevelDraft(String name, String outputFileName) {
    this.name = name;
    this.outputFileName = outputFileName;
    this.entityPlacements = new ArrayList<>();
  }


  public boolean addEntityPlacement(EntityPlacement placement) {
    return placement != null && entityPlacements.add(placement);
  }

  public boolean removeEntityPlacement(EntityPlacement placement) {
    return entityPlacements.remove(placement);
  }

  public EntityPlacement createAndAddEntityPlacement(EntityType type, double x, double y) {
    if (type == null) return null;
    EntityPlacement placement = new EntityPlacement(type, x, y, "Default");
    entityPlacements.add(placement);
    return placement;
  }

  public Optional<EntityPlacement> findEntityPlacementAt(double x, double y, double threshold) {
    return entityPlacements.stream()
        .filter(p -> {
          double dx = p.getX() - x;
          double dy = p.getY() - y;
          return Math.sqrt(dx * dx + dy * dy) <= threshold;
        })
        .findFirst();
  }

  public void clearPlacements() {
    entityPlacements.clear();
  }

  public List<EntityPlacement> getEntityPlacements() {
    return Collections.unmodifiableList(entityPlacements);
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOutputFileName() {
    return outputFileName;
  }

  public void setOutputFileName(String outputFileName) {
    this.outputFileName = outputFileName;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getEdgePolicy() {
    return edgePolicy;
  }

  public void setEdgePolicy(int edgePolicy) {
    this.edgePolicy = edgePolicy;
  }
}
