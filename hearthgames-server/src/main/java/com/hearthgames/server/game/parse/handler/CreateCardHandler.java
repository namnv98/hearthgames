package com.hearthgames.server.game.parse.handler;

import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameState;

import java.util.Map;
import java.util.regex.Pattern;

public class CreateCardHandler extends AbstractHandler {

    private static final String CREATE_CARD = "FULL_ENTITY - Creating";
    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");
    private static final Pattern cardPattern = Pattern.compile("(CardID)=(.*)");
    private static final String TAG = "tag";

    @Override
    protected boolean supportsLine(GameState gameState, String line) {
        return line.startsWith(CREATE_CARD) || gameState.isCreateCard();
    }

    @Override
    public boolean handle(GameState gameState, LogLineData logLineData) {
        String line = logLineData.getTrimmedLine();
        if (gameState.isCreateCard() && line.startsWith(CREATE_CARD)) { // we found back to back cards to add
            Map<String, String> data = getKeyValueData(line, cardPattern);
            gameState.endCreateCard(logLineData.getDateTime());
            gameState.startCreateCard(logLineData.getDateTime(), data);
        } else if (gameState.isCreateCard() && line.startsWith(TAG)) { // in progress creating card populate some data
            Map<String, String> data = getKeyValueData(line, tagPattern);
            gameState.updateCreateCard(data);
        } else if (line.startsWith(CREATE_CARD)) { // first time we found a card so create a new one and flag that we are creating cards
            Map<String, String> data = getKeyValueData(line, cardPattern);
            gameState.startCreateCard(logLineData.getDateTime(), data);
        } else { // we were creating a card but found a line that meant to be doing something else
            gameState.endCreateCard(logLineData.getDateTime());
            return false;
        }
        return true;
    }
}