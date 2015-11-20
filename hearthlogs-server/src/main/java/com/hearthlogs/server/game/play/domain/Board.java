package com.hearthlogs.server.game.play.domain;

import com.hearthlogs.server.game.parse.domain.Card;
import com.hearthlogs.server.game.parse.domain.Player;
import com.hearthlogs.server.game.parse.domain.Zone;

import java.util.ArrayList;
import java.util.List;

public class Board implements Action {

    private List<Card> friendlyHand = new ArrayList<>();
    private List<Card> friendlySecret = new ArrayList<>();
    private List<Card> friendlyPlay = new ArrayList<>();
    private List<Card> friendlyWeapon = new ArrayList<>();

    private List<Card> opposingHand = new ArrayList<>();
    private List<Card> opposingSecret = new ArrayList<>();
    private List<Card> opposingPlay = new ArrayList<>();
    private List<Card> opposingWeapon = new ArrayList<>();

    public Board(List<Card> cards, Player friendly) {
        for (Card c: cards) {
            if (Zone.HAND.eq(c.getZone())) {
                if (c.getController().equals(friendly.getController())) {
                    friendlyHand.add(c);
                } else {
                    opposingHand.add(c);
                }
            } else if (Zone.PLAY.eq(c.getZone()) && Card.Type.MINION.eq(c.getCardtype())) {
                if (c.getController().equals(friendly.getController())) {
                    friendlyPlay.add(c);
                } else {
                    opposingPlay.add(c);
                }
            } else if (Zone.SECRET.eq(c.getZone()) && Card.Type.SPELL.eq(c.getCardtype())) {
                if (c.getController().equals(friendly.getController())) {
                    friendlySecret.add(c);
                } else {
                    opposingSecret.add(c);
                }
            } else if (Zone.PLAY.eq(c.getZone()) && Card.Type.WEAPON.eq(c.getCardtype())) {
                if (c.getController().equals(friendly.getController())) {
                    friendlyWeapon.add(c);
                } else {
                    opposingWeapon.add(c);
                }
            }
        }
    }

    public List<Card> getFriendlyHand() {
        return friendlyHand;
    }

    public void setFriendlyHand(List<Card> friendlyHand) {
        this.friendlyHand = friendlyHand;
    }

    public List<Card> getFriendlySecret() {
        return friendlySecret;
    }

    public void setFriendlySecret(List<Card> friendlySecret) {
        this.friendlySecret = friendlySecret;
    }

    public List<Card> getFriendlyPlay() {
        return friendlyPlay;
    }

    public void setFriendlyPlay(List<Card> friendlyPlay) {
        this.friendlyPlay = friendlyPlay;
    }

    public List<Card> getFriendlyWeapon() {
        return friendlyWeapon;
    }

    public void setFriendlyWeapon(List<Card> friendlyWeapon) {
        this.friendlyWeapon = friendlyWeapon;
    }

    public List<Card> getOpposingHand() {
        return opposingHand;
    }

    public void setOpposingHand(List<Card> opposingHand) {
        this.opposingHand = opposingHand;
    }

    public List<Card> getOpposingSecret() {
        return opposingSecret;
    }

    public void setOpposingSecret(List<Card> opposingSecret) {
        this.opposingSecret = opposingSecret;
    }

    public List<Card> getOpposingPlay() {
        return opposingPlay;
    }

    public void setOpposingPlay(List<Card> opposingPlay) {
        this.opposingPlay = opposingPlay;
    }

    public List<Card> getOpposingWeapon() {
        return opposingWeapon;
    }

    public void setOpposingWeapon(List<Card> opposingWeapon) {
        this.opposingWeapon = opposingWeapon;
    }
}
