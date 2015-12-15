package com.hearthgames.utils.hearthpwn;

public class CardLink {

    private String href;
    private String cardId;
    private String name;
    private String hearthPwnId;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHearthPwnId() {
        return hearthPwnId;
    }

    public void setHearthPwnId(String hearthPwnId) {
        this.hearthPwnId = hearthPwnId;
    }

    @Override
    public String toString() {
        return "{ \"cardId\": \"" + cardId + "\", \"name\": \"" + name + "\", \"href\": \"" + href + "\", \"hearthPwnId\": \"" + hearthPwnId + "\" }";
    }
}
