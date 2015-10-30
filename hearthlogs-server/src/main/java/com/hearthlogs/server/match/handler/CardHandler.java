package com.hearthlogs.server.match.handler;

import com.hearthlogs.server.match.domain.Card;
import com.hearthlogs.server.match.domain.CardDetails;
import com.hearthlogs.server.match.domain.Player;
import com.hearthlogs.server.match.domain.Zone;
import com.hearthlogs.server.match.domain.Activity;
import com.hearthlogs.server.match.MatchContext;
import com.hearthlogs.server.match.result.MatchResult;
import org.springframework.stereotype.Component;

@Component
public class CardHandler extends ActivityHandler {

    public void handleNewCard(MatchResult result, MatchContext context, Activity activity) {
        if (activity.getEntity() != null && activity.getEntity() instanceof Card && context.getGame().isGameRunning()) {
            if (!activity.getParent().isJoust()) {
                Card created = (Card) activity.getEntity();

                if (created.getController() != null && created.getCreator() != null) {
                    Player beneficiary;
                    Card creator;
                    if (context.getFriendlyPlayer().getController().equals(created.getController())) {
                        beneficiary = context.getFriendlyPlayer();
                    } else {
                        beneficiary = context.getOpposingPlayer();
                    }
                    creator = (Card) context.getEntityById(created.getCreator());
                    result.getCurrentTurn().addCardCreation(beneficiary, creator, created);

                    System.out.println(creator.getCardDetails().getName() + " has created : " + created.getCardDetails().getName() + " for the benefit of " + beneficiary.getName());
                }
            }
        }
    }

    public void handleTagChange(MatchResult result, MatchContext context, Activity activity, Player player, Card before, Card after) {

        if (after.getZone() != null) {
            if (Zone.HAND.eq(before.getZone()) && Zone.DECK.eq(after.getZone())) {

                if ("DEALING".equals(player.getMulliganState())) {
                    if (player == context.getFriendlyPlayer()) {
                        result.mulliganFriendlyCard(before);
                    } else {
                        result.mulliganOpposingCard(before);
                    }
                    System.out.println(player.getName() + " has mulliganed " + getName(before));
                }

            }
            if (Zone.DECK.eq(before.getZone()) && Zone.HAND.eq(after.getZone())) {

                if (context.getGame().isMulliganOver()) {
                    result.getCurrentTurn().addCardDrawn(player, before);
                    System.out.println(player.getName() + " has drawn. " + getName(before) + ", id="+before.getEntityId());
                } else {
                    if (player == context.getFriendlyPlayer()) {
                        if (context.getStartingCardIds().contains(before.getEntityId())) {  // Only add cards that were in the starting deck
                            result.addFriendlyStartingCard(before);
                            System.out.println(player.getName() + " has drawn+ " + getName(before) + ", id="+before.getEntityId());
                        }
                    } else {
                        if (context.getStartingCardIds().contains(before.getEntityId())) {
                            result.addOpposingStartingCard(before);
                            System.out.println(player.getName() + " has drawn- " + getName(before) + ", id="+before.getEntityId());
                        }
                    }
                }



            }
//            else if (!Zone.HAND.eq(before.getZone()) && Zone.HAND.eq(after.getZone())) {
//                System.out.println(player.getName() + " has " + getName(before) + " from " + before.getZone());
//            }

//            if (Zone.SETASIDE.eq(before.getZone()) )

            if (Zone.HAND.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.Type.MINION.eq(before.getCardtype())) {
                result.getCurrentTurn().addCardPlayed(player, before);
                System.out.println(player.getName() + " has played " + getName(before));
            }

            if (Zone.PLAY.eq(before.getZone()) && Zone.GRAVEYARD.eq(after.getZone()) && Card.Type.MINION.eq(before.getCardtype())) {
                Card card = (Card) context.getEntityById(before.getLastAffectedBy());
                if (card != null) {
                    CardDetails cardDetails = card.getCardDetails();
                    result.getCurrentTurn().addKill(card, before);
                    System.out.println(cardDetails.getName() + " has killed " + getName(before));
                }
//                else {
//                    System.out.println(getName(before) + " has died.");
//                }
            }
        }

        if (after.getToBeDestroyed() != null && TRUE_OR_ONE.equals(after.getToBeDestroyed())) {
            Card card = (Card) context.getEntityById(activity.getEntityId());
            if (card != null) {
                Activity parent = activity.getParent();
                if (parent != null) {
                    Card parentCard = (Card) context.getEntityById(parent.getEntityId());
                    if (parentCard != null) {
                        CardDetails parentCardDetails = parentCard.getCardDetails();
                        CardDetails cardDetails = card.getCardDetails();
                        if (parentCardDetails != null && cardDetails != null) {
                            result.getCurrentTurn().addKill(parentCard, card);

                            System.out.println(parentCardDetails.getName() + " has destroyed " + cardDetails.getName());
                        }
                    }

                }

            }
        }

        if (after.getDamage() != null && !FALSE_OR_ZERO.equals(after.getDamage())) {
            int health = Integer.parseInt(before.getCardDetails().getHealth());
            int damage = Integer.parseInt(after.getDamage());
            int newHealth = health - damage;

            result.getCurrentTurn().addHealthChange(before, newHealth);
            System.out.println(before.getCardDetails().getName() + " health is now : " + (health - damage));
        }

        if (after.getPredamage() != null && !FALSE_OR_ZERO.equals(after.getPredamage())) {
            Card attacker = (Card) activity.getParent().getEntity();
            Card defender = (Card) activity.getParent().getTarget();

            int damage = Integer.parseInt(after.getPredamage());
            if (before == attacker) {
                if (defender == null) {
                    result.getCurrentTurn().addDamage(attacker, before, damage);
                    System.out.println(attacker.getCardDetails().getName() + " has done " + damage + " damage to " + before.getCardDetails().getName());
                } else {
                    result.getCurrentTurn().addDamage(defender, before, damage);
                    System.out.println(defender.getCardDetails().getName() + " has done " + damage + " damage to " + before.getCardDetails().getName());
                }
            } else if (before == defender) {
                result.getCurrentTurn().addDamage(attacker, before, damage);
                System.out.println(attacker.getCardDetails().getName() + " has done " + damage + " damage to " + before.getCardDetails().getName());
            } else {
                result.getCurrentTurn().addDamage(attacker, before, damage);
                System.out.println(attacker.getCardDetails().getName() + " has done " + damage + " damage to " + before.getCardDetails().getName());
            }
        }

    }
}