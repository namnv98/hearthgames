package com.hearthlogs.server.service;

import com.hearthlogs.server.match.analysis.HealthArmorInfoAnalyzer;
import com.hearthlogs.server.match.analysis.ManaInfoAnalyzer;
import com.hearthlogs.server.match.analysis.VersusInfoAnalyzer;
import com.hearthlogs.server.match.analysis.domain.ManaInfo;
import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.play.MatchResult;
import com.hearthlogs.server.match.analysis.domain.HealthArmorInfo;
import com.hearthlogs.server.match.analysis.domain.VersusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchAnalysisService {

    @Autowired
    private ManaInfoAnalyzer manaInfoAnalyzer;

    @Autowired
    private HealthArmorInfoAnalyzer healthArmorInfoAnalyzer;

    @Autowired
    private VersusInfoAnalyzer versusInfoAnalyzer;

    public VersusInfo getVersusInfo(MatchResult result, ParseContext context) {
        return versusInfoAnalyzer.analyze(result, context);
    }

    public HealthArmorInfo getHealthArmorInfo(MatchResult result, ParseContext context) {
        return healthArmorInfoAnalyzer.analyze(result, context);

    }

    public ManaInfo getManaInfo(MatchResult result, ParseContext context) {
        return manaInfoAnalyzer.analyze(result, context);
    }
}
