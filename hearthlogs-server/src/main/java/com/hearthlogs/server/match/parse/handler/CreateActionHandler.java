package com.hearthlogs.server.match.parse.handler;


import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.log.domain.LogLineData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateActionHandler extends AbstractHandler {

    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_END = "ACTION_END";
    private static final Pattern actionStartPattern = Pattern.compile("ACTION_START Entity=(.*?) BlockType=(.*?) Index=(.*?) Target=(.*)");

    @Override
    public boolean supports(ParseContext context, String line) {
        return line != null && context != null && (context.isCreateAction() || line.startsWith(ACTION_START) || line.startsWith(ACTION_END));
    }

    @Override
    public boolean handle(ParseContext context, LogLineData logLineData) {
        context.setCurrentLine(logLineData);
        String line = logLineData.getTrimmedLine();
        if (context.isCreateAction() && line.startsWith(ACTION_START) && context.hasIndentationDecreased()) {
            // some actions don't have an ACTION_END so if we find another ACTION_START that is a lower indentation level then end the previous domain
            context.endAction();
            context.createAction(logLineData.getDateTime(), getActionData(line));
        } else if (context.isCreateAction() && line.startsWith(ACTION_START)) { // we found a child ACTION_START
            context.createSubAction(logLineData.getDateTime(), getActionData(line));
        } else if (line.startsWith(ACTION_START)) {
            context.createAction(logLineData.getDateTime(), getActionData(line));
        } else if (line.startsWith(ACTION_END)) { // are we done?  found an ACTION_END
            context.endAction();
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