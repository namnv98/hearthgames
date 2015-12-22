package com.hearthgames.server.database.domain;

import javax.persistence.*;

@Entity
public class Account {

    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long battletagId;
    private String battletag;

    private String gameAccountId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBattletagId() {
        return battletagId;
    }

    public void setBattletagId(Long battletagId) {
        this.battletagId = battletagId;
    }

    public String getBattletag() {
        return battletag;
    }

    public void setBattletag(String battletag) {
        this.battletag = battletag;
    }

    public String getGameAccountId() {
        return gameAccountId;
    }

    public void setGameAccountId(String gameAccountId) {
        this.gameAccountId = gameAccountId;
    }
}
