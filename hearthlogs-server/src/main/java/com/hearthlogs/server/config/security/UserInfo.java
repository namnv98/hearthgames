package com.hearthlogs.server.config.security;

public class UserInfo {

    private long id;
    private String battletag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBattletag() {
        return battletag;
    }

    public void setBattletag(String battletag) {
        this.battletag = battletag;
    }
}
