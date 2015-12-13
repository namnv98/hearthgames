package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.parse.domain.Card;
import com.hearthgames.server.game.play.domain.json.ManaSavedSerializer;

import java.io.Serializable;

@JsonSerialize(using = ManaSavedSerializer.class)
public class ManaSaved implements Action, Serializable {

    private Card card;
    private int manaSaved;

    public ManaSaved(Card card, int manaSaved) {
        this.card = card;
        this.manaSaved = manaSaved;
    }

    public int getManaSaved() {
        return manaSaved;
    }

    public void setManaSaved(int manaSaved) {
        this.manaSaved = manaSaved;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

}


