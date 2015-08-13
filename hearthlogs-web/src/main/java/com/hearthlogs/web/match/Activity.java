package com.hearthlogs.web.match;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hearthlogs.web.domain.Entity;

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

    public enum SubType {

        POWER("POWER"),
        PLAY("PLAY"),
        TRIGGER("TRIGGER"),
        DEATHS("DEATHS"),
        ATTACK("ATTACK");

        private String subType;

        SubType(String subType) {
            this.subType = subType;
        }

        public String getSubType() {
            return subType;
        }
    }

    @JsonIgnore
    public boolean isTagChange() {
        return type == Type.TAG_CHANGE;
    }

    @JsonIgnore
    public boolean isAction() {
        return type == Type.ACTION;
    }

    @JsonIgnore
    public boolean isPower() {
        return this.subType.equals(SubType.POWER);
    }

    @JsonIgnore
    public boolean isPlay() {
        return this.subType.equals(SubType.PLAY);
    }

    @JsonIgnore
    public boolean isTrigger() {
        return this.subType.equals(SubType.TRIGGER);
    }

    @JsonIgnore
    public boolean isDeaths() {
        return this.subType.equals(SubType.DEATHS);
    }

    @JsonIgnore
    public boolean isAttack() {
        return this.subType.equals(SubType.ATTACK);
    }

    private Type type;

    private long id;

    private String entityId;

    // Action Data
    private Entity entity;
    private SubType subType;
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

    public SubType getSubType() {
        return subType;
    }

    public void setSubType(SubType subType) {
        this.subType = subType;
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