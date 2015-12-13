package com.hearthgames.server.game.play.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hearthgames.server.game.play.domain.json.LoggingActionSerializer;

import java.io.Serializable;

@JsonSerialize(using = LoggingActionSerializer.class)
public class LoggingAction implements Action, Serializable {

    private String msg;

    public LoggingAction(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
