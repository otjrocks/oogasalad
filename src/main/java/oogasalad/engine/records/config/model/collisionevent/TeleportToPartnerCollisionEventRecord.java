package oogasalad.engine.records.config.model.collisionevent;

import oogasalad.engine.records.config.model.CollisionEventInterface;

/**
 * A record encapsulating information about a teleport to partner collision event.
 *
 * @author Angela Predolac
 */
public record TeleportToPartnerCollisionEventRecord(
    String portalType,
    String partnerType,
    boolean randomPartner,
    double offsetX,
    double offsetY
) implements CollisionEventInterface {

}
