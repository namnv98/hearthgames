package com.hearthlogs.server.service;

import com.hearthlogs.server.service.log.handler.*;
import com.hearthlogs.server.match.MatchContext;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.InflaterInputStream;

@Component
public class MatchParserService {

    private static final Logger logger = LoggerFactory.getLogger(MatchParserService.class);

    private List<AbstractHandler> handlers = new ArrayList<>();

    public MatchParserService() {
        handlers.add(new CreateGameHandler());
        handlers.add(new CreatePlayerHandler());
        handlers.add(new UpdatePlayerHandler());
        handlers.add(new CreateCardHandler());
        handlers.add(new UpdateGameHandler());
        handlers.add(new UpdateCardHandler());
        handlers.add(new TagChangeHandler());
        handlers.add(new CreateActionHandler());
    }

    public List<String> deserializeGame(byte[] rawData) {
        String game = decompressGameData(rawData);
        String[] lines = game.split("\n");
        return Arrays.asList(lines);
    }

    public MatchContext processMatch(List<String> lines) {
        MatchContext context = new MatchContext();
        for (String line : lines) {
            context.setIndentLevel(line); // setting the level of indentation will help with cases where there are issues with the log file (i.e. an action_end is missing)
            parseLine(context, line.trim());
        }
        return context;
    }

    private String decompressGameData(byte[] data) {
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

    private void parseLine(MatchContext context, String line) {
        boolean processed = processLine(context, line);
        if (!processed) {
            parseLine(context, line);
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