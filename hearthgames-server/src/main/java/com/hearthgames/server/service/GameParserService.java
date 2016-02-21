package com.hearthgames.server.service;

import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.parse.GameState;
import com.hearthgames.server.game.parse.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameParserService {

    private static final Logger logger = LoggerFactory.getLogger(GameParserService.class);

    protected List<AbstractHandler> handlers = new ArrayList<>();

    public GameParserService() {
        handlers.add(new CreateGameEntityHandler());
        handlers.add(new CreatePlayerHandler());
        handlers.add(new UpdatePlayerHandler());
        handlers.add(new CreateCardHandler());
        handlers.add(new UpdateGameEntityHandler());
        handlers.add(new UpdateCardHandler());
        handlers.add(new TagChangeHandler());
        handlers.add(new CreateActionHandler());
    }

    public GameState parseLines(List<LogLineData> logLineDatas) {
        GameState gameState = new GameState();
        for (LogLineData logLineData : logLineDatas) {
            gameState.setIndentLevel(logLineData.getLine()); // setting the level of indentation will help with cases where there are issues with the log file (i.e. an action_end is missing)
            parseLine(gameState, logLineData);
        }
        return gameState;
    }


    protected void parseLine(GameState gameState, LogLineData logLineData) {
        boolean processed = processLine(gameState, logLineData);
        if (!processed) {
            parseLine(gameState, logLineData);
        }
    }

    protected boolean processLine(GameState gameState, LogLineData logLineData) {
        for (Handler handler: handlers) {
            if (handler.supports(gameState, logLineData.getTrimmedLine())) {
                return handler.handle(gameState, logLineData);
            }
        }
        return true;
    }
}