package com.hearthlogs.server.analytics.dispatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public abstract class AnalyticsDispatcher {
	private static final Logger log = LoggerFactory
			.getLogger(AnalyticsDispatcher.class);

	protected String userAgent;
	protected String host;
	protected int port;

	public AnalyticsDispatcher(String userAgent, String host, int port) {
		this.userAgent = userAgent;
		this.host = host;
		this.port = port;
	}

	public void dispatch(String analyticsString) {
		URI uri = URI.create(analyticsString);

		String timeDispatched = getQueryParameter(uri.getQuery(), "utmht");
		if (timeDispatched != null) {
			try {
				Long time = Long.parseLong(timeDispatched);
				analyticsString = analyticsString + "&utmqt="
						+ (System.currentTimeMillis() - time);
			} catch (NumberFormatException e) {
				log.error("Error parsing utmht parameter: " + e.toString());
			}
		} else {
			log.warn("Unable to find utmht parameter: " + analyticsString);
		}

		dispatchToNetwork(analyticsString);
	}

	protected abstract void dispatchToNetwork(String analyticsString);

	protected static String getQueryParameter(String query, String parameter) {
		String[] params = query.split("&");
		for (String param : params) {
			String[] nameValue = param.split("=");
			if (nameValue[0].equals(parameter)) {
				return nameValue[1];
			}
		}
		return null;
	}
}
