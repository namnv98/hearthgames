package com.hearthlogs.server.service;

import com.hearthlogs.server.match.analysis.CardInfoAnalyzer;
import com.hearthlogs.server.match.analysis.HealthArmorInfoAnalyzer;
import com.hearthlogs.server.match.analysis.ManaInfoAnalyzer;
import com.hearthlogs.server.match.analysis.VersusInfoAnalyzer;
import com.hearthlogs.server.match.analysis.domain.CardInfo;
import com.hearthlogs.server.match.analysis.domain.ManaInfo;
import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.play.MatchResult;
import com.hearthlogs.server.match.analysis.domain.HealthArmorInfo;
import com.hearthlogs.server.match.analysis.domain.VersusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchAnalysisService {

    private ManaInfoAnalyzer manaInfoAnalyzer;
    private HealthArmorInfoAnalyzer healthArmorInfoAnalyzer;
    private VersusInfoAnalyzer versusInfoAnalyzer;
    private CardInfoAnalyzer cardInfoAnalyzer;

    @Autowired
    public MatchAnalysisService(ManaInfoAnalyzer manaInfoAnalyzer,
                                HealthArmorInfoAnalyzer healthArmorInfoAnalyzer,
                                VersusInfoAnalyzer versusInfoAnalyzer,
                                CardInfoAnalyzer cardInfoAnalyzer) {
        this.manaInfoAnalyzer = manaInfoAnalyzer;
        this.healthArmorInfoAnalyzer = healthArmorInfoAnalyzer;
        this.versusInfoAnalyzer = versusInfoAnalyzer;
        this.cardInfoAnalyzer = cardInfoAnalyzer;
    }

    public VersusInfo getVersusInfo(MatchResult result, ParseContext context) {
        return versusInfoAnalyzer.analyze(result, context);
    }

    public List<HealthArmorInfo> getHealthArmorInfo(MatchResult result, ParseContext context) {
        return healthArmorInfoAnalyzer.analyze(result, context);

    }

    public ManaInfo getManaInfo(MatchResult result, ParseContext context) {
        return manaInfoAnalyzer.analyze(result, context);
    }

    public CardInfo getCardInfo(MatchResult result, ParseContext context) {
        return cardInfoAnalyzer.analyze(result, context);
    }
}
