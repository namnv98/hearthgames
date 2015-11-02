package com.hearthlogs.server.match.parse.handler;


import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.raw.domain.LogLineData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateActionHandler extends AbstractHandler {

    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_END = "ACTION_END";
    private static final Pattern actionStartPattern = Pattern.compile("ACTION_START Entity=(.*?) BlockType=(.*?) Index=(.*?) Target=(.*)");

    @Override
    public boolean supports(ParsedMatch parsedMatch, String line) {
        return line != null && parsedMatch != null && (parsedMatch.isCreateAction() || line.startsWith(ACTION_START) || line.startsWith(ACTION_END));
    }

    @Override
    public boolean handle(ParsedMatch parsedMatch, LogLineData logLineData) {
        String line = logLineData.getTrimmedLine();
        if (parsedMatch.isCreateAction() && line.startsWith(ACTION_START) && parsedMatch.hasIndentationDecreased()) {
            // some actions don't have an ACTION_END so if we find another ACTION_START that is a lower indentation level then end the previous domain
            parsedMatch.endAction();
            parsedMatch.createAction(logLineData.getDateTime(), getActionData(line));
        } else if (parsedMatch.isCreateAction() && line.startsWith(ACTION_START)) { // we found a child ACTION_START
            parsedMatch.createSubAction(logLineData.getDateTime(), getActionData(line));
        } else if (line.startsWith(ACTION_START)) {
            parsedMatch.createAction(logLineData.getDateTime(), getActionData(line));
        } else if (line.startsWith(ACTION_END)) { // are we done?  found an ACTION_END
            parsedMatch.endAction();
        }
        return true;
    }

    private String[] getActionData(String line) {
        String[] data = new String[4];
        Matcher matcher = actionStartPattern.matcher(line);
        if (matcher.find()) {
            data[0] = parseEntity(matcher.group(1));
            data[1] = matcher.group(2);
            data[2] = matcher.group(3);
            data[3] = parseEntity(matcher.group(4));
        }
        return data;
    }
}