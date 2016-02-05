package com.hearthgames.server.service;

import com.hearthgames.server.game.log.domain.GameLogger;
import com.hearthgames.server.game.log.domain.GameType;
import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.log.domain.RawGameData;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RawLogProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(RawLogProcessingService.class);

    private static final Pattern gameModePattern = Pattern.compile("\\[LoadingScreen\\] LoadingScreen.OnSceneLoaded\\(\\) - prevMode=(.*) currMode=(.*)");
    private static final Pattern gameModePattern2 = Pattern.compile("\\[LoadingScreen\\] LoadingScreen.OnScenePreUnload\\(\\) - prevMode=(.*) currMode=(.*) m_phase=.*");
    private static final Pattern medalRankPattern = Pattern.compile("name=Medal_Ranked_(.*) family");

    private static final Pattern arenaCardChoicePattern = Pattern.compile("\\[Arena\\] DraftManager.OnChoicesAndContents - Draft deck contains card (.*)");
    private static final Pattern arenaDeckIdPattern = Pattern.compile("\\[Arena\\] DraftManager.OnChoicesAndContents - Draft Deck ID: (.*), Hero Card = (.*)");

    private static final String CREATE_GAME = "CREATE_GAME";
    private static final String GAME_STATE_COMPLETE = "TAG_CHANGE Entity=GameEntity tag=STATE value=COMPLETE";
    private static final String END_OF_GAME_MODE_DETECTION = "---RegisterScreenBox---";
    private static final String END_OF_GAME = "---RegisterFriendChallenge---";

    private static final String RANKED = "unloading name=Medal_Ranked";
    private static final String ARENA_GAME = "---RegisterScreenForge---";
    private static final String PLAY_MODE = "---RegisterScreenTourneys---";
    private static final String FRIEND_CHALLENGE = "---RegisterScreenFriendly---";
    private static final String ADVENTURE_MODE = "lo=0]";
    private static final String TAVERN_BRAWL = "TAVERN_BRAWL";
    private static final String TOURNAMENT = "TOURNAMENT";
    private static final String FRIENDLY = "FRIENDLY";
    private static final String ADVENTURE = "ADVENTURE";
    private static final String DRAFT = "DRAFT";

    public boolean isHSReplayFile(String xml) {
        return xml != null && xml.contains("<?xml version");
    }

    public List<RawGameData> processHSReplayFile(String xml) {

        String[] rawGamesXml = xml.split("<Game ");
        List<String> rawGames = new ArrayList<>();
        for (String rawGame: rawGamesXml) {
            if (rawGame.startsWith("ts"))
                rawGames.add("<?xml version=\"1.0\" ?><Game "+rawGame);
        }
        List<RawGameData> rawGameDatas = new ArrayList<>();
        for (String rawGame: rawGames) {
            RawGameData rawGameData = new RawGameData();
            rawGameData.setGameType(GameType.CASUAL);
            rawGameData.setXml(rawGame);
            rawGameDatas.add(rawGameData);
        }
        return rawGameDatas;
    }

    public List<RawGameData> processLogFile(List<String> lines) {

        List<RawGameData> rawGameDatas = new ArrayList<>();

        GameType gameType = GameType.UNKNOWN;

        // TODO Need to purge the database of old games so that we don't have to do this check anymore
        boolean isLegacy = lines.get(0) != null & lines.get(0).startsWith(CREATE_GAME);

        boolean gameComplete = false;
        boolean gameModeDetectionComplete = false;
        List<LogLineData> currentGame = new ArrayList<>();
        List<String> currentRawGame = new ArrayList<>();
        Integer rank = null;
        int rankFoundCount = 0;

        String arenaDeckId = null;
        List<String> arenaDeckCards = new ArrayList<>();

        for (String rawLine: lines) {

            String timestamp = "";
            String lineWithoutTimestamp;
            String line;
            if (!StringUtils.isEmpty(rawLine) && Character.isDigit(rawLine.charAt(0)) && rawLine.contains(": [")) {
                timestamp = rawLine.substring(0, rawLine.indexOf(": ["));
                lineWithoutTimestamp = rawLine.substring(rawLine.indexOf("["));
                line = lineWithoutTimestamp;
            } else {
                line = rawLine; // For games that don't have timestamps
            }

            // TODO Need to purge the database of old games so that we don't have to do this check anymore
            if (GameLogger.isLineValid(line) || isLegacy) {

                if (!gameModeDetectionComplete || gameType == GameType.UNKNOWN) {
                    GameType detectedType = detectGameMode(line, gameComplete);
                    if (detectedType != null ) {
                        gameType = detectedType;
                    }
                }

                if (line.contains(CREATE_GAME)) {
                    addGameIfNotEmpty(currentGame, currentRawGame, rawGameDatas, rank, gameType, arenaDeckId, arenaDeckCards);
                    gameComplete = false;
                    gameModeDetectionComplete = false;
                    rankFoundCount = 0;
                    rank = null;
                    currentGame = new ArrayList<>();
                    currentRawGame = new ArrayList<>();
                    arenaDeckId = null;
                    arenaDeckCards = new ArrayList<>();
                } else if (line.contains(GAME_STATE_COMPLETE)) {
                    gameComplete = true;
                } else if (gameComplete && line.contains(RANKED)) {
                    rankFoundCount++;
                    int rankFound = getRank(line);
                    if (rank == null || rankFound < rank) {
                        rank = rankFound;
                    }
                } else if (line.contains(END_OF_GAME_MODE_DETECTION)) {
                    // the log can trigger end of game mode detection even when a game hasn't started so we need
                    // to check is the game we are recording is actually a full game
                    if (isGame(currentRawGame)) {
                        gameModeDetectionComplete = true;
                    }
                } else if (line.contains("[Arena]") && line.contains("Draft deck contains")) {
                    String cardId = getArenaCardChoice(line);
                    if (cardId != null) {
                        arenaDeckCards.add(cardId);
                    }
                } else if (line.contains("[Arena]") && line.contains("Draft Deck ID:")) {
                    String deckId = getArenaDeckId(line);
                    if (deckId != null) {
                        arenaDeckId = deckId;
                    }
                }

                LogLineData data = new LogLineData(timestamp, line);
                currentGame.add(data);
                currentRawGame.add(rawLine);

                if (gameComplete && line.contains(END_OF_GAME)) {
                    // wait until register friend challenge for casual/rank mode games since the ranks are contained in log messages in between
                    rank = rankFoundCount > 1 ? rank : null;
                    RawGameData rawGameData = createRawGameData(currentGame, currentRawGame, rank, arenaDeckId, arenaDeckCards);
                    rawGameData.setGameType(gameType);
                    if (isGame(rawGameData.getRawLines())) {
                        rawGameDatas.add(rawGameData);
                    }
                    currentGame = new ArrayList<>();
                    currentRawGame = new ArrayList<>();
                }
            }
        }
        rank = rankFoundCount > 1 ? rank : null;
        addGameIfNotEmpty(currentGame, currentRawGame, rawGameDatas, rank, gameType, arenaDeckId, arenaDeckCards);

        return rawGameDatas;
    }

    private GameType detectGameMode(String line, boolean gameComplete) {
        if (line.startsWith(GameLogger.Asset.getName())) {
            // check if this happens only once the game completes
            if (gameComplete && line.contains(RANKED)) {
                return GameType.RANKED;
            }
        } else if (line.startsWith(GameLogger.Bob.getName())) {
            if (line.contains(ARENA_GAME)) {
                return GameType.ARENA;
            } else if (line.contains(PLAY_MODE)) {
                return GameType.CASUAL;
            } else if (line.contains(FRIEND_CHALLENGE)) {
                return GameType.FRIENDLY_CHALLENGE;
            }
        } else if (line.startsWith(GameLogger.LoadingScreen.getName())) {
            String mode = getMode(line);
            if (mode != null) {
                if (TAVERN_BRAWL.equals(mode)) {
                    return GameType.TAVERN_BRAWL;
                } else if (TOURNAMENT.equals(mode)) {
                    return GameType.CASUAL;
                } else if (FRIENDLY.equals(mode)) {
                    return GameType.FRIENDLY_CHALLENGE;
                } else if (ADVENTURE.equals(mode)) {
                    return GameType.ADVENTURE;
                } else  if (DRAFT.equals(mode)) {
                    return GameType.ARENA;
                }
            }
        } else if (line.contains(ADVENTURE_MODE)) {
            return GameType.ADVENTURE;
        }
        return null;
    }

    private String getMode(String line) {
        String mode = null;
        Matcher matcher = gameModePattern.matcher(line);
        if (matcher.find()) {
            mode = matcher.group(2);
        } else {
            matcher = gameModePattern2.matcher(line);
            if (matcher.find()) {
                mode = matcher.group(2);
            }
        }
        return mode;
    }

    private void addGameIfNotEmpty(List<LogLineData> currentGame, List<String> currentRawGame, List<RawGameData> rawGameDatas,
                                   Integer rank, GameType gameType, String arenaDeckId, List<String> arenaDeckCards) {
        if (!CollectionUtils.isEmpty(currentGame)) {
            RawGameData rawGameData = createRawGameData(currentGame, currentRawGame, rank, arenaDeckId, arenaDeckCards);
            rawGameData.setGameType(gameType);
            if (rank != null) { // just a last test here because the [LoadingScreen] logger comes up at the very end and tells us the mode is TOURNAMENT which means casual
                rawGameData.setGameType(GameType.RANKED);
            }
            if (isGame(rawGameData.getRawLines())) {
                rawGameDatas.add(rawGameData);
            }
        }
    }

    private boolean isGame(List<String> rawLines) {
        boolean hasCreateGame = false;
        boolean hasCompleteState = false;
        if (rawLines != null && !rawLines.isEmpty()) {
            for (String line: rawLines) {
                if (line.contains(CREATE_GAME)) {
                    hasCreateGame = true;
                } else if (line.contains(GAME_STATE_COMPLETE)) {
                    hasCompleteState = true;
                }
                if (hasCreateGame && hasCompleteState) {
                    return true;
                }
            }
        }
        return false;
    }

    private RawGameData createRawGameData(List<LogLineData> currentGame, List<String> currentRawGame, Integer rank,
                                          String arenaDeckId, List<String> arenaDeckCards) {
        RawGameData rawGameData = new RawGameData();
        rawGameData.setLines(currentGame);
        rawGameData.setRawLines(currentRawGame);
        rawGameData.setRank(rank);
        rawGameData.setArenaDeckId(arenaDeckId);
        rawGameData.setArenaDeckCards(arenaDeckCards);
        return rawGameData;
    }

    // These are the core loggers needed...Arena, Rachelle and Achievements are optional
    public boolean doesLogFileContainAllLoggers(String logfile) {
        return logfile != null &&
                logfile.contains(GameLogger.Bob.getName()) &&
                logfile.contains(GameLogger.Asset.getName()) &&
                logfile.contains(GameLogger.Power.getName()) &&
                logfile.contains(GameLogger.LoadingScreen.getName());
    }

    private int getRank(String line) {
        int rank = 0;
        Matcher matcher = medalRankPattern.matcher(line);
        if (matcher.find()) {
            String r = null;
            try {
                r = matcher.group(1);
                rank = Integer.parseInt(r);
            } catch (NumberFormatException e) {
                logger.warn("Found a rank that was not parseable : " + r);
            }
        }
        return rank;
    }

    private String getArenaCardChoice(String line) {
        String cardId = null;
        Matcher matcher = arenaCardChoicePattern.matcher(line);
        if (matcher.find()) {
            cardId = matcher.group(1);
        }
        return cardId;
    }

    private String getArenaDeckId(String line) {
        String cardId = null;
        Matcher matcher = arenaDeckIdPattern.matcher(line);
        if (matcher.find()) {
            cardId = matcher.group(1);
        }
        return cardId;
    }
}
