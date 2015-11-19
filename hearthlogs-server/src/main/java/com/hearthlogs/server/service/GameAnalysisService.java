package com.hearthlogs.server.service;

import com.hearthlogs.server.game.analysis.*;
import com.hearthlogs.server.game.analysis.domain.*;
import com.hearthlogs.server.game.parse.GameContext;
import com.hearthlogs.server.game.play.GameResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameAnalysisService {

    private ManaInfoAnalyzer manaInfoAnalyzer;
    private HealthArmorInfoAnalyzer healthArmorInfoAnalyzer;
    private VersusInfoAnalyzer versusInfoAnalyzer;
    private CardInfoAnalyzer cardInfoAnalyzer;
    private BoardControlInfoAnalyzer boardControlInfoAnalyzer;
    private CardAdvantageInfoAnalyzer cardAdvantageInfoAnalyzer;

    @Autowired
    public GameAnalysisService(ManaInfoAnalyzer manaInfoAnalyzer,
                               HealthArmorInfoAnalyzer healthArmorInfoAnalyzer,
                               VersusInfoAnalyzer versusInfoAnalyzer,
                               CardInfoAnalyzer cardInfoAnalyzer,
                               BoardControlInfoAnalyzer boardControlInfoAnalyzer,
                               CardAdvantageInfoAnalyzer cardAdvantageInfoAnalyzer) {
        this.manaInfoAnalyzer = manaInfoAnalyzer;
        this.healthArmorInfoAnalyzer = healthArmorInfoAnalyzer;
        this.versusInfoAnalyzer = versusInfoAnalyzer;
        this.cardInfoAnalyzer = cardInfoAnalyzer;
        this.boardControlInfoAnalyzer = boardControlInfoAnalyzer;
        this.cardAdvantageInfoAnalyzer = cardAdvantageInfoAnalyzer;
    }

    public VersusInfo getVersusInfo(GameResult result, GameContext context) {
        return versusInfoAnalyzer.analyze(result, context);
    }

    public List<HealthArmorInfo> getHealthArmorInfo(GameResult result, GameContext context) {
        return healthArmorInfoAnalyzer.analyze(result, context);

    }

    public ManaInfo getManaInfo(GameResult result, GameContext context) {
        return manaInfoAnalyzer.analyze(result, context);
    }

    public CardInfo getCardInfo(GameResult result, GameContext context) {
        return cardInfoAnalyzer.analyze(result, context);
    }

    public List<BoardControlInfo> getBoardControlInfo(GameResult result, GameContext context) {
        return boardControlInfoAnalyzer.analyze(result, context);
    }

    public List<CardAdvantageInfo> getCardAdvantageInfo(GameResult result, GameContext context) {
        return cardAdvantageInfoAnalyzer.analyze(result, context);
    }

}
