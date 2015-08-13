package com.hearthlogs.client.config;

import java.io.File;

public class ApplicationProperties {

    private String hptWebServiceURL;
    private File logFile;
    private File logConfigFile;

    public String getHptWebServiceURL() {
        return hptWebServiceURL;
    }

    public void setHptWebServiceURL(String hptWebServiceURL) {
        this.hptWebServiceURL = hptWebServiceURL;
    }

    public File getLogFile() {
        return logFile;
    }

    public void setLogFile(File logFile) {
        this.logFile = logFile;
    }

    public File getLogConfigFile() {
        return logConfigFile;
    }

    public void setLogConfigFile(File logConfigFile) {
        this.logConfigFile = logConfigFile;
    }
}
