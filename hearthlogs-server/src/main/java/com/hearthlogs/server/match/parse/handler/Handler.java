package com.hearthlogs.server.match.parse.handler;

import com.hearthlogs.server.match.parse.ParseContext;
import com.hearthlogs.server.match.raw.domain.LogLineData;

public interface Handler {

    boolean supports(ParseContext context, String line);

    boolean handle(ParseContext context, LogLineData line);
}
