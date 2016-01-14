package com.hearthgames.server.game.play.handler;

import com.hearthgames.server.game.parse.domain.*;
import com.hearthgames.server.game.play.PlayContext;

public class KillHandler implements Handler {

    public static final String DOOMSAYER = "NEW1_021";

    @Override
    public boolean supports(PlayContext playContext) {
        return playContext.getActivity().isTagChange() && playContext.getActivity().getDelta() instanceof Card && playContext.getContext().getAfter(playContext.getActivity()).getZone() != null;
    }

    @Override
    public boolean handle(PlayContext playContext) {
        Card before = playContext.getContext().getBefore(playContext.getActivity());
        Card after = playContext.getContext().getAfter(playContext.getActivity());

        if (Zone.PLAY.eq(before.getZone()) && Zone.GRAVEYARD.eq(after.getZone()) && Card.Type.MINION.eq(before.getCardtype())) {
            Card card = playContext.getContext().getCardByEntityId(before.getLastAffectedBy());
            if (card != null) {
                boolean favorableTrade = false;
                boolean evenTrade = false;

                boolean killerFriendly = false;
                if (card.getController().equals(playContext.getContext().getFriendlyPlayer().getController())) {
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
                Player killerController = playContext.getContext().getPlayer(card);
                Player killedController = playContext.getContext().getPlayer(before);
                playContext.addKill("killed", killerController, killedController, killerFriendly ? playContext.getContext().getFriendlyPlayer() : playContext.getContext().getOpposingPlayer(), card, before, favorableTrade, evenTrade);

                return true;
            }
        }

        if (after.getToBeDestroyed() != null && TRUE_OR_ONE.equals(after.getToBeDestroyed())) {
            Card card = playContext.getContext().getCardByEntityId(playContext.getActivity().getEntityId());
            if (card != null) {
                Activity parent = playContext.getActivity().getParent();
                if (parent != null) {
                    Card parentCard = playContext.getContext().getCardByEntityId(parent.getEntityId());
                    if (parentCard != null) {
                        CardDetails parentCardDetails = parentCard.getCardDetails();
                        CardDetails cardDetails = card.getCardDetails();
                        if (parentCardDetails != null && cardDetails != null) {

                            boolean favorableTrade = false;
                            boolean evenTrade = false;

                            boolean killerFriendly = false;
                            if (parentCard.getController().equals(playContext.getContext().getFriendlyPlayer().getController())) {
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
                            Player killerController = playContext.getContext().getPlayer(parentCard);
                            Player killedController = playContext.getContext().getPlayer(card);
                            playContext.addKill("destroyed", killerController, killedController, killerFriendly ? playContext.getContext().getFriendlyPlayer() : playContext.getContext().getOpposingPlayer(), parentCard, card, favorableTrade, evenTrade);

                            return true;
                        }
                    }

                }

            }
        }
        return false;
    }
}
