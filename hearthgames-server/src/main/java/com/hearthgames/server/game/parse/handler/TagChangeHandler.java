package com.hearthgames.server.game.parse.handler;

import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameContext;

import java.util.Map;
import java.util.regex.Pattern;

public class TagChangeHandler extends AbstractHandler {

    private static final String TAG_CHANGE_LINE = "TAG_CHANGE Entity=";
    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");

    @Override
    protected boolean supportsLine(GameContext context, String line) {
        return line.startsWith(TAG_CHANGE_LINE);
    }

    @Override
    public boolean handle(GameContext context, LogLineData logLineData) {
        String line = logLineData.getTrimmedLine();
        String entityStr = parseEntityStr(line);
        Map<String, String> data = getKeyValueData(line, tagPattern);
        context.tagChange(logLineData.getDateTime(), entityStr, data);
        return true;
    }
}