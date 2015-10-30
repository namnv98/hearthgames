package com.hearthlogs.server.service.log.handler;

import com.hearthlogs.server.match.MatchContext;

public interface Handler {

    boolean applies(MatchContext context, String line);

    boolean handle(MatchContext context, String line);
}
