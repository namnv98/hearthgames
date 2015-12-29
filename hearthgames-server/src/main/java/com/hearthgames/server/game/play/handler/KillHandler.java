package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.*;
import com.hearthgames.server.game.play.GameResult;

public class KillHandler implements Handler {

    public static final String DOOMSAYER = "NEW1_021";

    @Override
    public boolean supports(GameResult result, GameContext context, Activity activity) {
        return activity.isTagChange() && activity.getDelta() instanceof Card && context.getAfter(activity).getZone() != null;
    }

    @Override
    public boolean handle(GameResult result, GameContext context, Activity activity) {
        Card before = context.getBefore(activity);
        Card after = context.getAfter(activity);

        if (Zone.PLAY.eq(before.getZone()) && Zone.GRAVEYARD.eq(after.getZone()) && Card.Type.MINION.eq(before.getCardtype())) {
            Card card = (Card) context.getEntityById(before.getLastAffectedBy());
            if (card != null) {
                boolean favorableTrade = false;
                boolean evenTrade = false;

                boolean killerFriendly = false;
                if (card.getController().equals(context.getFriendlyPlayer().getController())) {
                    killerFriendly = true;
                }

                if (card.getController().equals(before.getController())) { // is the same player affecting his own card

                } else {
                    int killedHealth = Integer.parseInt(before.getHealth()) - Integer.parseInt(before.getDamage());
                    if (killedHealth <= 0) {
                        if (Card.Type.MINION.eq(card.getCardtype())) {
                            int killerHealth = Integer.parseInt(card.getHealth()) - Integer.parseInt(card.getDamage() != null ? card.getDamage() : "0");
                            if (killerHealth > 0) {
                                // if a friendly minion killed an opposing minion and the friendly minion is still alive then its considered a favorable trade
                                favorableTrade = true;
                            } else if (killerHealth <= 0) {
                                int killerCost = card.getCardDetailsCost();
                                int killedCost = before.getCardDetailsCost();
                                if (killerCost < killedCost) {
                                    // if a friendly minion killed an opposing minion and they both died but the cost of the opposing minion is higher its considered a favorable trade
                                    favorableTrade = true;
                                } else if (killerCost == killedCost) {
                                    evenTrade = true;
                                }
                            }
                        } else if (Card.Type.SPELL.eq(card.getCardtype())) {
                            int killerCost = card.getCardDetailsCost();
                            int killedCost = before.getCardDetailsCost();
                            if (killerCost < killedCost) {
                                // if a friendly spell killed an opposing minion and the cost of the opposing minion is higher its considered a favorable trade
                                favorableTrade = true;
                            } else if (killerCost == killedCost) {
                                evenTrade = true;
                            }
                        } else if (Card.Type.HERO_POWER.eq(card.getCardtype())) {
                            favorableTrade = true;
                        }
                    }
                }
                Player killerController = context.getPlayer(card);
                Player killedController = context.getPlayer(before);
                result.addKill("killed", killerController, killedController, killerFriendly ? context.getFriendlyPlayer() : context.getOpposingPlayer(), card, before, favorableTrade, evenTrade);

                return true;
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

                            boolean favorableTrade = false;
                            boolean evenTrade = false;

                            boolean killerFriendly = false;
                            if (parentCard.getController().equals(context.getFriendlyPlayer().getController())) {
                                killerFriendly = true;
                            }

                            if (parentCard.getController().equals(card.getController())) { // is the same player affecting his own card
                                if (DOOMSAYER.equals(parentCard.getCardid())) {
                                    evenTrade = true; // we don't want to mark this a favorable or poor.
                                }
                            } else {
                                if (DOOMSAYER.equals(parentCard.getCardid())) {
                                    // because doomsayer destroys all cards in play we will consider all kills favorable.  (an issue might arise where it only kills 1 minion and its cost is 1, then thats not favorable)
                                    favorableTrade = true;
                                } else {
                                    // if a friendly card (minion,spell,etc...) destroyed an opposing minion
                                    int killerCost = parentCard.getCardDetailsCost();
                                    int killedCost = card.getCardDetailsCost();
                                    if (killerCost > killedCost) {
                                        favorableTrade = true;
                                    } else if (killerCost == killedCost) {
                                        evenTrade = true;
                                    }
                                }
                            }
                            Player killerController = context.getPlayer(parentCard);
                            Player killedController = context.getPlayer(card);
                            result.addKill("destroyed", killerController, killedController, killerFriendly ? context.getFriendlyPlayer() : context.getOpposingPlayer(), parentCard, card, favorableTrade, evenTrade);

                            return true;
                        }
                    }

                }

            }
        }
        return false;
    }
}
