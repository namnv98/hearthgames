package com.hearthlogs.server.match.log.domain;

public class FilteredLineData {

    private String line;
    private boolean loggable;

    public FilteredLineData(String line, boolean loggable) {
        this.line = line;
        this.loggable = loggable;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public boolean isLoggable() {
        return loggable;
    }

    public void setLoggable(boolean loggable) {
        this.loggable = loggable;
    }
}
