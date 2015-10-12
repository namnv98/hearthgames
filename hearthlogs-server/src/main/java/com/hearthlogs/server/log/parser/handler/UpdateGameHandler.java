package com.hearthlogs.server.log.parser.handler;

import com.hearthlogs.server.match.MatchContext;

import java.util.Map;
import java.util.regex.Pattern;

public class UpdateGameHandler extends AbstractHandler {

    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");
    private static final String TAG_CHANGE_ENTITY_GAME_ENTITY = "TAG_CHANGE Entity=GameEntity";
    private static final String STATE = "state";
    private static final String COMPLETE = "COMPLETE";
    private static final String RUNNING = "RUNNING";

    @Override
    public boolean applies(MatchContext context, String line) {
        return line != null && context != null && line.startsWith(TAG_CHANGE_ENTITY_GAME_ENTITY);
    }

    @Override
    public boolean handle(MatchContext context, String line) {
        Map<String, String> data = getKeyValueData(line, tagPattern);
        if (data.get(STATE) != null && data.get(STATE).equals(COMPLETE)) {  // TAG_CHANGE Entity=GameEntity tag=STATE value=COMPLETE
            context.endUpdateGame(data);
        } else if (data.get(STATE) != null && data.get(STATE).equals(RUNNING)) {  // TAG_CHANGE Entity=GameEntity tag=STATE value=RUNNING
            context.startUpdateGame(data);
        } else if (context.isGameRunning()) {
            context.updateCurrentGame(data);
        } else { // Game hasn't started so let's fill the initial match data
            context.populateEntity(context.getGame(), data);
        }
        return true;
    }
}
