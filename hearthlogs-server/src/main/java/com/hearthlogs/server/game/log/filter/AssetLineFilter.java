package com.hearthlogs.server.game.log.filter;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AssetLineFilter {

    public String filter(String line) {
        String filteredLine = null;
        if (!StringUtils.isEmpty(line) && line.startsWith("[Asset] CachedAsset.UnloadAssetObject() - unloading name=Medal_Ranked")) {
            filteredLine = line.replace("[Asset] CachedAsset.UnloadAssetObject() - ", "");
        }
        return filteredLine;
    }
}
