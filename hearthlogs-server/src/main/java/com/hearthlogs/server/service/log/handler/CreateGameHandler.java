package com.hearthlogs.server.service.log.handler;

import com.hearthlogs.server.match.MatchContext;

import java.util.Map;
import java.util.regex.Pattern;

public class CreateGameHandler extends AbstractHandler {

    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");
    private static final String GAME_ENTITY = "GameEntity";
    private static final String TAG = "tag";

    @Override
    public boolean applies(MatchContext context, String line) {
        return line != null && context != null && (line.startsWith(GAME_ENTITY) || context.isCreateGameEntity());
    }

    @Override
    public boolean handle(MatchContext context, String line) {
        if (context.isCreateGameEntity() && line.startsWith(TAG)) { // are we populating match data?
            Map<String, String> data = getKeyValueData(line, tagPattern);
            context.updateCreateGame(data);
        } else if (!context.isCreateGameEntity() && line.startsWith(GAME_ENTITY)) { // found the directive to create a match
            context.startCreateGame();
        } else { // we must have found something else to create/populate
            context.endCreateGame();
            return false;
        }
        return true;
    }
}
