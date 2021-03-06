package com.hearthgames.server.game.hsreplay;

import com.google.common.base.CaseFormat;
import com.hearthgames.server.game.hsreplay.domain.*;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.parse.domain.Player;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;

public class HSReplayHandler extends DefaultHandler {

    public static final String GAME = "Game";
    public static final String GAME_ENTITY = "GameEntity";
    public static final String TAG = "Tag";
    public static final String PLAYER = "Player";
    public static final String FULL_ENTITY = "FullEntity";
    public static final String ACTION = "Action";
    public static final String SHOW_ENTITY = "ShowEntity";
    public static final String HIDE_ENTITY = "HideEntity";
    public static final String TAG_CHANGE = "TagChange";

    public static final String ENTITY = "entity";
    public static final String STATE = "204";
    public static final String COMPLETE = "3";
    public static final String RUNNING = "2";

    public static final String CARDID = "cardid";

    private GameState gameState;

    private int actionNestingCount = 0;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (GAME.equalsIgnoreCase(qName)) {
            gameState = new GameState();
        } else if (GAME_ENTITY.equalsIgnoreCase(qName)) {
            gameState.startCreateGame();
            gameState.updateCreateGame(getEntityData(attributes));
        } else if (gameState != null && gameState.isCreateGameEntity() && TAG.equalsIgnoreCase(qName)) {
            gameState.updateCreateGame(getTagData(attributes, EntityType.GAME_ENTITY));
        } else if (PLAYER.equalsIgnoreCase(qName)) {
            Map<String, String> data = getEntityData(attributes);
            String[] playerData = new String[2];
            playerData[0] = data.get("accountHi");
            playerData[1] = data.get("accountLo");
            gameState.startCreatePlayer(null, playerData);
            Player player = gameState.getCurrentPlayer();
            player.setEntityId(data.get("entityId"));
            player.setPlayerId(data.get("playerID"));
            player.setName(data.get("name"));
        } else if (gameState != null && gameState.isCreatePlayer() && TAG.equalsIgnoreCase(qName)) {
            gameState.updateCreatePlayer(getTagData(attributes, EntityType.PLAYER));
        } else if (FULL_ENTITY.equalsIgnoreCase(qName)) {
            gameState.startCreateCard(null, getEntityData(attributes));
        } else if (gameState != null && gameState.isCreateCard() && TAG.equalsIgnoreCase(qName)) {
            gameState.updateCreateCard(getTagData(attributes, EntityType.CARD));
        } else if (ACTION.equalsIgnoreCase(qName)) {
            if (actionNestingCount == 1) {
                gameState.createAction(null, getActionData(attributes));
            } else if (actionNestingCount > 1) {
                gameState.createSubAction(null, getActionData(attributes));
            }
            actionNestingCount++;
        } else if (gameState != null && (SHOW_ENTITY.equalsIgnoreCase(qName) || gameState.isUpdateCard() || HIDE_ENTITY.equalsIgnoreCase(qName))) {
            if (gameState.isUpdateCard() && TAG.equalsIgnoreCase(qName)) {
                gameState.updateCurrentCard(getTagData(attributes, EntityType.CARD));
            } else if (SHOW_ENTITY.equalsIgnoreCase(qName)) {
                Map<String, String> data = getEntityData(attributes);
                gameState.startUpdateCard(null, data.get("entityId"), data.get(CARDID));
            } else if (HIDE_ENTITY.equalsIgnoreCase(qName)) {
                Map<String, String> data = getEntityData(attributes);
                gameState.hideEntity(null, data.get("entityId"), data);
            }
        } else if (gameState != null && TAG_CHANGE.equalsIgnoreCase(qName) && gameState.getGameEntity().getEntityId().equalsIgnoreCase(attributes.getValue(ENTITY))) {
            Map<String, String> data = getTagData(attributes, EntityType.GAME_ENTITY);
            String state = attributes.getValue(STATE);
            if (state != null && state.equals(COMPLETE)) {
                gameState.endUpdateGame(null, data);
            } else if (state != null && state.equals(RUNNING)) {
                gameState.startUpdateGame(null, data);
            } else if (gameState.isGameUpdating()) {
                gameState.updateCurrentGame(null, data);
            } else {
                gameState.tagChange(null, "1", data);
            }
        } else if (TAG_CHANGE.equalsIgnoreCase(qName)) {
            int entityId = getEntityId(attributes.getValue("entity"));
            EntityType entityType = EntityType.GAME_ENTITY;
            if (entityId >= 2 && entityId <= 3) {
                entityType = EntityType.PLAYER;
            } else if (entityId > 3) {
                entityType = EntityType.CARD;
            }
            Map<String, String> data = getTagData(attributes, entityType);
            gameState.tagChange(null, Integer.toString(entityId), data);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (GAME_ENTITY.equalsIgnoreCase(qName)) {
            gameState.endCreateGame(null);
        } else if (PLAYER.equalsIgnoreCase(qName)) {
            gameState.endCreatePlayer(null);
        } else if (FULL_ENTITY.equalsIgnoreCase(qName)) {
            gameState.endCreateCard(null);
        } else if (ACTION.equalsIgnoreCase(qName)) {
            if (actionNestingCount >= 1) {
                gameState.endAction();
            }
            actionNestingCount--;
        } else if (SHOW_ENTITY.equalsIgnoreCase(qName)) {
            gameState.endUpdateCard(null);
        }

    }

    private static String[] getActionData(Attributes attributes) {
        Map<String, String> data = getEntityData(attributes);
        String[] actionData = new String[4];
        actionData[0] = data.get("entityId");
        actionData[1] = BlockType.getBlockTypeByValue(data.get("type")).toString();
        actionData[2] = data.get("index");
        actionData[3] = data.get("target");
        return actionData;
    }

    private static Map<String, String> getEntityData(Attributes attributes) {
        Map<String, String> data = new HashMap<>();
        for (int i=0; i < attributes.getLength(); i++) {
            String tag = fixTagName(attributes.getQName(i));
            String value = fixTagValue(tag, attributes.getValue(i));
            data.put(tag, value);
        }
        return data;
    }

    private static Map<String, String> getTagData(Attributes attributes, EntityType entityType) {
        Map<String, String> data = new HashMap<>();
        String tag = attributes.getValue("tag");
        Tag t = Tag.getTagByValue(tag, entityType);
        tag = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, t.toString());
        String value = fixTagValue(tag, attributes.getValue("value"));
        data.put(tag, value);
        return data;
    }

    private static String fixTagName(String originalTagName) {
        switch (originalTagName) {
            case "id": case ENTITY: return "entityId";
            case "cardID": return "cardid";
            default: return originalTagName;
        }
    }

    private int getEntityId(String entityIdStr) {
        return Integer.parseInt(entityIdStr);
    }

    private static String fixTagValue(String tagName, String originalTagValue) {
        if ("zone".equalsIgnoreCase(tagName)) {
            return Zone.getZoneByValue(originalTagValue).toString();
        } else if ("step".equalsIgnoreCase(tagName) || "nextStep".equalsIgnoreCase(tagName)) {
            return Step.getStepByValue(originalTagValue).toString();
        } else if ("cardType".equalsIgnoreCase(tagName)) {
            return CardType.getCardTypeByValue(originalTagValue).toString();
        }
        return originalTagValue;
    }

    public GameState getGameState() {
        return gameState;
    }

}