package com.hearthgames.server.game.parse.handler;

import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameState;

import java.util.Map;
import java.util.regex.Pattern;

public class CreateGameEntityHandler extends AbstractHandler {

    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");
    private static final String GAME_ENTITY = "GameEntity";
    private static final String TAG = "tag";

    @Override
    protected boolean supportsLine(GameState gameState, String line) {
        return line.startsWith(GAME_ENTITY) || gameState.isCreateGameEntity();
    }

    @Override
    public boolean handle(GameState gameState, LogLineData logLineData) {
        String line = logLineData.getTrimmedLine();
        if (gameState.isCreateGameEntity() && line.startsWith(TAG)) { // are we populating match data?
            Map<String, String> data = getKeyValueData(line, tagPattern);
            gameState.updateCreateGame(data);
        } else if (!gameState.isCreateGameEntity() && line.startsWith(GAME_ENTITY)) { // found the directive to create a match
            gameState.startCreateGame();
        } else { // we must have found something else to create/populate
            gameState.endCreateGame(logLineData.getDateTime());
            return false;
        }
        return true;
    }
}
