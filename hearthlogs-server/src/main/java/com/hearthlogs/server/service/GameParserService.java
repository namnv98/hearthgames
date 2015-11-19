package com.hearthlogs.server.service;

import com.hearthlogs.server.game.parse.handler.*;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.log.domain.LogLineData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.InflaterInputStream;

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

    public List<String> deserializeGame(byte[] rawData) {
        String game = decompressGameData(rawData);
        String[] lines = game.split("\n");
        return Arrays.asList(lines);
    }

    public GameContext parseLines(List<LogLineData> logLineDatas) {
        GameContext context = new GameContext();
        for (LogLineData logLineData : logLineDatas) {
            context.setIndentLevel(logLineData.getLine()); // setting the level of indentation will help with cases where there are issues with the log file (i.e. an action_end is missing)
            parseLine(context, logLineData);
        }
        return context;
    }

    protected String decompressGameData(byte[] data) {
        try (
                InputStream is = new ByteArrayInputStream(data);
                InflaterInputStream iis = new InflaterInputStream(is);
                ByteArrayOutputStream baos = new ByteArrayOutputStream(512)
        ) {
            int b;
            while ((b = iis.read()) != -1) {
                baos.write(b);
            }
            return new String(baos.toByteArray(), "UTF-8");
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    protected void parseLine(GameContext context, LogLineData logLineData) {
        boolean processed = processLine(context, logLineData);
        if (!processed) {
            parseLine(context, logLineData);
        }
    }

    protected boolean processLine(GameContext context, LogLineData logLineData) {
        for (Handler handler: handlers) {
            if (handler.supports(context, logLineData.getTrimmedLine())) {
                return handler.handle(context, logLineData);
            }
        }
        return true;
    }
}