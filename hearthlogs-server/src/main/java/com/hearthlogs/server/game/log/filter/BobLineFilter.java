package com.hearthlogs.server.game.log.filter;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class BobLineFilter {

    public String filter(String line) {
        String filteredLine = null;
        if (!StringUtils.isEmpty(line) && line.startsWith("[Bob] ---RegisterFriendChallenge---")) {
            filteredLine = line.replace("[Bob] ", "");
        }
        return filteredLine;
    }
}
