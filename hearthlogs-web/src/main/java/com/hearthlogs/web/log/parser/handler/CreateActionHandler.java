package com.hearthlogs.web.log.parser.handler;

import com.hearthlogs.web.match.MatchContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateActionHandler extends Handler {

    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_END = "ACTION_END";
    private static final String SUB_TYPE_TRIGGER = "SubType=TRIGGER";
    private static final Pattern actionStartPattern = Pattern.compile("ACTION_START Entity=(.*?) SubType=(.*?) Index=(.*?) Target=(.*)");

    @Override
    public boolean applies(MatchContext context, String line) {
        return line != null && context != null && (context.isCreateAction() || line.startsWith(ACTION_START) || line.startsWith(ACTION_END));
    }

    @Override
    public boolean handle(MatchContext context, String line) {
        if (context.isCreateAction() && line.startsWith(ACTION_START) && line.contains(SUB_TYPE_TRIGGER)) {
            // some actions don't have an ACTION_END so if we find another ACTION_START that is a sub type = TRIGGER we know we should
            // end the previous action and
            context.endAction();
            context.createAction(getActionData(line));
        } else if (context.isCreateAction() && line.startsWith(ACTION_START)) { // we found a child ACTION_START
            context.createSubAction(getActionData(line));
        } else if (line.startsWith(ACTION_START)) {
            context.createAction(getActionData(line));
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