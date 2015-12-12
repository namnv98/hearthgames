package com.hearthgames.server.game.parse.handler;

import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameContext;

import java.util.Map;
import java.util.regex.Pattern;

public class UpdateGameEntityHandler extends AbstractHandler {

    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");
    private static final String TAG_CHANGE_ENTITY_GAME_ENTITY = "TAG_CHANGE Entity=GameEntity";
    private static final String STATE = "state";
    private static final String COMPLETE = "COMPLETE";
    private static final String RUNNING = "RUNNING";

    @Override
    public boolean supports(GameContext context, String line) {
        return line != null && context != null && line.startsWith(TAG_CHANGE_ENTITY_GAME_ENTITY);
    }

    @Override
    public boolean handle(GameContext context, LogLineData logLineData) {
        String line = logLineData.getTrimmedLine();
        Map<String, String> data = getKeyValueData(line, tagPattern);
        if (data.get(STATE) != null && data.get(STATE).equals(COMPLETE)) {  // TAG_CHANGE Entity=GameEntity tag=STATE value=COMPLETE
            context.endUpdateGame(logLineData.getDateTime(), data);
        } else if (data.get(STATE) != null && data.get(STATE).equals(RUNNING)) {  // TAG_CHANGE Entity=GameEntity tag=STATE value=RUNNING
            context.startUpdateGame(logLineData.getDateTime(), data);
        } else if (context.isGameUpdating()) {
            context.updateCurrentGame(logLineData.getDateTime(), data);
        } else { // Game hasn't started so let's fill the initial match data
            context.populateEntity(context.getGameEntity(), data);
        }
        return true;
    }
}
