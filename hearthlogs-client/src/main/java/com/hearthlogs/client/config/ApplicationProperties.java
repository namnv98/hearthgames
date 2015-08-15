package com.hearthlogs.client.config;

import java.io.File;

public class ApplicationProperties {

    private String uploadUrl;
    private File logFile;
    private File logConfigFile;

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
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
