package com.hearthgames.server.game.parse.handler;

import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameContext;

import java.util.Map;
import java.util.regex.Pattern;

public class CreateCardHandler extends AbstractHandler {

    private static final String CREATE_CARD = "FULL_ENTITY - Creating";
    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");
    private static final Pattern cardPattern = Pattern.compile("(CardID)=(.*)");
    private static final String TAG = "tag";

    @Override
    protected boolean supportsLine(GameContext context, String line) {
        return line.startsWith(CREATE_CARD) || context.isCreateCard();
    }

    @Override
    public boolean handle(GameContext context, LogLineData logLineData) {
        String line = logLineData.getTrimmedLine();
        if (context.isCreateCard() && line.startsWith(CREATE_CARD)) { // we found back to back cards to add
            Map<String, String> data = getKeyValueData(line, cardPattern);
            context.endCreateCard(logLineData.getDateTime());
            context.startCreateCard(logLineData.getDateTime(), data);
        } else if (context.isCreateCard() && line.startsWith(TAG)) { // in progress creating card populate some data
            Map<String, String> data = getKeyValueData(line, tagPattern);
            context.updateCreateCard(data);
        } else if (line.startsWith(CREATE_CARD)) { // first time we found a card so create a new one and flag that we are creating cards
            Map<String, String> data = getKeyValueData(line, cardPattern);
            context.startCreateCard(logLineData.getDateTime(), data);
        } else { // we were creating a card but found a line that meant to be doing something else
            context.endCreateCard(logLineData.getDateTime());
            return false;
        }
        return true;
    }
}