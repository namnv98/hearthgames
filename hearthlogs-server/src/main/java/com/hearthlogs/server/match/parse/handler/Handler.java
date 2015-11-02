package com.hearthlogs.server.match.parse.handler;

import com.hearthlogs.server.match.parse.ParsedMatch;
import com.hearthlogs.server.match.raw.domain.LogLineData;

public interface Handler {

    boolean supports(ParsedMatch parsedMatch, String line);

    boolean handle(ParsedMatch parsedMatch, LogLineData line);
}
