package com.hearthlogs.web.log.parser.handler;

import com.hearthlogs.web.match.MatchContext;

import java.util.Map;
import java.util.regex.Pattern;

public class TagChangeHandler extends Handler {

    private static final String TAG_CHANGE_LINE = "TAG_CHANGE Entity=";
    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");

    @Override
    public boolean applies(MatchContext context, String line) {
        return line != null && context != null && line.startsWith(TAG_CHANGE_LINE) ;
    }

    @Override
    public boolean handle(MatchContext context, String line) {
        String entityStr = parseEntityStr(line);
        Map<String, String> data = getKeyValueData(line, tagPattern);
        context.tagChange(entityStr, data);
        return true;
    }
}