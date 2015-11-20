package com.hearthlogs.server.service;

import com.hearthlogs.server.game.analysis.*;
import com.hearthlogs.server.game.analysis.domain.*;
import com.hearthlogs.server.game.analysis.domain.generic.GenericTable;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.play.GameResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameAnalysisService {

    private ManaInfoAnalyzer manaInfoAnalyzer;
    private HealthArmorAnalyzer healthArmorAnalyzer;
    private VersusInfoAnalyzer versusInfoAnalyzer;
    private CardSummaryAnalyzer cardSummaryAnalyzer;
    private BoardControlAnalyzer boardControlAnalyzer;
    private CardAdvantageAnalyzer cardAdvantageAnalyzer;
    private TurnInfoAnalyzer turnInfoAnalyzer;

    @Autowired
    public GameAnalysisService(ManaInfoAnalyzer manaInfoAnalyzer,
                               HealthArmorAnalyzer healthArmorAnalyzer,
                               VersusInfoAnalyzer versusInfoAnalyzer,
                               CardSummaryAnalyzer cardSummaryAnalyzer,
                               BoardControlAnalyzer boardControlAnalyzer,
                               CardAdvantageAnalyzer cardAdvantageAnalyzer,
                               TurnInfoAnalyzer turnInfoAnalyzer) {
        this.manaInfoAnalyzer = manaInfoAnalyzer;
        this.healthArmorAnalyzer = healthArmorAnalyzer;
        this.versusInfoAnalyzer = versusInfoAnalyzer;
        this.cardSummaryAnalyzer = cardSummaryAnalyzer;
        this.boardControlAnalyzer = boardControlAnalyzer;
        this.cardAdvantageAnalyzer = cardAdvantageAnalyzer;
        this.turnInfoAnalyzer = turnInfoAnalyzer;
    }

    public VersusInfo getVersusInfo(GameResult result, GameContext context) {
        return versusInfoAnalyzer.analyze(result, context);
    }

    public List<GenericTable> getHealthArmor(GameResult result, GameContext context) {
        return healthArmorAnalyzer.analyze(result, context);
    }

    public ManaInfo getManaInfo(GameResult result, GameContext context) {
        return manaInfoAnalyzer.analyze(result, context);
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
}
