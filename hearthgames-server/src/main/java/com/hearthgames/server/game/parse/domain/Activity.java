package com.hearthgames.server.game.parse.domain;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Activity implements Serializable {

    private static final long serialVersionUID = 1;

    private Type type;

    private long id;

    private String entityId;

    // Action Data
    private LocalDateTime dateTime;
    private Card delta;
    private BlockType blockType;
    private String index;
    private Card target;

    private Activity parent;

    private List<Activity> children = new ArrayList<>();

    public enum Type {
        NEW_GAME,
        NEW_PLAYER,
        NEW_CARD,
        ACTION,
        TAG_CHANGE,
        SHOW_ENTITY,
        HIDE_ENTITY
    }

    public enum BlockType {

        POWER("POWER"),
        PLAY("PLAY"),
        TRIGGER("TRIGGER"),
        DEATHS("DEATHS"),
        ATTACK("ATTACK"),
        JOUST("JOUST"),
        FATIGUE("FATIGUE");

        private String type;

        BlockType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public boolean isTagChange() {
        return type == Type.TAG_CHANGE;
    }

    public boolean isShowEntity() {
        return type == Type.SHOW_ENTITY;
    }

    public boolean isCard() {
        return !(delta instanceof Player) && !(delta instanceof GameEntity);
    }

    public boolean isPlayer() {
        return delta instanceof Player;
    }

    public boolean isGame() {
        return delta instanceof GameEntity;
    }

    public boolean isHideEntity() {
        return type == Type.HIDE_ENTITY;
    }

    public boolean isNewCard() {
        return type == Type.NEW_CARD;
    }

    public boolean isNewGame() {
        return type == Type.NEW_GAME;
    }

    public boolean isNewPlayer() {
        return type == Type.NEW_PLAYER;
    }

    public boolean isAction() {
        return type == Type.ACTION;
    }

    public boolean isPower() {
        return this.blockType != null && this.blockType.equals(BlockType.POWER);
    }

    public boolean isPlay() {
        return this.blockType != null && this.blockType.equals(BlockType.PLAY);
    }

    public boolean isTrigger() {
        return this.blockType != null && this.blockType.equals(BlockType.TRIGGER);
    }

    public boolean isDeaths() {
        return this.blockType != null && this.blockType.equals(BlockType.DEATHS);
    }

    public boolean isAttack() {
        return this.blockType != null && this.blockType.equals(BlockType.ATTACK);
    }

    public boolean isJoust() {
        return this.blockType != null && this.blockType.equals(BlockType.JOUST);
    }

    public boolean isFatigue() {
        return this.blockType != null && this.blockType.equals(BlockType.FATIGUE);
    }

    public Card getDelta() {
        return delta;
    }

    public void setDelta(Card delta) {
        this.delta = delta;
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

    public Card getTarget() {
        return target;
    }

    public void setTarget(Card target) {
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        String entityType;
        if (delta instanceof Player) {
            entityType = "Player";
        } else if (delta instanceof GameEntity) {
            entityType = "Game";
        } else {
            entityType = "Card";
        }

        String fields = type != Type.ACTION ? Arrays.toString(getNonNullPropertyNames(delta)) : "";

        String bt = this.blockType != null ? ", type=" + this.blockType : "";

        return "Activity{" +
                "id=" + id+
                ", type=" + type + bt +
                ", delta=" + entityType +
                ", fields= " + fields +
                '}';
    }

    public static String[] getNonNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new LinkedHashSet<>();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue != null && !pd.getName().startsWith("cardDetails") && !pd.getName().equals("class") && !pd.getName().equals("unknownTags")) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}