package com.hearthlogs.server.match.parse.handler;

import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.raw.domain.LogLineData;

import java.util.Map;
import java.util.regex.Pattern;

public class TagChangeHandler extends AbstractHandler {

    private static final String TAG_CHANGE_LINE = "TAG_CHANGE Entity=";
    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");

    @Override
    public boolean supports(ParsedMatch parsedMatch, String line) {
        return line != null && parsedMatch != null && line.startsWith(TAG_CHANGE_LINE) ;
    }

    @Override
    public boolean handle(ParsedMatch parsedMatch, LogLineData logLineData) {
        String line = logLineData.getTrimmedLine();
        String entityStr = parseEntityStr(line);
        Map<String, String> data = getKeyValueData(line, tagPattern);
        parsedMatch.tagChange(logLineData.getDateTime(), entityStr, data);
        return true;
    }
}