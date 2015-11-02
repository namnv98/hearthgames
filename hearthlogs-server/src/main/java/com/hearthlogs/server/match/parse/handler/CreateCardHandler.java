package com.hearthlogs.server.match.parse.handler;

import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.raw.domain.LogLineData;

import java.util.Map;
import java.util.regex.Pattern;

public class CreateCardHandler extends AbstractHandler {

    private static final String CREATE_CARD = "FULL_ENTITY - Creating";
    private static final Pattern tagPattern = Pattern.compile("tag=(.*?) value=(.*)");
    private static final Pattern cardPattern = Pattern.compile("(CardID)=(.*)");
    private static final String TAG = "tag";

    @Override
    public boolean supports(ParsedMatch parsedMatch, String line) {
        return line != null && parsedMatch != null && (line.startsWith(CREATE_CARD) || parsedMatch.isCreateCard());
    }

    @Override
    public boolean handle(ParsedMatch parsedMatch, LogLineData logLineData) {
        String line = logLineData.getTrimmedLine();
        if (parsedMatch.isCreateCard() && line.startsWith(CREATE_CARD)) { // we found back to back cards to add
            Map<String, String> data = getKeyValueData(line, cardPattern);
            parsedMatch.endCreateCard(logLineData.getDateTime());
            parsedMatch.startCreateCard(logLineData.getDateTime(), data);
        } else if (parsedMatch.isCreateCard() && line.startsWith(TAG)) { // in progress creating card populate some data
            Map<String, String> data = getKeyValueData(line, tagPattern);
            parsedMatch.updateCreateCard(data);
        } else if (line.startsWith(CREATE_CARD)) { // first time we found a card so create a new one and flag that we are creating cards
            Map<String, String> data = getKeyValueData(line, cardPattern);
            parsedMatch.startCreateCard(logLineData.getDateTime(), data);
        } else { // we were creating a card but found a line that meant to be doing something else
            parsedMatch.endCreateCard(logLineData.getDateTime());
            return false;
        }
        return true;
    }
}