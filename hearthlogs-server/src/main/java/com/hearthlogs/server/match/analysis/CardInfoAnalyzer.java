package com.hearthlogs.server.match.analysis;

import com.hearthlogs.server.match.analysis.domain.CardInfo;
import com.hearthlogs.server.match.analysis.domain.generic.GenericColumn;
import com.hearthlogs.server.match.analysis.domain.generic.GenericRow;
import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.play.MatchResult;

public class CardInfoAnalyzer implements Analyzer<CardInfo> {

    @Override
    public CardInfo analyze(MatchResult result, ParseContext context) {

        CardInfo info = new CardInfo();

        GenericRow header = new GenericRow();
        header.addColumn(new GenericColumn("Player"));


        return info;
    }
}
