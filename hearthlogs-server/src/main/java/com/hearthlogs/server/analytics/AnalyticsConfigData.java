/**
 * Copyright (c) 2010 Daniel Murphy
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
/**
 * Created at Jul 22, 2010, 11:37:36 PM
 */
package com.hearthlogs.server.analytics;


public class AnalyticsConfigData {

    private final String trackingCode;
    private String encoding = "UTF-8";
    private String screenResolution = null;
    private String colorDepth = null;
    private String userLanguage = null;
    private String flashVersion = null;
    private String userAgent = null;
	private VisitorData visitorData;
	private boolean enabled;

    public AnalyticsConfigData(String argTrackingCode) {
        this(argTrackingCode, VisitorData.newVisitor(), true);
    }

    public AnalyticsConfigData(String argTrackingCode, boolean enabled) {
        this(argTrackingCode, VisitorData.newVisitor(), enabled);
    }

    public AnalyticsConfigData(String argTrackingCode, VisitorData visitorData, boolean enabled) {
        if (argTrackingCode == null) {
            throw new RuntimeException("Tracking code cannot be null");
        }
        this.trackingCode = argTrackingCode;
        this.visitorData = visitorData;
        this.enabled = enabled;
    }

    public String getColorDepth() {
        return colorDepth;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getFlashVersion() {
        return flashVersion;
    }

    public String getScreenResolution() {
        return screenResolution;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public String getUserLanguage() {
        return userLanguage;
    }
    
    public String getUserAgent() {
		return userAgent;
	}
    
    public VisitorData getVisitorData() {
		return visitorData;
	}

    public void setColorDepth(String argColorDepth) {
        colorDepth = argColorDepth;
    }

    public void setEncoding(String argEncoding) {
        encoding = argEncoding;
    }

    public void setFlashVersion(String argFlashVersion) {
        flashVersion = argFlashVersion;
    }

    public void setScreenResolution(String argScreenResolution) {
        screenResolution = argScreenResolution;
    }

    public void setUserLanguage(String argUserLanguage) {
        userLanguage = argUserLanguage;
    }
    
    public void setUserAgent(String userAgent){
    	this.userAgent = userAgent;
    }

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
}