package com.hearthlogs.server.match.analysis;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.play.MatchResult;

public interface Analyzer<T> {

    T analyze(MatchResult result, ParseContext context);

}
