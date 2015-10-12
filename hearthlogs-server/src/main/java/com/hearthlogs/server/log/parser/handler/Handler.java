package com.hearthlogs.server.log.parser.handler;

import com.hearthlogs.server.match.MatchContext;

public interface Handler {

    boolean applies(MatchContext context, String line);

    boolean handle(MatchContext context, String line);
}
