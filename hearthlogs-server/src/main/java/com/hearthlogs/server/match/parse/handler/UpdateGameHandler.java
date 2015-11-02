package com.hearthlogs.server.match.parse.handler;

import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.raw.domain.LogLineData;

import java.util.Map;
import java.util.regex.Pattern;

public class UpdateGameHandler extends AbstractHandler {

    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");
    private static final String TAG_CHANGE_ENTITY_GAME_ENTITY = "TAG_CHANGE Entity=GameEntity";
    private static final String STATE = "state";
    private static final String COMPLETE = "COMPLETE";
    private static final String RUNNING = "RUNNING";

    @Override
    public boolean supports(ParsedMatch parsedMatch, String line) {
        return line != null && parsedMatch != null && line.startsWith(TAG_CHANGE_ENTITY_GAME_ENTITY);
    }

    @Override
    public boolean handle(ParsedMatch parsedMatch, LogLineData logLineData) {
        String line = logLineData.getTrimmedLine();
        Map<String, String> data = getKeyValueData(line, tagPattern);
        if (data.get(STATE) != null && data.get(STATE).equals(COMPLETE)) {  // TAG_CHANGE Entity=GameEntity tag=STATE value=COMPLETE
            parsedMatch.endUpdateGame(logLineData.getDateTime(), data);
        } else if (data.get(STATE) != null && data.get(STATE).equals(RUNNING)) {  // TAG_CHANGE Entity=GameEntity tag=STATE value=RUNNING
            parsedMatch.startUpdateGame(logLineData.getDateTime(), data);
        } else if (parsedMatch.isGameUpdating()) {
            parsedMatch.updateCurrentGame(logLineData.getDateTime(), data);
        } else { // Game hasn't started so let's fill the initial match data
            parsedMatch.populateEntity(parsedMatch.getGame(), data);
        }
        return true;
    }
}
