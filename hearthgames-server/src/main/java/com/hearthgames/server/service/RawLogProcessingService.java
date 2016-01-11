package com.hearthgames.server.service;

import com.hearthgames.server.game.log.domain.GameType;
import com.hearthgames.server.game.log.domain.LogLineData;
import com.hearthgames.server.game.log.domain.RawGameData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RawLogProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(RawLogProcessingService.class);

    private static final Pattern gameModePattern = Pattern.compile("\\[LoadingScreen\\] LoadingScreen.OnSceneLoaded\\(\\) - prevMode=(.*) currMode=(.*)");
    private static final Pattern medalRankPattern = Pattern.compile("name=Medal_Ranked_(.*) family");
    private static final String MEDAL_RANKED = "unloading name=Medal_Ranked";
    private static final String CREATE_GAME = "CREATE_GAME";
    private static final String GAME_STATE_COMPLETE = "TAG_CHANGE Entity=GameEntity tag=STATE value=COMPLETE";
    private static final String END_OF_LOGS_FOR_GAME_MARKER = "---RegisterFriendChallenge---";


    private static final String BOB = "[Bob]";
    private static final String LOADING_SCREEN = "[LoadingScreen]";

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

    public List<RawGameData> processLogFile(List<String> lines, int type) {

        List<RawGameData> rawGameDatas = new ArrayList<>();

        GameType gameType = type == -1 ? GameType.UNKNOWN : GameType.getGameType(type);

        boolean gameComplete = false;
        List<LogLineData> currentGame = new ArrayList<>();
        List<String> currentRawGame = new ArrayList<>();
        Integer rank = null;
        for (String rawLine: lines) {

            String timestamp = "";
            String lineWithoutTimestamp;
            String line;
            if (rawLine.contains(": [")) {
                timestamp = rawLine.substring(0, rawLine.indexOf(": ["));
                lineWithoutTimestamp = rawLine.substring(rawLine.indexOf("["));
                line = lineWithoutTimestamp;
            } else {
                line = rawLine; // For games that don't have timestamps
            }

            GameType detectedType = detectGameMode(line);
            if (detectedType != null) {
                gameType = detectedType;
            }

            if (line.startsWith(CREATE_GAME)) {
                addGameIfNotEmpty(currentGame, currentRawGame, rawGameDatas, rank, gameType);
                gameComplete = false;
                rank = null;
                currentGame = new ArrayList<>();
                currentRawGame = new ArrayList<>();
            } else if (line.startsWith(GAME_STATE_COMPLETE)) {
                gameComplete = true;
            } else if (gameComplete && line.startsWith(MEDAL_RANKED)) {
                int rankFound = getRank(line);
                if (rank == null || rankFound < rank) {
                    rank = rankFound;
                }
            }
            if (isLineLoggable(line)) {
                LogLineData data = new LogLineData(timestamp, line);
                currentGame.add(data);
                currentRawGame.add(rawLine);
            }

            if (gameComplete && line.startsWith(END_OF_LOGS_FOR_GAME_MARKER)) {
                // wait until register friend challenge for casual/rank mode games since the ranks are contained in log messages in between
                RawGameData rawGameData = createRawGameData(currentGame, currentRawGame, rank);
                rawGameData.setGameType(gameType);
                if (isGame(rawGameData)) {
                    rawGameDatas.add(rawGameData);
                }
                currentGame = new ArrayList<>();
                currentRawGame = new ArrayList<>();
            }
        }
        addGameIfNotEmpty(currentGame, currentRawGame, rawGameDatas, rank, gameType);

        return rawGameDatas;
    }

    private GameType detectGameMode(String line) {
        if (line.startsWith(BOB)) {
            if (line.contains(RANKED)) {
                return GameType.RANKED;
            } else if (line.contains(ARENA_GAME)) {
                return GameType.ARENA;
            } else if (line.contains(PLAY_MODE)) {
                return GameType.CASUAL;
            } else if (line.contains(FRIEND_CHALLENGE)) {
                return GameType.FRIENDLY_CHALLENGE;
            } else if (line.contains(ADVENTURE_MODE)) {
                return GameType.ADVENTURE;
            }
        } else if (line.startsWith(LOADING_SCREEN)) {
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
        }
        return null;
    }

    private String getMode(String line) {
        String mode = null;
        Matcher matcher = gameModePattern.matcher(line);
        if (matcher.find()) {
            mode = matcher.group(2);
        }
        return mode;
    }

    private void addGameIfNotEmpty(List<LogLineData> currentGame, List<String> currentRawGame, List<RawGameData> rawGameDatas, Integer rank, GameType gameType) {
        if (!CollectionUtils.isEmpty(currentGame)) {
            RawGameData rawGameData = createRawGameData(currentGame, currentRawGame, rank);
            rawGameData.setGameType(gameType);
            if (rank != null) { // just a last test here because the [LoadingScreen] logger comes up at the very end and tells us the mode is TOURNAMENT which means casual
                rawGameData.setGameType(GameType.RANKED);
            }
            if (isGame(rawGameData)) {
                rawGameDatas.add(rawGameData);
            }
        }
    }

    private boolean isGame(RawGameData rawGameData) {
        boolean hasCreateGame = false;
        boolean hasCompleteState = false;
        if (rawGameData.getRawLines() != null && rawGameData.getRawLines().size() > 0) {
            for (String line: rawGameData.getRawLines()) {
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

    private RawGameData createRawGameData(List<LogLineData> currentGame, List<String> currentRawGame, Integer rank) {
        RawGameData rawGameData = new RawGameData();
        rawGameData.setLines(currentGame);
        rawGameData.setRawLines(currentRawGame);
        rawGameData.setRank(rank);
        return rawGameData;
    }

    private boolean isLineLoggable(String line) {
        if (StringUtils.isEmpty(line)) return false;
        if (line.startsWith("[Power] GameState.DebugPrintPower() - ")) return true;
        if (line.startsWith("[Asset] CachedAsset.UnloadAssetObject() - unloading name=Medal_Ranked")) return true;
        if (line.startsWith("[Bob] ---Register")) return true;
        if (line.startsWith("[LoadingScreen] LoadingScreen.OnSceneLoaded() - prevMode=")) return true;
        return false;
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

}
