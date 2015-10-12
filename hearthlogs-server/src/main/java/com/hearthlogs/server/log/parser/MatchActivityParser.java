package com.hearthlogs.server.log.parser;

import com.hearthlogs.server.log.parser.handler.*;
import com.hearthlogs.server.match.MatchContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MatchActivityParser {

    List<AbstractHandler> handlers = new ArrayList<>();

    public MatchActivityParser() {
        handlers.add(new CreateGameHandler());
        handlers.add(new CreatePlayerHandler());
        handlers.add(new UpdatePlayerHandler());
        handlers.add(new CreateCardHandler());
        handlers.add(new UpdateGameHandler());
        handlers.add(new UpdateCardHandler());
        handlers.add(new TagChangeHandler());
        handlers.add(new CreateActionHandler());
    }

    public void parse(MatchContext context, String line) {
        boolean processed = processLine(context, line);
        if (!processed) {
            parse(context, line);
        }
    }

    private boolean processLine(MatchContext context, String line) {
        for (Handler handler: handlers) {
            if (handler.applies(context, line)) {
                return handler.handle(context, line);
            }
        }
        return true;
    }
}