package com.hearthlogs.server.match.parse.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.log.domain.LogLineData;

import java.util.Map;
import java.util.regex.Pattern;

public class TagChangeHandler extends AbstractHandler {

    private static final String TAG_CHANGE_LINE = "TAG_CHANGE Entity=";
    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");

    @Override
    public boolean supports(ParseContext context, String line) {
        return line != null && context != null && line.startsWith(TAG_CHANGE_LINE) ;
    }

    @Override
    public boolean handle(ParseContext context, LogLineData logLineData) {
        context.setCurrentLine(logLineData);
        String line = logLineData.getTrimmedLine();
        String entityStr = parseEntityStr(line);
        Map<String, String> data = getKeyValueData(line, tagPattern);
        context.tagChange(logLineData.getDateTime(), entityStr, data);
        return true;
    }
}