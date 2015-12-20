package com.hearthgames.server.database.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class RawGameError {

    @Id
    @Column(name = "raw_game_error_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer rank;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

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

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
