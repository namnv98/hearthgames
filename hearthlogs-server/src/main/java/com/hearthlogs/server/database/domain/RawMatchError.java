package com.hearthlogs.server.database.domain;

import javax.persistence.*;

@Entity
public class RawMatchError {

    @Id
    @Column(name = "raw_match_error_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(nullable = false)
    private byte[] rawGame;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getRawGame() {
        return rawGame;
    }

    public void setRawGame(byte[] rawGame) {
        this.rawGame = rawGame;
    }
}
