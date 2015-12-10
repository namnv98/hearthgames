package com.hearthlogs.server.game.play.domain.board;

import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;
import com.hearthlogs.server.game.parse.domain.Zone;
import com.hearthlogs.server.game.play.GameResult;
import com.hearthlogs.server.game.play.domain.Action;
import com.hearthlogs.server.game.play.domain.Turn;

import static com.hearthlogs.server.util.HeroStatsUtil.*;

import java.util.ArrayList;
import java.util.List;

public class Board implements Action {

    public static final String TRUE = "1";
    public static final String LEGENDARY = "Legendary";

    private TurnData turnData = new TurnData();

    private Hero friendlyHero = new Hero();
    private Hero opposingHero = new Hero();

    private List<String> actions = new ArrayList<>();

    public Board(GameResult result, GameContext context) {

        actions.addAll(result.getActionLogs());
        result.getActionLogs().clear();

        Board previousBoard = result.getCurrentTurn().findLastBoard();
        if (previousBoard != null) {
            turnData.setBoard(previousBoard.getTurnData().getBoard()+1);
        } else {
            turnData.setBoard(1);
        }
        turnData.setTurn(result.getCurrentTurn().getTurnNumber());

        setHeroIds(context, friendlyHero, opposingHero);
        setHeroHealthArmor(result, context, friendlyHero, opposingHero);
        setHeroMana(result, context, friendlyHero, opposingHero);
        setHeroPowerStatus(context, friendlyHero, opposingHero);
        setHeroCards(context, friendlyHero, opposingHero);
    }

    public Hero getFriendlyHero() {
        return friendlyHero;
    }

    public Hero getOpposingHero() {
        return opposingHero;
    }

    public void addAction(String action) {
        actions.add(action);
    }

    public List<String> getActions() {
        return actions;
    }


    public TurnData getTurnData() {
        return turnData;
    }

    @Override
    public int getType() {
        return 4;
    }

    private void setHeroIds(GameContext context, Hero friendlyHero, Hero opposingHero) {
        friendlyHero.setId(getHeroId(context.getFriendlyPlayer(), context));
        opposingHero.setId(getHeroId(context.getOpposingPlayer(), context));
    }

    private void setHeroHealthArmor(GameResult result, GameContext context, Hero friendlyHero, Hero opposingHero) {

        Integer friendlyHealth;
        Integer opposingHealth;
        Integer friendlyArmor;
        Integer opposingArmor;

        Turn currentTurn = result.getCurrentTurn();
        Turn previousTurn = result.getTurnBefore(currentTurn);
        Board previousBoard = currentTurn.findLastBoard();

        if (result.getCurrentTurn().getTurnNumber() == 1 && previousBoard == null) {
            friendlyHealth = getCurrentHealth(context.getFriendlyPlayer(), context);
            opposingHealth = getCurrentHealth(context.getOpposingPlayer(), context);
            friendlyArmor = getCurrentArmor(context.getFriendlyPlayer(), context);
            opposingArmor = getCurrentArmor(context.getOpposingPlayer(), context);
        } else {
            if (previousBoard == null) {
                previousBoard = previousTurn.findLastBoard();;
            }
            friendlyHealth = previousBoard.getFriendlyHero().getHealth();
            friendlyArmor = previousBoard.getFriendlyHero().getArmor();
            opposingHealth = previousBoard.getOpposingHero().getHealth();
            opposingArmor = previousBoard.getOpposingHero().getArmor();
        }


        if (hasHealthChanged(context.getFriendlyPlayer(), result.getCurrentTurn().getActions(), this)) {
            friendlyHealth = getHealth(context.getFriendlyPlayer(), result.getCurrentTurn().getActions(), this);
        }
        if (hasArmorChanged(context.getFriendlyPlayer(), result.getCurrentTurn().getActions(), this)) {
            friendlyArmor = getArmor(context.getFriendlyPlayer(), result.getCurrentTurn().getActions(), this);
        }
        if (hasHealthChanged(context.getOpposingPlayer(), result.getCurrentTurn().getActions(), this)) {
            opposingHealth = getHealth(context.getOpposingPlayer(), result.getCurrentTurn().getActions(), this);
        }
        if (hasArmorChanged(context.getOpposingPlayer(), result.getCurrentTurn().getActions(), this)) {
            opposingArmor = getArmor(context.getOpposingPlayer(), result.getCurrentTurn().getActions(), this);
        }

        friendlyHero.setHealth(friendlyHealth);
        friendlyHero.setArmor(friendlyArmor);

        opposingHero.setHealth(opposingHealth);
        opposingHero.setArmor(opposingArmor);
    }

    private void setHeroMana(GameResult result, GameContext context, Hero friendlyHero, Hero opposingHero) {

        Integer friendlyManaGained = getManaGained(context.getFriendlyPlayer(), result.getCurrentTurn(), this);
        Integer friendlyManaUsed = getManaUsed(context.getFriendlyPlayer(), result.getCurrentTurn(), this);
        friendlyHero.setMana(friendlyManaGained-friendlyManaUsed);
        friendlyHero.setManaTotal(friendlyManaGained);

        Integer opposingManaGained = getManaGained(context.getOpposingPlayer(), result.getCurrentTurn(), this);
        Integer opposingManaUsed = getManaUsed(context.getOpposingPlayer(), result.getCurrentTurn(), this);
        opposingHero.setMana(opposingManaGained-opposingManaUsed);
        opposingHero.setManaTotal(opposingManaGained);
    }

    private void setHeroPowerStatus(GameContext context, Hero friendlyHero, Hero opposingHero) {

        Player friendlyPlayer = context.getFriendlyPlayer();
        Player opposingPlayer = context.getOpposingPlayer();

        List<Card> cards = context.getCards();
        for (Card card: cards) {
            if (Card.Type.HERO_POWER.eq(card.getCardtype()) && Zone.PLAY.eq(card.getZone()) && card.getController().equals(friendlyPlayer.getController())) {
                friendlyHero.setPowerId(card.getCardid());
                friendlyHero.setPowerUsed(TRUE.equals(card.getExhausted()));
            }
            if (Card.Type.HERO_POWER.eq(card.getCardtype()) && Zone.PLAY.eq(card.getZone()) && card.getController().equals(opposingPlayer.getController())) {
                opposingHero.setPowerId(card.getCardid());
                opposingHero.setPowerUsed(TRUE.equals(card.getExhausted()));
            }
        }
    }

    private void setHeroCards(GameContext context, Hero friendlyHero, Hero opposingHero) {

        for (Card c: context.getCards()) {
            if (Zone.HAND.eq(c.getZone())) {
                CardInHand cardInHand = new CardInHand();
                cardInHand.setHealth(c.getHealth() != null ? Integer.parseInt(c.getHealth()) : 0);
                cardInHand.setAttack(c.getAtk() != null ? Integer.parseInt(c.getAtk()) : 0);
                cardInHand.setCost(c.getCost() != null ? Integer.parseInt(c.getCost()) : 0);
                cardInHand.setId(c.getCardDetails() != null ? c.getCardDetails().getId() : "cardback");
                if (c.getController().equals(context.getFriendlyPlayer().getController())) {
                    friendlyHero.getCardsInHand().add(cardInHand);
                } else {
                    opposingHero.getCardsInHand().add(cardInHand);
                }
            } else if (Zone.PLAY.eq(c.getZone()) && Card.Type.MINION.eq(c.getCardtype())) {
                MinionInPlay minionInPlay = new MinionInPlay();
                minionInPlay.setId(c.getCardid());
                int health = c.getHealth() != null ? Integer.parseInt(c.getHealth()) : 0;
                if (c.getDamage() != null) {
                    health = health - Integer.parseInt(c.getDamage());
                    minionInPlay.setDamaged(true);
                } else if (c.getPredamage() != null) {
                    health = health - Integer.parseInt(c.getPredamage());
                    minionInPlay.setDamaged(true);
                }
                minionInPlay.setHealth(health);
                minionInPlay.setAttack(c.getAtk() != null ? Integer.parseInt(c.getAtk()) : 0);
                minionInPlay.setFrozen(c.getFrozen() != null && TRUE.equals(c.getFrozen()));
                minionInPlay.setLegendary(LEGENDARY.equals(c.getCardDetails().getRarity()));
                minionInPlay.setShielded(TRUE.equals(c.getDivineShield()));
                minionInPlay.setTaunting(TRUE.equals(c.getTaunt()));
                minionInPlay.setExhausted(TRUE.equals(c.getExhausted()));
                if (c.getController().equals(context.getFriendlyPlayer().getController())) {
                    friendlyHero.getMinionsInPlay().add(minionInPlay);
                } else {
                    opposingHero.getMinionsInPlay().add(minionInPlay);
                }
                if (c.getCardDetails().getMechanics() != null && c.getCardDetails().getMechanics().contains("Inspire")) {
                    minionInPlay.setIcon("inspire");
                } else if (TRUE.equals(c.getTriggerVisual())) {
                    minionInPlay.setIcon("trigger");
                } else if (c.getCardDetails().getMechanics() != null && c.getCardDetails().getMechanics().contains("Deathrattle")) {
                    minionInPlay.setIcon("deathrattle");
                }

            } else if (Zone.SECRET.eq(c.getZone())) {
                CardInSecret cardInSecret = new CardInSecret();
                cardInSecret.setCardClass(c.getCardClass().toLowerCase());
                cardInSecret.setId(c.getCardDetails().getId());
                if (c.getController().equals(context.getFriendlyPlayer().getController())) {
                    friendlyHero.getCardsInSecret().add(cardInSecret);
                } else {
                    opposingHero.getCardsInSecret().add(cardInSecret);
                }
            } else if (Zone.PLAY.eq(c.getZone()) && Card.Type.WEAPON.eq(c.getCardtype())) {
                Weapon weapon = new Weapon();
                weapon.setId(c.getCardid());
                weapon.setAttack(c.getAtk() != null ? Integer.parseInt(c.getAtk()) : 0);
                int durability = Integer.parseInt(c.getCardDetails().getDurability());
                int damage = c.getDamage() != null ? Integer.parseInt(c.getDamage()) : 0;

                weapon.setDurability(durability - damage);
                if (c.getController().equals(context.getFriendlyPlayer().getController())) {
                    friendlyHero.setWeapon(weapon);
                } else {
                    opposingHero.setWeapon(weapon);
                }
            }
        }

    }

}
