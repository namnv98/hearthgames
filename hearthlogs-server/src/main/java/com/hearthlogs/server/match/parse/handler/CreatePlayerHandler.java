package com.hearthlogs.server.match.parse.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.raw.domain.LogLineData;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreatePlayerHandler extends AbstractHandler {

    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");
    private static final Pattern playerPattern = Pattern.compile("GameAccountId=\\[hi=(.*?) lo=(.*?)]");
    private static final String PLAYER = "Player";
    private static final String TAG = "tag";

    @Override
    public boolean supports(ParseContext context, String line) {
        return line != null && context != null && (line.startsWith(PLAYER) || context.isCreatePlayer());
    }

    @Override
    public boolean handle(ParseContext context, LogLineData logLineData) {
        context.setCurrentLine(logLineData);
        String line = logLineData.getTrimmedLine();
        if (context.isCreatePlayer() && line.startsWith(PLAYER)) { // we found the 2nd player
            context.endCreatePlayer(logLineData.getDateTime());
            String[] data = getGameAccountInfo(line);
            context.startCreatePlayer(logLineData.getDateTime(), data);
        } else if (context.isCreatePlayer() && line.startsWith(TAG)) { // in progress creating player populate some data
            Map<String, String> data = getKeyValueData(line, tagPattern);
            context.updateCreatePlayer(data);
        } else if (line.startsWith(PLAYER)) { // first time we found a player so create a new one and flag that we are creating players
            String[] data = getGameAccountInfo(line);
            context.startCreatePlayer(logLineData.getDateTime(), data);
        } else { // we were creating player but found a line that meant to be doing something else
            context.endCreatePlayer(logLineData.getDateTime());
            return false;
        }
        return true;
    }

    private String[] getGameAccountInfo(String line) {
        String[] data = new String[2];
        Matcher matcher = playerPattern.matcher(line);
        if (matcher.find()) {
            int count = matcher.groupCount();
            if (count == 2) {
                data[0] = matcher.group(1);
                data[1] = matcher.group(2);
            }
        }
        return data;
    }
}
