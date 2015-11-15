package com.hearthlogs.server.config.analytics;

import com.hearthlogs.server.analytics.AnalyticsConfigData;
import com.hearthlogs.server.analytics.JGoogleAnalyticsTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnalyticsConfiguration {

    @Autowired
    private AnalyticsProperties analyticsProperties;

    @Bean
    public JGoogleAnalyticsTracker jGoogleAnalyticsTracker() {
        return new JGoogleAnalyticsTracker(new AnalyticsConfigData(analyticsProperties.getTrackingCode()));
    }
}
