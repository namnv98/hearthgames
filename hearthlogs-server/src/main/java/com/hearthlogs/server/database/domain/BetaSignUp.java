package com.hearthlogs.server.database.domain;

import javax.persistence.*;

@Entity
public class BetaSignUp {

    @Id
    @Column(name = "beta_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String battletag;
    private String email;
    private boolean approved;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBattletag() {
        return battletag;
    }

    public void setBattletag(String battletag) {
        this.battletag = battletag;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
