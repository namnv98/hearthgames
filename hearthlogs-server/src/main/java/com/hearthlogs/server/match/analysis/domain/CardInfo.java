package com.hearthlogs.server.match.analysis.domain;

import com.hearthlogs.server.match.analysis.domain.generic.GenericRow;

public class CardInfo {

    private GenericRow header;
    private GenericRow friendly;
    private GenericRow opposing;

    private String friendlyClass;
    private String opposingClass;

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

    public String getFriendlyClass() {
        return friendlyClass;
    }

    public void setFriendlyClass(String friendlyClass) {
        this.friendlyClass = friendlyClass;
    }

    public String getOpposingClass() {
        return opposingClass;
    }

    public void setOpposingClass(String opposingClass) {
        this.opposingClass = opposingClass;
    }
}
