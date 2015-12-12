package com.hearthlogs.server.game.play.domain.board;

public class CardInSecret extends CardIn {

    private String id;
    private String cardClass;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardClass() {
        return cardClass;
    }

    public void setCardClass(String cardClass) {
        this.cardClass = cardClass;
    }
}
