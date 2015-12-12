package com.hearthgames.server.service;

import com.hearthgames.server.game.analysis.*;
import com.hearthgames.server.game.analysis.domain.TurnInfo;
import com.hearthgames.server.game.analysis.domain.VersusInfo;
import com.hearthgames.server.game.analysis.domain.generic.GenericTable;
import com.hearthgames.server.game.parse.GameContext;
import com.hearthgames.server.game.play.GameResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameAnalysisService {

    private ManaSummaryAnalyzer manaSummaryAnalyzer;
    private HealthArmorAnalyzer healthArmorAnalyzer;
    private VersusInfoAnalyzer versusInfoAnalyzer;
    private CardSummaryAnalyzer cardSummaryAnalyzer;
    private BoardControlAnalyzer boardControlAnalyzer;
    private CardAdvantageAnalyzer cardAdvantageAnalyzer;
    private TurnInfoAnalyzer turnInfoAnalyzer;
    private TradeAnalyzer tradeAnalyzer;

    @Autowired
    public GameAnalysisService(ManaSummaryAnalyzer manaSummaryAnalyzer,
                               HealthArmorAnalyzer healthArmorAnalyzer,
                               VersusInfoAnalyzer versusInfoAnalyzer,
                               CardSummaryAnalyzer cardSummaryAnalyzer,
                               BoardControlAnalyzer boardControlAnalyzer,
                               CardAdvantageAnalyzer cardAdvantageAnalyzer,
                               TurnInfoAnalyzer turnInfoAnalyzer,
                               TradeAnalyzer tradeAnalyzer) {
        this.manaSummaryAnalyzer = manaSummaryAnalyzer;
        this.healthArmorAnalyzer = healthArmorAnalyzer;
        this.versusInfoAnalyzer = versusInfoAnalyzer;
        this.cardSummaryAnalyzer = cardSummaryAnalyzer;
        this.boardControlAnalyzer = boardControlAnalyzer;
        this.cardAdvantageAnalyzer = cardAdvantageAnalyzer;
        this.turnInfoAnalyzer = turnInfoAnalyzer;
        this.tradeAnalyzer = tradeAnalyzer;
    }

    public VersusInfo getVersusInfo(GameResult result, GameContext context) {
        return versusInfoAnalyzer.analyze(result, context);
    }

    public List<GenericTable> getHealthArmor(GameResult result, GameContext context) {
        return healthArmorAnalyzer.analyze(result, context);
    }

    public GenericTable getManaInfo(GameResult result, GameContext context) {
        return manaSummaryAnalyzer.analyze(result, context);
    }

    public GenericTable getCardSummary(GameResult result, GameContext context) {
        return cardSummaryAnalyzer.analyze(result, context);
    }

    public List<GenericTable> getBoardControl(GameResult result, GameContext context) {
        return boardControlAnalyzer.analyze(result, context);
    }

    public List<GenericTable> getCardAdvantage(GameResult result, GameContext context) {
        return cardAdvantageAnalyzer.analyze(result, context);
    }

    public List<TurnInfo> getTurnInfo(GameResult result, GameContext context) {
        return turnInfoAnalyzer.analyze(result, context);
    }

    public GenericTable getTradeInfo(GameResult result, GameContext context) {
        return tradeAnalyzer.analyze(result, context);
    }
}
