package com.hearthlogs.server.match.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Activity implements Serializable {

    private static final long serialVersionUID = 1;

    public Activity() {
    }

    public enum Type {
        NEW_GAME,
        NEW_PLAYER,
        NEW_CARD,
        ACTION,
        TAG_CHANGE
    }

    public enum BlockType {

        POWER("POWER"),
        PLAY("PLAY"),
        TRIGGER("TRIGGER"),
        DEATHS("DEATHS"),
        ATTACK("ATTACK"),
        JOUST("JOUST"),
        FATIGUE("FATIGUE");

        private String blockType;

        BlockType(String blockType) {
            this.blockType = blockType;
        }

        public String getBlockType() {
            return blockType;
        }
    }

    @JsonIgnore
    public boolean isTagChange() {
        return type == Type.TAG_CHANGE;
    }

    @JsonIgnore
    public boolean isNewCard() {
        return type == Type.NEW_CARD;
    }

    @JsonIgnore
    public boolean isNewGame() {
        return type == Type.NEW_GAME;
    }

    @JsonIgnore
    public boolean isAction() {
        return type == Type.ACTION;
    }

    @JsonIgnore
    public boolean isPower() {
        return this.blockType.equals(BlockType.POWER);
    }

    @JsonIgnore
    public boolean isPlay() {
        return this.blockType.equals(BlockType.PLAY);
    }

    @JsonIgnore
    public boolean isTrigger() {
        return this.blockType.equals(BlockType.TRIGGER);
    }

    @JsonIgnore
    public boolean isDeaths() {
        return this.blockType.equals(BlockType.DEATHS);
    }

    @JsonIgnore
    public boolean isAttack() {
        return this.blockType.equals(BlockType.ATTACK);
    }

    @JsonIgnore
    public boolean isJoust() {
        return this.blockType.equals(BlockType.JOUST);
    }

    @JsonIgnore
    public boolean isFatigue() {
        return this.blockType.equals(BlockType.FATIGUE);
    }

    private Type type;

    private long id;

    private String entityId;

    // Action Data
    private Entity entity;
    private BlockType blockType;
    private String index;
    private Entity target;

    @JsonBackReference()
    private Activity parent;

    @JsonManagedReference()
    private List<Activity> children = new ArrayList<>();

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public void addChildGameEvent(Activity activity) {
        activity.setParent(this);
        children.add(activity);
    }

    public List<Activity> getChildren() {
        return children;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setChildren(List<Activity> children) {
        this.children = children;
    }

    public Activity getParent() {
        return parent;
    }

    public void setParent(Activity parent) {
        this.parent = parent;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}