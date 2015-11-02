package com.hearthlogs.server.match.play.handler;

import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.parse.domain.Activity;
import com.hearthlogs.server.match.parse.domain.Card;
import com.hearthlogs.server.match.parse.domain.Game;
import com.hearthlogs.server.match.parse.domain.Player;
import com.hearthlogs.server.match.play.MatchResult;

public interface Handler {
    
    boolean supports(MatchResult result, ParsedMatch parsedMatch, Activity activity);

    boolean handle(MatchResult result, ParsedMatch parsedMatch, Activity activity);

    void handleAction(MatchResult result, ParsedMatch parsedMatch, Activity activity, Player player, Card source, Card target);

    void handleNewGame(MatchResult result, ParsedMatch parsedMatch, Activity activity);

    void handleNewCard(MatchResult result, ParsedMatch parsedMatch, Activity activity);

    void handleCardTagChange(MatchResult result, ParsedMatch parsedMatch, Activity activity, Player player, Card before, Card after);

    void handlePlayerTagChange(MatchResult result, ParsedMatch parsedMatch, Activity activity, Player before, Player after);

    void handleGameTagChange(MatchResult result, ParsedMatch parsedMatch, Activity activity, Game before, Game after);

    void handleShowEntity(MatchResult result, ParsedMatch parsedMatch, Activity activity, Player player, Card before, Card after);

}
