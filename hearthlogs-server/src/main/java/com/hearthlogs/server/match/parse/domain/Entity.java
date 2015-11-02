package com.hearthlogs.server.match.parse.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Entity implements Serializable {

    private static final long serialVersionUID = 1;

    protected String entityId;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    protected Map<String, String> unknownTags = new HashMap<>();

    public Map<String, String> getUnknownTags() {
        return unknownTags;
    }

    public void setUnknownTags(Map<String, String> unknownTags) {
        this.unknownTags = unknownTags;
    }
}
