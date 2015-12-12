package com.hearthgames.server.game.play.domain;

import java.io.Serializable;

public class NumOptions implements Action, Serializable {

    private int number;

    public NumOptions(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
