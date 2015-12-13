package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.play.domain.json.NumOptionsSerializer;

import java.io.Serializable;

@JsonSerialize(using = NumOptionsSerializer.class)
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
