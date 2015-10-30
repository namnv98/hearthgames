package com.hearthlogs.server.service.log.handler;

import com.hearthlogs.server.match.MatchContext;

import java.util.Map;
import java.util.regex.Pattern;

public class UpdateCardHandler extends AbstractHandler {

    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");
    private static final String UPDATE_CARD = "SHOW_ENTITY - Updating Entity=";
    private static final String TAG = "tag";
    private static final String B = "[";
    private static final String B_NAME_EQ = "[name=";
    private static final String ID_EQ = "id=";
    private static final String SP_ZONE_EQ = " zone=";
    private static final String SP_CARDID_EQ = " cardId=";
    private static final String CARDID_EQ = "CardID=";

    @Override
    public boolean applies(MatchContext context, String line) {
        return line != null && context != null && (line.startsWith(UPDATE_CARD) || context.isUpdateCard());
    }

    @Override
    public boolean handle(MatchContext context, String line) {
        if (context.isUpdateCard() && line.startsWith(TAG)) { // are we updating an existing card's data
            Map<String, String> data = getKeyValueData(line, tagPattern);
            context.updateCurrentCard(data);
        } else if (line.startsWith(UPDATE_CARD)) {
            String[] data = getCardInfo(line);
            context.startUpdateCard(data[0], data[1]);
        } else { // we were updating a card but found a line that meant to be doing something else
            context.endUpdateCard();
            return false;
        }
        return true;
    }

    private String[] getCardInfo(String line) {
        String[] data = new String[2];
        // SHOW_ENTITY - Updating Entity=85 CardID=CS2_017o
        // SHOW_ENTITY - Updating Entity=[name=Emperor Thaurissan id=25 zone=DECK zonePos=0 cardId=BRM_028 player=1] CardID=BRM_028
        // SHOW_ENTITY - Updating Entity=[id=23 cardId= type=INVALID zone=DECK zonePos=0 player=1] CardID=CS2_031
        String entityStr = line.replace(UPDATE_CARD,"").trim();
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