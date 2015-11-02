package com.hearthlogs.server.match.raw.filter;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AssetFilter {

    public String filter(String line) {
        String filteredLine = null;
        if (!StringUtils.isEmpty(line) && line.startsWith("[Asset] CachedAsset.UnloadAssetObject() - unloading name=Medal_Ranked")) {
            filteredLine = line.replace("[Asset] CachedAsset.UnloadAssetObject() - ", "");
        }
        return filteredLine;
    }
}
