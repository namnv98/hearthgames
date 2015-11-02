package com.hearthlogs.server.match.parse.handler;

import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.raw.domain.LogLineData;

import java.util.Map;
import java.util.regex.Pattern;

public class CreateGameHandler extends AbstractHandler {

    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");
    private static final String GAME_ENTITY = "GameEntity";
    private static final String TAG = "tag";

    @Override
    public boolean supports(ParsedMatch parsedMatch, String line) {
        return line != null && parsedMatch != null && (line.startsWith(GAME_ENTITY) || parsedMatch.isCreateGameEntity());
    }

    @Override
    public boolean handle(ParsedMatch context, LogLineData logLineData) {
        String line = logLineData.getTrimmedLine();
        if (context.isCreateGameEntity() && line.startsWith(TAG)) { // are we populating match data?
            Map<String, String> data = getKeyValueData(line, tagPattern);
            context.updateCreateGame(data);
        } else if (!context.isCreateGameEntity() && line.startsWith(GAME_ENTITY)) { // found the directive to create a match
            context.startCreateGame();
        } else { // we must have found something else to create/populate
            context.endCreateGame(logLineData.getDateTime());
            return false;
        }
        return true;
    }
}
