package com.hearthlogs.server.domain;

import java.io.Serializable;

/**
 *  The Game object stores the state of the GameEntity
 *
 *
 *     Game States (tag = STATE)
 *     RUNNING - indicates the match has started
 *     COMPLETE - indicates the match has completed
 *
 *     Game Steps (tag = STEP)  - this denotes each phase of the match
 *
 *     BEGIN_MULLIGAN      - start of the mulligan phase
 *
 *     MAIN_READY          - denotes the first phase in a turn. The amount of mana updated.
 *
 *     MAIN_START_TRIGGERS - not sure what is done in this phase yet (need more sample data) but I would assume something like Nat Pagle would trigger in this phase.
 *
 *     MAIN_START          - cards are drawn from the deck
 *
 *     MAIN_ACTION		   - Cards are played, Player Entity resources updated, cards played by opponent get updated to reveal card details, cards are put into play with their zone positions affected
 *                         - Minions can ATTACK. Hero Health, Hero Armor and Minion health is updated
 *                         - MAIN_ACTION repeats many times
 *
 *         MAIN_COMBAT	   - This happens within an ACTION_START ... ACTION_END and it details the damage dealt
 *                         - A following ACTION_START with SubType=DEATHS will follow
 *
 *     MAIN_END            - Other than denoting the end of a turn I haven't seen anything happen in this phase
 *
 *     MAIN_CLEANUP        - If a card was frozen on the previous turn it is unfrozen here. Cards that were flagged JUST_PLAYED=1 prevents them from attack have the flag set to 0.
 *                         - Secrets become not EXHAUSTED (does this mean that the secret could not trigger on your own turn?)
 *                         - Secrets that were actived during the main turn go to the GRAVEYARD
 *
 *     MAIN_NEXT           - Changes whose turn it is by setting the Player Entity CURRENT_PLAYER=1, updates the match entity TURN and the turn start time is recorded
 *
 *     FINAL_WRAPUP        - Not sure what happens in this phase
 *
 *     FINAL_GAMEOVER      - The last phase of the match
 *
 *
 */
public class Game extends Entity implements Serializable {

    private static final long serialVersionUID = 1;

    public enum State {
        RUNNING,
        COMPLETE;

        public boolean eq(String state) {
            return this.toString().equals(state);
        }
    }

    public enum Step {
        BEGIN_MULLIGAN,

        MAIN_READY,
        MAIN_START_TRIGGERS,
        MAIN_START,
        MAIN_COMBAT,
        MAIN_END,
        MAIN_CLEANUP,
        MAIN_NEXT,
        FINAL_WRAPUP,
        FINAL_GAMEOVER;

        public boolean eq(String step) {
            return this.toString().equals(step);
        }
    }

    private String turn;
    private String zone;
    private String nextStep;
    private String cardtype;
    private String state;
    private String step;
    private String numMinionsKilledThisTurn;
    private String proposedAttacker;
    private String proposedDefender;

    /**
     * Gets the current turn the match is on
     *
     * @return a turn number
     */
    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    /**
     * Gets the zone the match is in.  Other than when the match is initialized I don't see this ever get updated.
     *
     * @return a zone (only known value: PLAY)
     */
    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    /**
     * Gets the next match step (See class description for the available steps)
     *
     * @return the next step in the match
     */
    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    /**
     * Gets the type of card the Game object is
     *
     * @return "Game".  Yes the Game object is a Card too!  But I didn't model it that way but that might change if I see the Game object behaving like a Card.
     */
    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    /**
     * Gets the state of the match
     *
     * @return either "RUNNING" or "COMPLETED"
     */
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    /**
     * Gets the current match step (See class description for the available steps)
     *
     * @return the current step in the match
     */

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getNumMinionsKilledThisTurn() {
        return numMinionsKilledThisTurn;
    }

    public void setNumMinionsKilledThisTurn(String numMinionsKilledThisTurn) {
        this.numMinionsKilledThisTurn = numMinionsKilledThisTurn;
    }

    public String getProposedAttacker() {
        return proposedAttacker;
    }

    public void setProposedAttacker(String proposedAttacker) {
        this.proposedAttacker = proposedAttacker;
    }

    public String getProposedDefender() {
        return proposedDefender;
    }

    public void setProposedDefender(String proposedDefender) {
        this.proposedDefender = proposedDefender;
    }
}