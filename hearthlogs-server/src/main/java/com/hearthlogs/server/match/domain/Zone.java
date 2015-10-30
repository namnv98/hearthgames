package com.hearthlogs.server.match.domain;

/**
 *  Zones (tag = ZONE)
 *
 *     PLAY                - The play area where minions live.  There are actually 2 PLAY ZONEs, 1 for each player so we need to know which players turn it is.
 *     HAND                - The area where a players cards are kept.  Same as play area, there are 2 zones.
 *     SECRET              - The zone where secrets live. 2 zones for this as well.
 *     GRAVEYARD           - Where minions go when they die or when spells are used. I would assume that there would be 2 zones for each player for this as well.
 *     REMOVEDFROMGAME     - temporary cards like enchantments created from other cards are removed from the match.
 *     SETASIDE            - temporary cards created. Example: Enchantments created by Emperor Thaurissan are attached to cards to reduce their mana cost.  They seem to be put in play but dont occupy a zone position.
 */
public enum Zone {

    PLAY,
    HAND,
    DECK,
    GRAVEYARD,
    REMOVEDFROMGAME,
    SETASIDE;

    public boolean eq(String zone) {
        return this.toString().equals(zone);
    }
}
