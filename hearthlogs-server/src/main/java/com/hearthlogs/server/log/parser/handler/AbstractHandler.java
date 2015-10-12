package com.hearthlogs.server.log.parser.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractHandler implements Handler {

    private static final String TAG_CHANGE_ENTITY = "TAG_CHANGE Entity=";
    private static final String TAG_EQ = "tag=";
    private static final String SP_TAG_EQ = " tag=";
    private static final String B_NAME_EQ = "[name=";
    private static final String ID_EQ = "id=";
    private static final String B_ID_EQ = "[id=";
    private static final String SP_ZONE_EQ = " zone=";
    private static final String SP_CARD_ID_EQ = " cardId=";
    private static final String CLASS = "class";
    private static final String CARD_CLASS = "cardClass";
    private static final char CHAR_UNDERSCORE = '_';
    private static final String STRING_UNDERSCORE = "_";

    public String parseEntityStr(String line) {
        String parseEntityStr = null;
        if (line.startsWith(TAG_CHANGE_ENTITY)) {
            // We have 3 kinds of TAG_CHANGE entities.  One that is a enclosed in square brackets and one that name which may have spaces in it and one with an entity id
            String entityStr = line.replace(TAG_CHANGE_ENTITY, "");
            if (!entityStr.contains(TAG_EQ)) return null;
            entityStr = entityStr.substring(0, entityStr.indexOf(SP_TAG_EQ));
            parseEntityStr =  parseEntity(entityStr);
        }
        return parseEntityStr;
    }

    public String parseEntity(String entityStr) {
        // During actions we may already have the entity string such as GameEntity.
        String entity = entityStr;
        if (entityStr.startsWith(B_NAME_EQ)) { // [name=Antique Healbot id=16 zone=HAND zonePos=4 cardId=GVG_069 player=1]
            entity = entityStr.substring(entityStr.indexOf(ID_EQ) + ID_EQ.length(), entityStr.indexOf(SP_ZONE_EQ));
        } else if (entityStr.startsWith(B_ID_EQ)) { // [id=46 cardId= type=INVALID zone=DECK zonePos=0 player=2]
            entity = entityStr.substring(entityStr.indexOf(B_ID_EQ) + B_ID_EQ.length(), entityStr.indexOf(SP_CARD_ID_EQ));
        }
        return entity; // We have either a player name, the GameEntity or an id
    }

    public Map<String, String> getKeyValueData(String line, Pattern pattern) {
        Map<String, String> data = new HashMap<>();
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            int count = matcher.groupCount();
            if (count == 2) {
                String key = matcher.group(1);
                String newKey = StringUtils.remove(WordUtils.capitalizeFully(StringUtils.upperCase(key), CHAR_UNDERSCORE), STRING_UNDERSCORE);
                newKey = StringUtils.uncapitalize(newKey);
                if (CLASS.equals(newKey)) { // special case for cards that have a class tag.
                    newKey = CARD_CLASS;
                }
                data.put(newKey, matcher.group(2));
            }
        }
        return data;
    }
}