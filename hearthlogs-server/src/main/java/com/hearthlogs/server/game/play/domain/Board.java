package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.CardWrapper;
import com.hearthlogs.server.game.parse.domain.Player;
import com.hearthlogs.server.game.parse.domain.Zone;

import java.util.ArrayList;
import java.util.List;

public class Board implements Action {

    private List<CardWrapper> friendlyHand = new ArrayList<>();
    private List<CardWrapper> friendlySecret = new ArrayList<>();
    private List<CardWrapper> friendlyPlay = new ArrayList<>();
    private List<CardWrapper> friendlyWeapon = new ArrayList<>();

    private List<CardWrapper> opposingHand = new ArrayList<>();
    private List<CardWrapper> opposingSecret = new ArrayList<>();
    private List<CardWrapper> opposingPlay = new ArrayList<>();
    private List<CardWrapper> opposingWeapon = new ArrayList<>();

    public Board(List<Card> cards, Player friendly) {
        for (Card c: cards) {
            if (Zone.HAND.eq(c.getZone())) {
                if (c.getController().equals(friendly.getController())) {
                    friendlyHand.add(new CardWrapper(c));
                } else {
                    opposingHand.add(new CardWrapper(c));
                }
            } else if (Zone.PLAY.eq(c.getZone()) && Card.Type.MINION.eq(c.getCardtype())) {
                if (c.getController().equals(friendly.getController())) {
                    friendlyPlay.add(new CardWrapper(c));
                } else {
                    opposingPlay.add(new CardWrapper(c));
                }
            } else if (Zone.SECRET.eq(c.getZone()) && Card.Type.SPELL.eq(c.getCardtype())) {
                if (c.getController().equals(friendly.getController())) {
                    friendlySecret.add(new CardWrapper(c));
                } else {
                    opposingSecret.add(new CardWrapper(c));
                }
            } else if (Zone.PLAY.eq(c.getZone()) && Card.Type.WEAPON.eq(c.getCardtype())) {
                if (c.getController().equals(friendly.getController())) {
                    friendlyWeapon.add(new CardWrapper(c));
                } else {
                    opposingWeapon.add(new CardWrapper(c));
                }
            }
        }
    }

    public List<CardWrapper> getFriendlyHand() {
        return friendlyHand;
    }

    public void setFriendlyHand(List<CardWrapper> friendlyHand) {
        this.friendlyHand = friendlyHand;
    }

    public List<CardWrapper> getFriendlySecret() {
        return friendlySecret;
    }

    public void setFriendlySecret(List<CardWrapper> friendlySecret) {
        this.friendlySecret = friendlySecret;
    }

    public List<CardWrapper> getFriendlyPlay() {
        return friendlyPlay;
    }

    public void setFriendlyPlay(List<CardWrapper> friendlyPlay) {
        this.friendlyPlay = friendlyPlay;
    }

    public List<CardWrapper> getFriendlyWeapon() {
        return friendlyWeapon;
    }

    public void setFriendlyWeapon(List<CardWrapper> friendlyWeapon) {
        this.friendlyWeapon = friendlyWeapon;
    }

    public List<CardWrapper> getOpposingHand() {
        return opposingHand;
    }

    public void setOpposingHand(List<CardWrapper> opposingHand) {
        this.opposingHand = opposingHand;
    }

    public List<CardWrapper> getOpposingSecret() {
        return opposingSecret;
    }

    public void setOpposingSecret(List<CardWrapper> opposingSecret) {
        this.opposingSecret = opposingSecret;
    }

    public List<CardWrapper> getOpposingPlay() {
        return opposingPlay;
    }

    public void setOpposingPlay(List<CardWrapper> opposingPlay) {
        this.opposingPlay = opposingPlay;
    }

    public List<CardWrapper> getOpposingWeapon() {
        return opposingWeapon;
    }

    public void setOpposingWeapon(List<CardWrapper> opposingWeapon) {
        this.opposingWeapon = opposingWeapon;
    }

    @Override
    public int getType() {
        return 4;
    }
}
