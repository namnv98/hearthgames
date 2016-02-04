package com.hearthgames.server.game.hsreplay;

import com.google.common.base.CaseFormat;
import com.hearthgames.server.game.hsreplay.domain.BlockType;
import com.hearthgames.server.game.hsreplay.domain.Tag;
import com.hearthgames.server.game.hsreplay.domain.Zone;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.parse.domain.Player;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private List<GameContext> contexts = new ArrayList<>();

    private GameContext context;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (GAME.equalsIgnoreCase(qName)) {
            context = new GameContext();
        } else if (GAME_ENTITY.equalsIgnoreCase(qName)) {
            context.startCreateGame();
            context.updateCreateGame(getEntityData(attributes));
        } else if (context != null && context.isCreateGameEntity() && TAG.equalsIgnoreCase(qName)) {
            context.updateCreateGame(getTagData(attributes));
        } else if (PLAYER.equalsIgnoreCase(qName)) {
            Map<String, String> data = getEntityData(attributes);
            String[] playerData = new String[2];
            playerData[0] = data.get("accountHi");
            playerData[1] = data.get("accountLo");
            context.startCreatePlayer(null, playerData);
            Player player = context.getCurrentPlayer();
            player.setEntityId(data.get("entityId"));
            player.setPlayerId(data.get("playerID"));
            player.setName(data.get("name"));
        } else if (context != null && context.isCreatePlayer() && TAG.equalsIgnoreCase(qName)) {
            context.updateCreatePlayer(getTagData(attributes));
        } else if (FULL_ENTITY.equalsIgnoreCase(qName)) {
            context.startCreateCard(null, getEntityData(attributes));
        } else if (context != null && context.isCreateCard() && TAG.equalsIgnoreCase(qName)) {
            context.updateCreateCard(getTagData(attributes));
        } else if (context != null && ACTION.equalsIgnoreCase(qName) && context.isCreateAction()) {
            context.createSubAction(null, getActionData(attributes));
        } else if (ACTION.equalsIgnoreCase(qName)) {
            context.createAction(null, getActionData(attributes));
        } else if (context != null && (SHOW_ENTITY.equalsIgnoreCase(qName) || context.isUpdateCard() || HIDE_ENTITY.equalsIgnoreCase(qName))) {
            if (context.isUpdateCard() && TAG.equalsIgnoreCase(qName)) {
                context.updateCurrentCard(getTagData(attributes));
            } else if (SHOW_ENTITY.equalsIgnoreCase(qName)) {
                Map<String, String> data = getEntityData(attributes);
                context.startUpdateCard(null, data.get("entityId"), data.get(CARDID));
            } else if (HIDE_ENTITY.equalsIgnoreCase(qName)) {
                Map<String, String> data = getEntityData(attributes);
                context.hideEntity(null, data.get("entityId"), data);
            }
        } else if (context != null && TAG_CHANGE.equalsIgnoreCase(qName) && context.getGameEntity().getEntityId().equalsIgnoreCase(attributes.getValue(ENTITY))) {
            Map<String, String> data = getTagData(attributes);
            String state = attributes.getValue(STATE);
            if (state != null && state.equals(COMPLETE)) {
                context.endUpdateGame(null, data);
            } else if (state != null && state.equals(RUNNING)) {
                context.startUpdateGame(null, data);
            } else if (context.isGameUpdating()) {
                context.updateCurrentGame(null, data);
            } else {
                context.populateEntity(context.getGameEntity(), data);
            }
        } else if (TAG_CHANGE.equalsIgnoreCase(qName)) {
            Map<String, String> data = getTagChangeData(attributes);
            String entity = data.get(ENTITY);
            data.remove(ENTITY);
            context.tagChange(null, entity, data);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (GAME.equalsIgnoreCase(qName)) {
            contexts.add(context);
            context = null;
        } else if (GAME_ENTITY.equalsIgnoreCase(qName)) {
            context.endCreateGame(null);
        } else if (PLAYER.equalsIgnoreCase(qName)) {
            context.endCreatePlayer(null);
        } else if (FULL_ENTITY.equalsIgnoreCase(qName)) {
            context.endCreateCard(null);
        } else if (ACTION.equalsIgnoreCase(qName)) {
            context.endAction();
        } else if (SHOW_ENTITY.equalsIgnoreCase(qName)) {
            context.endUpdateCard(null);
        }

    }

    private String[] getActionData(Attributes attributes) {
        Map<String, String> data = getEntityData(attributes);
        String[] actionData = new String[4];
        actionData[0] = data.get("entityId");
        actionData[1] = BlockType.getBlockTypeByValue(data.get("type")).toString();
        actionData[2] = data.get("index");
        actionData[3] = data.get("target");
        return actionData;
    }

    private Map<String, String> getEntityData(Attributes attributes) {
        Map<String, String> data = new HashMap<>();
        for (int i=0; i < attributes.getLength(); i++) {
            String tag = fixTagName(attributes.getQName(i));
            String value = fixTagValue(tag, attributes.getValue(i));
            data.put(tag, value);
        }
        return data;
    }

    private Map<String, String> getTagData(Attributes attributes) {
        Map<String, String> data = new HashMap<>();
        String tag = attributes.getValue("tag");
        Tag t = Tag.getTagByValue(tag);
        tag = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, t.toString());
        String value = attributes.getValue("value");
        data.put(tag, value);
        return data;
    }

    private Map<String, String> getTagChangeData(Attributes attributes) {
        Map<String, String> data = new HashMap<>();
        data.put(ENTITY, attributes.getValue("entity"));
        String tag = attributes.getValue("tag");
        Tag t = Tag.getTagByValue(tag);
        tag = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, t.toString());
        String value = attributes.getValue("value");
        data.put(tag, value);
        return data;
    }

    private String fixTagName(String originalTagName) {
        switch (originalTagName) {
            case "id": case ENTITY: return "entityId";
            case "cardID": return "cardid";
            default: return originalTagName;
        }
    }

    private String fixTagValue(String tagName, String originalTagValue) {
        if ("zone".equalsIgnoreCase(tagName)) {
            return Zone.getZoneByValue(originalTagValue).toString();
        }
        return originalTagValue;
    }

    public List<GameContext> getContexts() {
        return contexts;
    }

    public static void main(String[] args) throws Exception {

        System.out.println();

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        HSReplayHandler handler = new HSReplayHandler();
        saxParser.parse(new File("C:\\GitSandbox\\hearthgames\\hearthgames-server\\src\\main\\resources\\test-data\\decks-assemble.hsreplay.xml"), handler);

        List<GameContext> contexts = handler.getContexts();

        System.out.println();

    }


}


