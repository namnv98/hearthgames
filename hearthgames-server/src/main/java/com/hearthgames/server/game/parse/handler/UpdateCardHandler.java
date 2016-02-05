package com.hearthgames.server.game.parse.handler;

import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class UpdateCardHandler extends AbstractHandler {

    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");
    private static final String SHOW_ENTITY = "SHOW_ENTITY - Updating Entity=";
    private static final String HIDE_ENTITY = "HIDE_ENTITY - Entity=";
    private static final String TAG = "tag";
    private static final String B = "[";
    private static final String B_NAME_EQ = "[name=";
    private static final String ID_EQ = "id=";
    private static final String SP_ZONE_EQ = " zone=";
    private static final String SP_CARDID_EQ = " cardId=";
    private static final String CARDID_EQ = "CardID=";
    private static final String SP_TAG_EQ = " tag=";
    private static final String TAG_EQ = "tag=";

    @Override
    public boolean supports(GameContext context, String line) {
        return line != null && context != null && (line.startsWith(SHOW_ENTITY) || context.isUpdateCard() || line.startsWith(HIDE_ENTITY));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean handle(GameContext context, LogLineData logLineData) {
        String line = logLineData.getTrimmedLine();
        if (context.isUpdateCard() && line.startsWith(TAG)) { // are we updating an existing card's data
            Map<String, String> data = getKeyValueData(line, tagPattern);
            context.updateCurrentCard(data);
        } else if (line.startsWith(SHOW_ENTITY)) {
            String[] data = getCardInfo(line);
            context.startUpdateCard(logLineData.getDateTime(), data[0], data[1]);
        } else if (line.startsWith(HIDE_ENTITY)) {
            List<Object> objects = getHideCardInfo(line);
            context.hideEntity(logLineData.getDateTime(), (String) objects.get(0), ( Map<String, String> ) objects.get(1));
        } else { // we were updating a card but found a line that meant to be doing something else
            context.endUpdateCard(logLineData.getDateTime());
            return false;
        }
        return true;
    }

    private List<Object> getHideCardInfo(String line) {
        // HIDE_ENTITY - Entity=[name=Master Jouster id=49 zone=HAND zonePos=3 cardId=AT_112 player=2] tag=ZONE value=DECK
        // HIDE_ENTITY - Entity=69 tag=ZONE value=DECK
        String entityStr = line.replace(HIDE_ENTITY,"").trim();
        if (!entityStr.startsWith(B)) {
            entityStr = entityStr.substring(0, entityStr.indexOf(TAG_EQ)).trim();
        } else if (entityStr.startsWith(B_NAME_EQ)) {
            // The entityStr doesn't ever appear to start with [name= during a card update.  I am leaving this here just in case in the future it does
            entityStr = entityStr.substring(entityStr.indexOf(ID_EQ)+ID_EQ.length(), entityStr.indexOf(SP_ZONE_EQ)).trim();
        } else {
            entityStr = entityStr.substring(entityStr.indexOf(ID_EQ)+ID_EQ.length(), entityStr.indexOf(SP_TAG_EQ)).trim();
        }
        List<Object> objects = new ArrayList<>();
        objects.add(entityStr);
        objects.add(getKeyValueData(line.substring(line.lastIndexOf(TAG_EQ)), tagPattern));
        return objects;
    }

    private String[] getCardInfo(String line) {
        String[] data = new String[2];
        // SHOW_ENTITY - Updating Entity=85 CardID=CS2_017o
        // SHOW_ENTITY - Updating Entity=[name=Emperor Thaurissan id=25 zone=DECK zonePos=0 cardId=BRM_028 player=1] CardID=BRM_028
        // SHOW_ENTITY - Updating Entity=[id=23 cardId= type=INVALID zone=DECK zonePos=0 player=1] CardID=CS2_031
        String entityStr = line.replace(SHOW_ENTITY,"").trim();
        if (!entityStr.startsWith(B)) {
            entityStr = entityStr.substring(0, entityStr.indexOf(CARDID_EQ)).trim();
        } else if (entityStr.startsWith(B_NAME_EQ)) {
            // The entityStr doesn't ever appear to start with [name= during a card update.  I am leaving this here just in case in the future it does
            entityStr = entityStr.substring(entityStr.indexOf(ID_EQ)+ID_EQ.length(), entityStr.indexOf(SP_ZONE_EQ)).trim();
        } else {
            entityStr = entityStr.substring(entityStr.indexOf(ID_EQ)+ID_EQ.length(), entityStr.indexOf(SP_CARDID_EQ)).trim();
        }
        data[0] = entityStr;
        data[1] = line.substring(line.lastIndexOf(CARDID_EQ)+CARDID_EQ.length());
        return data;
    }
}