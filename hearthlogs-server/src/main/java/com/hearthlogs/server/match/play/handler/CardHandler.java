package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.domain.*;
import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.play.MatchResult;
import com.hearthlogs.server.match.play.domain.ActionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardHandler extends ActivityHandler {

    public static final String DOOMSAYER = "NEW1_021";

    @Autowired
    public CardHandler(ActionFactory actionFactory) {
        super(actionFactory);
    }

    public void handleNewCard(MatchResult result, ParsedMatch parsedMatch, Activity activity) {
        if (activity.getEntity() != null && activity.getEntity() instanceof Card && parsedMatch.getGame().isGameRunning()) {
            if (!activity.getParent().isJoust()) { // Cards created during the joust are not part of your deck but do represent a copy of card in your deck, they are temporary.
                Card created = (Card) activity.getEntity();

                if (created.getController() != null && created.getCreator() != null) {
                    Player beneficiary;
                    Card creator;
                    if (parsedMatch.getFriendlyPlayer().getController().equals(created.getController())) {
                        beneficiary = parsedMatch.getFriendlyPlayer();
                    } else {
                        beneficiary = parsedMatch.getOpposingPlayer();
                    }
                    creator = (Card) parsedMatch.getEntityById(created.getCreator());
                    result.getCurrentTurn().addCardCreation(beneficiary, creator, created);

                    System.out.println(creator.getCardDetails().getName() + " has created : " + created.getCardDetails().getName());
                }
            }
        }
    }

    public void handleShowEntity(MatchResult result, ParsedMatch parsedMatch, Activity activity, Player player, Card before, Card after) {

        if (after.getZone() != null) {
            if (Zone.HAND.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.Type.MINION.eq(after.getCardtype())) {
                result.getCurrentTurn().addCardPlayed(player, before);
                System.out.println(player.getName() + " has played " + before.getCardDetails().getName());

                if (before.getController().equals(parsedMatch.getFriendlyPlayer().getController())) {
                    result.getCurrentTurn().getFriendlyCardsPutInPlay().add(before);
                } else {
                    result.getCurrentTurn().getOpposingCardsPutInPlay().add(before);
                }
            }

        }

        if (Zone.DECK.eq(before.getZone()) && Zone.HAND.eq(after.getZone())) {

            if (parsedMatch.getGame().isMulliganOver()) {

                result.getCurrentTurn().addCardDrawn(player, before, activity.getParent().getEntity());
                if (activity.getParent() != null && (activity.getParent().isTrigger() || activity.getParent().isPower())) {
                    Entity entity = activity.getParent().getEntity();
                    if (entity instanceof Card) {
                        System.out.println(player.getName() + " has drawn. " + (before.getCardDetails() != null ? before.getCardDetails().getName() : "") + ", id="+before.getEntityId());
                    } else if (entity instanceof Player) {
                        System.out.println(player.getName() + " has drawn. " +  before.getCardDetails().getName() + ", id="+before.getEntityId());
                    }
                }

            } else {
                if (player == parsedMatch.getFriendlyPlayer()) {
                    if (parsedMatch.getStartingCardIds().contains(before.getEntityId())) {  // Only add cards that were in the starting deck
                        result.addFriendlyStartingCard(before);
                        System.out.println(player.getName() + " has drawn+ " +  before.getCardDetails().getName() + ", id="+before.getEntityId());
                    }
                } else {
                    if (parsedMatch.getStartingCardIds().contains(before.getEntityId())) {
                        result.addOpposingStartingCard(before);
                        System.out.println(player.getName() + " has drawn- " +  before.getCardDetails().getName() + ", id="+before.getEntityId());
                    }
                }
            }



        }


    }

    public void handleHideEntity(MatchResult result, ParsedMatch parsedMatch, Activity activity, Player player, Card before, Card after) {
        if (Zone.HAND.eq(before.getZone()) && Zone.DECK.eq(after.getZone())) {

            if ("DEALING".equals(player.getMulliganState())) {
                if (player == parsedMatch.getFriendlyPlayer()) {
                    result.mulliganFriendlyCard(before);
                } else {
                    result.mulliganOpposingCard(before);
                }
                System.out.println(player.getName() + " has mulliganed " + before.getCardDetails().getName());
            }

        }
    }

    public void handleCardTagChange(MatchResult result, ParsedMatch parsedMatch, Activity activity, Player player, Card before, Card after) {

        if (after.getAttached() != null) {
            if (FALSE_OR_ZERO.equals(after.getAttached())) {
                Card detachFrom = (Card) parsedMatch.getEntityById(before.getAttached());
                result.getCurrentTurn().addDetached(before, detachFrom);

                System.out.println("Detached card : " + before.getCardDetails().getName() + " from " + detachFrom.getCardDetails().getName());

            } else {
                Card attachTo = (Card) parsedMatch.getEntityById(after.getAttached());
                result.getCurrentTurn().addAttached(before, attachTo);

                System.out.println("Attach card : " + before.getCardDetails().getName() + " to " + attachTo.getCardDetails().getName() + " : " + before.getCardDetails().getText());
            }


        }

        if (after.getZonePosition() != null) {
            if (parsedMatch.getGame().isMulliganOver()) {
                result.getCurrentTurn().addZonePositionChange(before, Zone.valueOf(before.getZone()), Integer.parseInt(after.getZonePosition()));
            }

//            System.out.println(before.getCardDetails() == null ? "unknown card" : before.getCardDetails().getName() + " has moved to " + before.getZone() + ", position " + after.getZonePosition());
        }

        if (after.getZone() != null) {

            // Has a card come into play
            if (Zone.PLAY.eq(after.getZone()) && !Zone.PLAY.eq(before.getZone()) && Card.Type.MINION.eq(before.getCardtype())) {
                if (before.getController().equals(parsedMatch.getFriendlyPlayer().getController())) {
                    result.getCurrentTurn().getFriendlyCardsPutInPlay().add(before);
                } else {
                    result.getCurrentTurn().getOpposingCardsPutInPlay().add(before);
                }
            } else if (Zone.PLAY.eq(before.getZone()) && !Zone.PLAY.eq(after.getZone()) && Card.Type.MINION.eq(before.getCardtype())) {
                if (before.getController().equals(parsedMatch.getFriendlyPlayer().getController())) {
                    result.getCurrentTurn().getFriendlyCardsRemovedFromPlay().add(before);
                } else {
                    result.getCurrentTurn().getOpposingCardsRemovedFromPlay().add(before);
                }
            }

            if (Zone.HAND.eq(before.getZone()) && Zone.DECK.eq(after.getZone())) {

                if ("DEALING".equals(player.getMulliganState())) {
                    if (player == parsedMatch.getFriendlyPlayer()) {
                        result.mulliganFriendlyCard(before);
                    } else {
                        result.mulliganOpposingCard(before);
                    }
                    System.out.println(player.getName() + " has mulliganed " + before.getCardDetails().getName());
                }

            }

            if (Zone.DECK.eq(before.getZone()) && Zone.HAND.eq(after.getZone())) {

                if (parsedMatch.getGame().isMulliganOver()) {

                    result.getCurrentTurn().addCardDrawn(player, before, activity.getParent().getEntity());
                    if (activity.getParent() != null && (activity.getParent().isTrigger() || activity.getParent().isPower())) {
                        Entity entity = activity.getParent().getEntity();
                        if (entity instanceof Card) {
                            System.out.println(player.getName() + " has drawn. " + (before.getCardDetails() != null ? before.getCardDetails().getName() : "") + ", id="+before.getEntityId());
                        } else if (entity instanceof Player) {
                            System.out.println(player.getName() + " has drawn. " +  before.getCardDetails().getName() + ", id="+before.getEntityId());
                        }
                    }

                } else {
                    if (player == parsedMatch.getFriendlyPlayer()) {
                        if (parsedMatch.getStartingCardIds().contains(before.getEntityId())) {  // Only add cards that were in the starting deck
                            result.addFriendlyStartingCard(before);
                            System.out.println(player.getName() + " has drawn+ " +  before.getCardDetails().getName() + ", id="+before.getEntityId());
                        }
                    } else {
                        if (parsedMatch.getStartingCardIds().contains(before.getEntityId())) {
                            result.addOpposingStartingCard(before);
                            System.out.println(player.getName() + " has drawn- " +  before.getCardDetails().getName() + ", id="+before.getEntityId());
                        }
                    }
                }



            }

            if (Zone.HAND.eq(before.getZone()) && Zone.PLAY.eq(after.getZone()) && Card.Type.MINION.eq(before.getCardtype())) {
                result.getCurrentTurn().addCardPlayed(player, before);
                System.out.println(player.getName() + " has played " + before.getCardDetails().getName());
            }

            if (Zone.PLAY.eq(before.getZone()) && Zone.GRAVEYARD.eq(after.getZone()) && Card.Type.MINION.eq(before.getCardtype())) {
                Card card = (Card) parsedMatch.getEntityById(before.getLastAffectedBy());
                if (card != null) {
                    CardDetails cardDetails = card.getCardDetails();

                    boolean favorableTrade = false;
                    boolean evenTrade = false;

                    boolean killerFriendly = false;
                    if (card.getController().equals(parsedMatch.getFriendlyPlayer().getController())) {
                        killerFriendly = true;
                    }

                    if (card.getController().equals(before.getController())) { // is the same player affecting his own card

                    } else {
                        int killedHealth = Integer.parseInt(before.getHealth()) - Integer.parseInt(before.getDamage());
                        if (killedHealth <= 0) {
                            if (Card.Type.MINION.eq(card.getCardtype())) {
                                int killerHealth = 0;
                                killerHealth = Integer.parseInt(card.getHealth()) - Integer.parseInt(card.getDamage() != null ? card.getDamage() : "0");
                                if (killerHealth > 0) {
                                    // if a friendly minion killed an opposing minion and the friendly minion is still alive then its considered a favorable trade
                                    favorableTrade = true;
                                } else if (killerHealth <= 0) {
                                    int killerCost = Integer.parseInt(card.getCardDetails().getCost());
                                    int killedCost = Integer.parseInt(before.getCardDetails().getCost());
                                    if (killerCost < killedCost) {
                                        // if a friendly minion killed an opposing minion and they both died but the cost of the opposing minion is higher its considered a favorable trade
                                        favorableTrade = true;
                                    } else if (killerCost == killedCost) {
                                        evenTrade = true;
                                    }
                                }
                            } else if (Card.Type.SPELL.eq(card.getCardtype())) {
                                int killerCost = Integer.parseInt(card.getCardDetails().getCost());
                                int killedCost = Integer.parseInt(before.getCardDetails().getCost());
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
                    result.getCurrentTurn().addKill(killerFriendly ? parsedMatch.getFriendlyPlayer() : parsedMatch.getOpposingPlayer(), card, before, favorableTrade, evenTrade);

                    String msg;
                    String favoring = killerFriendly ? " friendly" : " opposing";
                    if (favorableTrade) {
                        msg = " (favorable)" + favoring;
                    } else if (evenTrade) {
                        msg = " (even)" + favoring;
                    } else {
                        msg = " (poor)" + favoring;
                    }
                    System.out.println(cardDetails.getName() + " has killed " + before.getCardDetails().getName() + msg);

                }
//                else {
//                    System.out.println(before.getCardDetails().getName() + " has died.");
//                }
            }
        }

        if (after.getToBeDestroyed() != null && TRUE_OR_ONE.equals(after.getToBeDestroyed())) {
            Card card = (Card) parsedMatch.getEntityById(activity.getEntityId());
            if (card != null) {
                Activity parent = activity.getParent();
                if (parent != null) {
                    Card parentCard = (Card) parsedMatch.getEntityById(parent.getEntityId());
                    if (parentCard != null) {
                        CardDetails parentCardDetails = parentCard.getCardDetails();
                        CardDetails cardDetails = card.getCardDetails();
                        if (parentCardDetails != null && cardDetails != null) {

                            boolean favorableTrade = false;
                            boolean evenTrade = false;

                            boolean killerFriendly = false;
                            if (parentCard.getController().equals(parsedMatch.getFriendlyPlayer().getController())) {
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
                                    int killerCost = Integer.parseInt(parentCard.getCardDetails().getCost());
                                    int killedCost = Integer.parseInt(card.getCardDetails().getCost());
                                    if (killerCost > killedCost) {
                                        favorableTrade = true;
                                    } else if (killerCost == killedCost) {
                                        evenTrade = true;
                                    }
                                }
                            }
                            result.getCurrentTurn().addKill(killerFriendly ? parsedMatch.getFriendlyPlayer() : parsedMatch.getOpposingPlayer(), parentCard, card, favorableTrade, evenTrade);

                            String msg;
                            String favoring = killerFriendly ? " friendly" : " opposing";
                            if (favorableTrade) {
                                msg = " (favorable)" + favoring;
                            } else if (evenTrade) {
                                msg = " (even)" + favoring;
                            } else {
                                msg = " (poor)" + favoring;
                            }
                            System.out.println(parentCardDetails.getName() + " has destroyed " + cardDetails.getName() + msg);
                        }
                    }

                }

            }
        }

        if (after.getHealth() != null && Zone.PLAY.eq(before.getZone())) {
            int newHealth = Integer.parseInt(after.getHealth());
            int currentHealth = Integer.parseInt(before.getHealth() == null ? before.getCardDetails().getHealth() : before.getHealth());

            int diffHealth = newHealth - currentHealth;
            result.getCurrentTurn().addHealthChange(before, diffHealth);

            System.out.println(before.getCardDetails().getName() + " health is now : " + newHealth);
        }

        if (after.getAtk() != null && Zone.PLAY.eq(before.getZone())) {
            int newAttack = Integer.parseInt(after.getAtk());
            int currentAttack = Integer.parseInt(before.getAtk() == null ? before.getCardDetails().getAttack() : before.getAtk());

            int diffAttack = newAttack - currentAttack;
            result.getCurrentTurn().addAttackChange(before, diffAttack);

            System.out.println(before.getCardDetails().getName() + " attack is now : " + newAttack);
        }


        if (after.getDamage() != null && !FALSE_OR_ZERO.equals(after.getDamage())) {
            int health = Integer.parseInt(before.getCardDetails().getHealth());
            int damage = Integer.parseInt(after.getDamage());
            int newHealth = health - damage;

            result.getCurrentTurn().addHeroHealthChange(before, newHealth);
            System.out.println(before.getCardDetails().getName() + " health is now : " + (health - damage));
        }

        if (after.getArmor() != null) {
            int armor = Integer.parseInt(after.getArmor());
            result.getCurrentTurn().addArmorChange(before, armor);
            System.out.println(before.getCardDetails().getName() + " armor is now : " + armor);
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