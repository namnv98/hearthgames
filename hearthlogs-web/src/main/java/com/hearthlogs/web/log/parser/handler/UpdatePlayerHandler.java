package com.hearthlogs.web.log.parser.handler;

import com.hearthlogs.web.match.MatchContext;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdatePlayerHandler extends Handler {

    private static final Pattern playInfoPattern = Pattern.compile("TAG_CHANGE Entity=(.*?) tag=TIMEOUT value");
    private static final Pattern entityPattern = Pattern.compile("TAG_CHANGE Entity=(.*?) tag=");
    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");
    private static final String GAME_ENTITY = "GameEntity";

    @Override
    public boolean applies(MatchContext context, String line) {
        // We are looking for the following line:
        // TAG_CHANGE Entity=<Player Name> tag=TIMEOUT value=75
        // This tells us that additional properties are being populated on players.  It is the first time we see
        // the player's name.  Unfortunately we have to wait till all the tag changes are complete
        // to find out what the player id is.
        return line != null && context != null && !context.isGameRunning() && (getPlayerName(line) != null || context.isUpdatePlayer());
    }

    @Override
    public boolean handle(MatchContext context, String line) {
        String playerName = getPlayerName(line);
        String entityStr = getEntityStr(line);
        if (entityStr == null || entityStr.equals(GAME_ENTITY)) { // Player updates are always followed by an update to the GameEntity...at least so far
            // we were updating a player but found a line that meant to be doing something else
            context.endUpdatePlayer();
            return false;
        } else {
            Map<String, String> data = getKeyValueData(line, tagPattern);
            if (context.isUpdatePlayer() && playerName != null && context.getCurrentPlayerName() != null && !playerName.equals(context.getCurrentPlayerName())) { // we have found a 2nd player
                context.endUpdatePlayer();
                context.startUpdatePlayer(playerName, data);
            } else if (context.isUpdatePlayer() && context.getCurrentPlayerName() != null && entityStr.equals(context.getCurrentPlayerName())) {
                // Found data to update on a player.  Also assuming that we are updating a player until we see an update to the GameEntity
                context.updateCurrentPlayer(data);
            } else if (playerName != null) { // first time finding a player to update
                context.startUpdatePlayer(playerName, data);
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