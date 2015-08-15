package com.hearthlogs.web.log.parser;

import com.hearthlogs.web.log.parser.handler.*;
import com.hearthlogs.web.match.MatchContext;
import com.hearthlogs.web.log.parser.handler.*;
import org.springframework.stereotype.Component;

@Component
public class MatchActivityParser {

    private CreateActionHandler createActionHandler = new CreateActionHandler();
    private CreateGameHandler createGameHandler = new CreateGameHandler();
    private CreatePlayerHandler createPlayerHandler = new CreatePlayerHandler();
    private CreateCardHandler createCardHandler = new CreateCardHandler();
    private TagChangeHandler tagChangeHandler = new TagChangeHandler();
    private UpdateGameHandler updateGameHandler = new UpdateGameHandler();
    private UpdatePlayerHandler updatePlayerHandler = new UpdatePlayerHandler();
    private UpdateCardHandler updateCardHandler = new UpdateCardHandler();

    public void parse(MatchContext context, String line) {
        boolean processed = processLine(context, line);
        if (!processed) {
            parse(context, line);
        }
    }

    private boolean processLine(MatchContext context, String line) {
        boolean processed = true;
        if (createGameHandler.applies(context, line)) {
            processed = createGameHandler.handle(context, line);
        } else if (createPlayerHandler.applies(context, line)) {
            processed = createPlayerHandler.handle(context, line);
        } else if (updatePlayerHandler.applies(context, line)) {
            processed = updatePlayerHandler.handle(context, line);
        } else if (createCardHandler.applies(context, line)) {
            processed = createCardHandler.handle(context, line);
        } else if (updateGameHandler.applies(context, line)) {
            processed = updateGameHandler.handle(context, line);
        } else if (updateCardHandler.applies(context, line)) {
            processed = updateCardHandler.handle(context, line);
        } else if (tagChangeHandler.applies(context, line)) {
            processed = tagChangeHandler.handle(context, line);
        } else if (createActionHandler.applies(context, line)) {
            processed = createActionHandler.handle(context, line);
        }
        return processed;
    }
}