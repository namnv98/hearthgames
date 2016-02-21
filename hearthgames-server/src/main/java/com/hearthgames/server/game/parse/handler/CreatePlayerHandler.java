package com.hearthgames.server.game.parse.handler;

import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameState;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreatePlayerHandler extends AbstractHandler {

    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");
    private static final Pattern playerPattern = Pattern.compile("GameAccountId=\\[hi=(.*?) lo=(.*?)]");
    private static final String PLAYER = "Player";
    private static final String TAG = "tag";

    @Override
    protected boolean supportsLine(GameState gameState, String line) {
        return line.startsWith(PLAYER) || gameState.isCreatePlayer();
    }

    @Override
    public boolean handle(GameState gameState, LogLineData logLineData) {
        String line = logLineData.getTrimmedLine();
        if (gameState.isCreatePlayer() && line.startsWith(PLAYER)) { // we found the 2nd player
            gameState.endCreatePlayer(logLineData.getDateTime());
            String[] data = getGameAccountInfo(line);
            gameState.startCreatePlayer(logLineData.getDateTime(), data);
        } else if (gameState.isCreatePlayer() && line.startsWith(TAG)) { // in progress creating player populate some data
            Map<String, String> data = getKeyValueData(line, tagPattern);
            gameState.updateCreatePlayer(data);
        } else if (line.startsWith(PLAYER)) { // first time we found a player so create a new one and flag that we are creating players
            String[] data = getGameAccountInfo(line);
            gameState.startCreatePlayer(logLineData.getDateTime(), data);
        } else { // we were creating player but found a line that meant to be doing something else
            gameState.endCreatePlayer(logLineData.getDateTime());
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
