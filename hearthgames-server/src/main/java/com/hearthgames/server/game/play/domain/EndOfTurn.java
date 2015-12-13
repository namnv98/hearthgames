package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.play.domain.json.EndofTurnSerializer;

import java.io.Serializable;

@JsonSerialize(using = EndofTurnSerializer.class)
public class EndOfTurn implements Action, Serializable {

    String msg;

    public EndOfTurn() {
        msg = "eot";
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
