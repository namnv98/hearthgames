package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.play.MatchResult;

public interface Handler {

    String TRUE_OR_ONE = "1";
    String FALSE_OR_ZERO = "0";

    boolean supports(MatchResult result, ParseContext context, Activity activity);

    boolean handle(MatchResult result, ParseContext context, Activity activity);

}
