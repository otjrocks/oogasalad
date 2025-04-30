package oogasalad.player.model.strategies.collision;

import oogasalad.engine.records.CollisionContextRecord;
import oogasalad.engine.records.CollisionContextRecord.StrategyAppliesTo;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A collision strategy that teleports an entity from one portal to its partner portal. When an
 * entity collides with a portal, this strategy finds another portal of the matching type and
 * teleports the entity to that location.
 *
 * @author Angela Predolac
 */
public class TeleportToPartnerPortalStrategy implements CollisionStrategyInterface {

  private final String portalType;
  private final String partnerType;
  private final boolean randomPartner;
  private final Random random;
  private final double offsetX;
  private final double offsetY;

  /**
   * Creates a new teleportation strategy for moving between portal pairs.
   *
   * @param portalType    The type of portal entity that triggers teleportation
   * @param partnerType   The type of portal entity to teleport to
   * @param randomPartner If true, selects a random partner portal; otherwise uses the first found
   * @param offsetX       X-axis offset to apply after teleportation (prevents re-entry)
   * @param offsetY       Y-axis offset to apply after teleportation (prevents re-entry)
   */
  public TeleportToPartnerPortalStrategy(String portalType, String partnerType,
      boolean randomPartner,
      double offsetX, double offsetY) {
    this.portalType = portalType;
    this.partnerType = partnerType;
    this.randomPartner = randomPartner;
    this.random = new Random();
    this.offsetX = offsetX;
    this.offsetY = offsetY;
  }

  /**
   * Simplified constructor with default offsets.
   *
   * @param portalType    The type of portal entity that triggers teleportation
   * @param partnerType   The type of portal entity to teleport to
   * @param randomPartner If true, selects a random partner portal; otherwise uses the first found
   */
  public TeleportToPartnerPortalStrategy(String portalType, String partnerType,
      boolean randomPartner) {
    this(portalType, partnerType, randomPartner, 0.0, 0.0);
  }

  @Override
  public void handleCollision(CollisionContextRecord collisionContext) {
    // Determine which entity is being teleported (the one that collided with the portal)
    Entity entityToTeleport = collisionContext.strategyAppliesTo() == StrategyAppliesTo.ENTITY1
        ? collisionContext.entity1()
        : collisionContext.entity2();

    // Determine which entity is the portal
    Entity portalEntity = collisionContext.strategyAppliesTo() == StrategyAppliesTo.ENTITY1
        ? collisionContext.entity2()
        : collisionContext.entity1();

    // Verify that the collision is with the correct portal type
    if (!portalEntity.getEntityPlacement().getTypeString().equals(portalType)) {
      return; // Not the right portal type, do nothing
    }

    // Find all possible destination portals
    List<Entity> partnerPortals = findPartnerPortals(collisionContext.gameMap());

    // If no partner portals found, do nothing
    if (partnerPortals.isEmpty()) {
      return;
    }

    // Select destination portal
    Entity destinationPortal = selectDestinationPortal(partnerPortals);

    // Teleport the entity to the destination portal
    teleportEntity(entityToTeleport, destinationPortal);
  }

  /**
   * Finds all partner portals in the game map.
   *
   * @param gameMap The current game map
   * @return A list of all partner portals found
   */
  private List<Entity> findPartnerPortals(GameMapInterface gameMap) {
    List<Entity> partnerPortals = new ArrayList<>();

    for (Entity entity : gameMap) {
      if (entity.getEntityPlacement().getTypeString().equals(partnerType)) {
        partnerPortals.add(entity);
      }
    }

    return partnerPortals;
  }

  /**
   * Selects a destination portal from the list of available partner portals.
   *
   * @param partnerPortals List of all available partner portals
   * @return The selected destination portal
   */
  private Entity selectDestinationPortal(List<Entity> partnerPortals) {
    if (randomPartner && partnerPortals.size() > 1) {
      // Choose a random portal from the list
      return partnerPortals.get(random.nextInt(partnerPortals.size()));
    } else {
      // Just use the first one
      return partnerPortals.get(0);
    }
  }

  /**
   * Teleports an entity to the destination portal with appropriate offset.
   *
   * @param entityToTeleport  The entity being teleported
   * @param destinationPortal The destination portal entity
   */
  private void teleportEntity(Entity entityToTeleport, Entity destinationPortal) {
    double destX = destinationPortal.getEntityPlacement().getX() + offsetX;
    double destY = destinationPortal.getEntityPlacement().getY() + offsetY;

    // Teleport the entity
    entityToTeleport.getEntityPlacement().setX(destX);
    entityToTeleport.getEntityPlacement().setY(destY);
  }
}
