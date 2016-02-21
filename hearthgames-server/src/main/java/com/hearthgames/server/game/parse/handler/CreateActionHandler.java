package com.hearthgames.server.game.parse.handler;


import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameState;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateActionHandler extends AbstractHandler {

    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_END = "ACTION_END";
    private static final Pattern actionStartPattern = Pattern.compile("ACTION_START Entity=(.*?) BlockType=(.*?) Index=(.*?) Target=(.*)");

    @Override
    protected boolean supportsLine(GameState gameState, String line) {
        return gameState.isCreateAction() || line.startsWith(ACTION_START) || line.startsWith(ACTION_END);
    }

    @Override
    public boolean handle(GameState gameState, LogLineData logLineData) {
        String line = logLineData.getTrimmedLine();
        if (gameState.isCreateAction() && line.startsWith(ACTION_START) && gameState.isEndAction()) {
            // some actions don't have an ACTION_END so if we find another ACTION_START that is a lower indentation level then end the previous domain
            gameState.endAction();
            gameState.createAction(logLineData.getDateTime(), getActionData(line));
        } else if (gameState.isCreateAction() && line.startsWith(ACTION_START)) { // we found a child ACTION_START
            gameState.createSubAction(logLineData.getDateTime(), getActionData(line));
        } else if (line.startsWith(ACTION_START)) {
            gameState.createAction(logLineData.getDateTime(), getActionData(line));
        } else if (line.startsWith(ACTION_END)) { // are we done?  found an ACTION_END
            gameState.endAction();
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