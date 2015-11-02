package com.hearthlogs.server.match.stats.domain;

public class MatchStatistics {

    private long friendlyTotalMana;
    private long friendlyManaUsed;
    private long friendlyManaSaved;
    private float friendlyManaEfficiency;

    private long opposingTotalMana;
    private long opposingManaUsed;
    private long opposingManaSaved;
    private float opposingManaEfficiency;


    public long getFriendlyTotalMana() {
        return friendlyTotalMana;
    }

    public void setFriendlyTotalMana(long friendlyTotalMana) {
        this.friendlyTotalMana = friendlyTotalMana;
    }

    public long getFriendlyManaUsed() {
        return friendlyManaUsed;
    }

    public void setFriendlyManaUsed(long friendlyManaUsed) {
        this.friendlyManaUsed = friendlyManaUsed;
    }

    public long getFriendlyManaSaved() {
        return friendlyManaSaved;
    }

    public void setFriendlyManaSaved(long friendlyManaSaved) {
        this.friendlyManaSaved = friendlyManaSaved;
    }

    public float getFriendlyManaEfficiency() {
        return friendlyManaEfficiency;
    }

    public void setFriendlyManaEfficiency(float friendlyManaEfficiency) {
        this.friendlyManaEfficiency = friendlyManaEfficiency;
    }

    public long getOpposingTotalMana() {
        return opposingTotalMana;
    }

    public void setOpposingTotalMana(long opposingTotalMana) {
        this.opposingTotalMana = opposingTotalMana;
    }

    public long getOpposingManaUsed() {
        return opposingManaUsed;
    }

    public void setOpposingManaUsed(long opposingManaUsed) {
        this.opposingManaUsed = opposingManaUsed;
    }

    public long getOpposingManaSaved() {
        return opposingManaSaved;
    }

    public void setOpposingManaSaved(long opposingManaSaved) {
        this.opposingManaSaved = opposingManaSaved;
    }

    public float getOpposingManaEfficiency() {
        return opposingManaEfficiency;
    }

    public void setOpposingManaEfficiency(float opposingManaEfficiency) {
        this.opposingManaEfficiency = opposingManaEfficiency;
    }
}
