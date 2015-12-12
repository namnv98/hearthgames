package com.hearthlogs.server.game.play.domain;

public class EndOfTurn implements Action {

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
