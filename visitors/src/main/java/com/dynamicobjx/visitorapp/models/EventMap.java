package com.dynamicobjx.visitorapp.models;

import com.parse.ParseFile;

/**
 * Created by rsbulanon on 3/5/15.
 */
public class EventMap {

    private ParseFile map;
    private String fileName;

    public EventMap(ParseFile map, String fileName) {
        this.map = map;
        this.fileName = fileName;
    }

    public ParseFile getMap() { return map; }

    public void setMap(ParseFile map) { this.map = map; }

    public String getFileName() { return fileName; }

    public void setFileName(String fileName) { this.fileName = fileName; }
}
