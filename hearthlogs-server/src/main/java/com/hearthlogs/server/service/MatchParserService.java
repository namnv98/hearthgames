package com.hearthlogs.server.service;

import com.hearthlogs.server.match.parse.handler.*;
import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.raw.domain.LogLineData;
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

    protected List<AbstractHandler> handlers = new ArrayList<>();

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

    public ParsedMatch parseLines(List<LogLineData> logLineDatas) {
        ParsedMatch parsedMatch = new ParsedMatch();
        for (LogLineData logLineData : logLineDatas) {
            parsedMatch.setIndentLevel(logLineData.getLine()); // setting the level of indentation will help with cases where there are issues with the log file (i.e. an action_end is missing)
            parseLine(parsedMatch, logLineData);
        }
        return parsedMatch;
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

    protected void parseLine(ParsedMatch parsedMatch, LogLineData logLineData) {
        boolean processed = processLine(parsedMatch, logLineData);
        if (!processed) {
            parseLine(parsedMatch, logLineData);
        }
    }

    protected boolean processLine(ParsedMatch parsedMatch, LogLineData logLineData) {
        for (Handler handler: handlers) {
            if (handler.supports(parsedMatch, logLineData.getTrimmedLine())) {
                return handler.handle(parsedMatch, logLineData);
            }
        }
        return true;
    }
}