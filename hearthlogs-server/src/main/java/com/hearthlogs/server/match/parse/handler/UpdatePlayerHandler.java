package com.hearthlogs.server.match.parse.handler;

import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.raw.domain.LogLineData;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdatePlayerHandler extends AbstractHandler {

    private static final Pattern playInfoPattern = Pattern.compile("TAG_CHANGE Entity=(.*?) tag=TIMEOUT value");
    private static final Pattern entityPattern = Pattern.compile("TAG_CHANGE Entity=(.*?) tag=");
    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");
    private static final String GAME_ENTITY = "GameEntity";

    @Override
    public boolean supports(ParsedMatch parsedMatch, String line) {
        // We are looking for the following line:
        // TAG_CHANGE Entity=<Player Name> tag=TIMEOUT value=75
        // This tells us that additional properties are being populated on players.  It is the first time we see
        // the player's name.  Unfortunately we have to wait till all the tag changes are complete
        // to find out what the player id is.
        return line != null && parsedMatch != null && !parsedMatch.isGameUpdating() && (getPlayerName(line) != null || parsedMatch.isUpdatePlayer());
    }

    @Override
    public boolean handle(ParsedMatch parsedMatch,  LogLineData logLineData) {
        String line = logLineData.getTrimmedLine();
        String playerName = getPlayerName(line);
        String entityStr = getEntityStr(line);
        if (entityStr == null || entityStr.equals(GAME_ENTITY)) { // Player updates are always followed by an update to the GameEntity...at least so far
            // we were updating a player but found a line that meant to be doing something else
            parsedMatch.endUpdatePlayer(logLineData.getDateTime());
            return false;
        } else {
            Map<String, String> data = getKeyValueData(line, tagPattern);
            if (parsedMatch.isUpdatePlayer() && playerName != null && parsedMatch.getCurrentPlayerName() != null && !playerName.equals(parsedMatch.getCurrentPlayerName())) { // we have found a 2nd player
                parsedMatch.endUpdatePlayer(logLineData.getDateTime());
                parsedMatch.startUpdatePlayer(logLineData.getDateTime(), playerName, data);
            } else if (parsedMatch.isUpdatePlayer() && parsedMatch.getCurrentPlayerName() != null && entityStr.equals(parsedMatch.getCurrentPlayerName())) {
                // Found data to update on a player.  Also assuming that we are updating a player until we see an update to the GameEntity
                parsedMatch.updateCurrentPlayer(data);
            } else if (playerName != null) { // first time finding a player to update
                parsedMatch.startUpdatePlayer(logLineData.getDateTime(), playerName, data);
            }
        }
        return true;
    }

    private String getEntityStr(String line) {
        String match = null;
        Matcher matcher = entityPattern.matcher(line);
        if (matcher.find()) {
            match = matcher.group(1);
        }
        return match;
    }

    private String getPlayerName(String line) {
        String match = null;
        Matcher matcher = playInfoPattern.matcher(line);
        if (matcher.find()) {
            match = matcher.group(1);
        }
        return match;
    }
}