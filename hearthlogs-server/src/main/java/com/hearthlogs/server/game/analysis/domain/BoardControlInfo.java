package com.hearthlogs.server.game.analysis.domain;

import com.hearthlogs.server.game.analysis.domain.generic.GenericRow;

public class BoardControlInfo {

    private GenericRow header;
    private GenericRow friendly;
    private GenericRow opposing;

    public GenericRow getHeader() {
        return header;
    }

    public void setHeader(GenericRow header) {
        this.header = header;
    }

    public GenericRow getFriendly() {
        return friendly;
    }

    public void setFriendly(GenericRow friendly) {
        this.friendly = friendly;
    }

    public GenericRow getOpposing() {
        return opposing;
    }

    public void setOpposing(GenericRow opposing) {
        this.opposing = opposing;
    }
}
