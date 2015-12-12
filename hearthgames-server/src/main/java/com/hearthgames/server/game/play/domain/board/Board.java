package com.hearthgames.server.game.play.domain.board;

import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Player;
import com.hearthgames.server.game.parse.domain.Zone;
import com.hearthgames.server.game.play.GameResult;
import com.hearthgames.server.game.play.domain.Action;
import com.hearthgames.server.game.play.domain.Turn;
import com.hearthgames.server.util.HeroStatsUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Board implements Action {

    public static final String TRUE = "1";
    public static final String LEGENDARY = "Legendary";

    private TurnData turnData = new TurnData();

    private Hero friendlyHero = new Hero();
    private Hero opposingHero = new Hero();

    private List<Action> actions = new ArrayList<>();

    public Board(GameResult result, GameContext context) {

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

    public void addAction(Action action) {
        actions.add(action);
    }

    public List<Action> getActions() {
        return actions;
    }

    public TurnData getTurnData() {
        return turnData;
    }

    private void setHeroIds(GameContext context, Hero friendlyHero, Hero opposingHero) {
        friendlyHero.setId(HeroStatsUtil.getHeroId(context.getFriendlyPlayer(), context));
        opposingHero.setId(HeroStatsUtil.getHeroId(context.getOpposingPlayer(), context));
    }

    private void setHeroHealthArmor(GameResult result, GameContext context, Hero friendlyHero, Hero opposingHero) {

        Integer friendlyHealth;
        Integer opposingHealth;
        Integer friendlyArmor;
        Integer opposingArmor;

        Turn currentTurn = result.getCurrentTurn();
        Turn previousTurn = result.getTurnBefore(currentTurn);
        Board previousBoard = currentTurn.findLastBoard();

        if (result.getCurrentTurn().getTurnNumber() == 0 && previousBoard == null) {
            friendlyHealth = HeroStatsUtil.getCurrentHealth(context.getFriendlyPlayer(), context);
            opposingHealth = HeroStatsUtil.getCurrentHealth(context.getOpposingPlayer(), context);
            friendlyArmor = HeroStatsUtil.getCurrentArmor(context.getFriendlyPlayer(), context);
            opposingArmor = HeroStatsUtil.getCurrentArmor(context.getOpposingPlayer(), context);
        } else {
            if (previousBoard == null) {
                previousBoard = previousTurn.findLastBoard();
            }
            friendlyHealth = previousBoard.getFriendlyHero().getHealth();
            friendlyArmor = previousBoard.getFriendlyHero().getArmor();
            opposingHealth = previousBoard.getOpposingHero().getHealth();
            opposingArmor = previousBoard.getOpposingHero().getArmor();
        }


        if (HeroStatsUtil.hasHealthChanged(context.getFriendlyPlayer(), result.getCurrentTurn().getActions(), this)) {
            friendlyHealth = HeroStatsUtil.getHealth(context.getFriendlyPlayer(), result.getCurrentTurn().getActions(), this);
        }
        if (HeroStatsUtil.hasArmorChanged(context.getFriendlyPlayer(), result.getCurrentTurn().getActions(), this)) {
            friendlyArmor = HeroStatsUtil.getArmor(context.getFriendlyPlayer(), result.getCurrentTurn().getActions(), this);
        }
        if (HeroStatsUtil.hasHealthChanged(context.getOpposingPlayer(), result.getCurrentTurn().getActions(), this)) {
            opposingHealth = HeroStatsUtil.getHealth(context.getOpposingPlayer(), result.getCurrentTurn().getActions(), this);
        }
        if (HeroStatsUtil.hasArmorChanged(context.getOpposingPlayer(), result.getCurrentTurn().getActions(), this)) {
            opposingArmor = HeroStatsUtil.getArmor(context.getOpposingPlayer(), result.getCurrentTurn().getActions(), this);
        }

        friendlyHero.setHealth(friendlyHealth);
        friendlyHero.setArmor(friendlyArmor);

        opposingHero.setHealth(opposingHealth);
        opposingHero.setArmor(opposingArmor);
    }

    private void setHeroMana(GameResult result, GameContext context, Hero friendlyHero, Hero opposingHero) {

        Integer friendlyManaGained = HeroStatsUtil.getManaGained(context.getFriendlyPlayer(), result.getCurrentTurn(), this);
        Integer friendlyManaUsed = HeroStatsUtil.getManaUsed(context.getFriendlyPlayer(), result.getCurrentTurn(), this);
        friendlyHero.setMana(friendlyManaGained-friendlyManaUsed);
        friendlyHero.setManaTotal(friendlyManaGained);

        Integer opposingManaGained = HeroStatsUtil.getManaGained(context.getOpposingPlayer(), result.getCurrentTurn(), this);
        Integer opposingManaUsed = HeroStatsUtil.getManaUsed(context.getOpposingPlayer(), result.getCurrentTurn(), this);
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
                cardInHand.setPosition(Integer.parseInt(c.getZonePosition()));
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
                minionInPlay.setPosition(Integer.parseInt(c.getZonePosition()));
            } else if (Zone.SECRET.eq(c.getZone())) {
                CardInSecret cardInSecret = new CardInSecret();
                cardInSecret.setCardClass(c.getCardClass().toLowerCase());
                cardInSecret.setId(c.getCardDetails().getId());
                if (c.getController().equals(context.getFriendlyPlayer().getController())) {
                    friendlyHero.getCardsInSecret().add(cardInSecret);
                } else {
                    opposingHero.getCardsInSecret().add(cardInSecret);
                }
                cardInSecret.setPosition(Integer.parseInt(c.getZonePosition()));
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
        CardZonePositionCompartor compartor = new CardZonePositionCompartor();
        friendlyHero.getCardsInHand().sort(compartor);
        friendlyHero.getCardsInSecret().sort(compartor);
        friendlyHero.getMinionsInPlay().sort(compartor);
        opposingHero.getCardsInHand().sort(compartor);
        opposingHero.getCardsInSecret().sort(compartor);
        opposingHero.getMinionsInPlay().sort(compartor);
    }

    private static class CardZonePositionCompartor implements Comparator<CardIn> {

        @Override
        public int compare(CardIn o1, CardIn o2) {
            return o1.getPosition() - o2.getPosition();
        }
    }

}
