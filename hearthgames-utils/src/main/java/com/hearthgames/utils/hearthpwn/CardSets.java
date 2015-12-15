package com.hearthgames.utils.hearthpwn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardSets implements Serializable {

    private static final long serialVersionUID = 1;

    private List<CardDetails> basic = new ArrayList<>();
    private List<CardDetails> blackrockMountain = new ArrayList<>();
    private List<CardDetails> classic = new ArrayList<>();
    private List<CardDetails> credits = new ArrayList<>();
    private List<CardDetails> curseOfNaxxramas = new ArrayList<>();
    private List<CardDetails> debug = new ArrayList<>();
    private List<CardDetails> goblinsVsGnomes = new ArrayList<>();
    private List<CardDetails> missions = new ArrayList<>();
    private List<CardDetails> promotion = new ArrayList<>();
    private List<CardDetails> reward = new ArrayList<>();
    private List<CardDetails> system = new ArrayList<>();
    private List<CardDetails> tavernBrawl = new ArrayList<>();
    private List<CardDetails> heroSkins = new ArrayList<>();
    private List<CardDetails> theGrandTournament = new ArrayList<>();
    private List<CardDetails> leagueOfExplorers = new ArrayList<>();

    public List<CardDetails> getBasic() {
        return basic;
    }

    public void setBasic(List<CardDetails> basic) {
        this.basic = basic;
    }

    public List<CardDetails> getBlackrockMountain() {
        return blackrockMountain;
    }

    public void setBlackrockMountain(List<CardDetails> blackrockMountain) {
        this.blackrockMountain = blackrockMountain;
    }

    public List<CardDetails> getClassic() {
        return classic;
    }

    public void setClassic(List<CardDetails> classic) {
        this.classic = classic;
    }

    public List<CardDetails> getCredits() {
        return credits;
    }

    public void setCredits(List<CardDetails> credits) {
        this.credits = credits;
    }

    public List<CardDetails> getCurseOfNaxxramas() {
        return curseOfNaxxramas;
    }

    public void setCurseOfNaxxramas(List<CardDetails> curseOfNaxxramas) {
        this.curseOfNaxxramas = curseOfNaxxramas;
    }

    public List<CardDetails> getDebug() {
        return debug;
    }

    public void setDebug(List<CardDetails> debug) {
        this.debug = debug;
    }

    public List<CardDetails> getGoblinsVsGnomes() {
        return goblinsVsGnomes;
    }

    public void setGoblinsVsGnomes(List<CardDetails> goblinsVsGnomes) {
        this.goblinsVsGnomes = goblinsVsGnomes;
    }

    public List<CardDetails> getMissions() {
        return missions;
    }

    public void setMissions(List<CardDetails> missions) {
        this.missions = missions;
    }

    public List<CardDetails> getPromotion() {
        return promotion;
    }

    public void setPromotion(List<CardDetails> promotion) {
        this.promotion = promotion;
    }

    public List<CardDetails> getReward() {
        return reward;
    }

    public void setReward(List<CardDetails> reward) {
        this.reward = reward;
    }

    public List<CardDetails> getSystem() {
        return system;
    }

    public void setSystem(List<CardDetails> system) {
        this.system = system;
    }

    public List<CardDetails> getTavernBrawl() {
        return tavernBrawl;
    }

    public void setTavernBrawl(List<CardDetails> tavernBrawl) {
        this.tavernBrawl = tavernBrawl;
    }

    public List<CardDetails> getHeroSkins() {
        return heroSkins;
    }

    public void setHeroSkins(List<CardDetails> heroSkins) {
        this.heroSkins = heroSkins;
    }

    public List<CardDetails> getTheGrandTournament() {
        return theGrandTournament;
    }

    public void setTheGrandTournament(List<CardDetails> theGrandTournament) {
        this.theGrandTournament = theGrandTournament;
    }

    public List<CardDetails> getLeagueOfExplorers() {
        return leagueOfExplorers;
    }

    public void setLeagueOfExplorers(List<CardDetails> leagueOfExplorers) {
        this.leagueOfExplorers = leagueOfExplorers;
    }
}
