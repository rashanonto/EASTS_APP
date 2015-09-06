package com.dynamicobjx.visitorapp.models;

/**
 * Created by rsbulanon on 2/16/15.
 */
public class Favorites {

    private String objectId;
    private String exhibitorId;
    private String exhitor;

    public Favorites(String objectId, String exhibitorId, String exhitor) {
        this.objectId = objectId;
        this.exhibitorId = exhibitorId;
        this.exhitor = exhitor;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getExhibitorId() {
        return exhibitorId;
    }

    public void setExhibitorId(String exhibitorId) {
        this.exhibitorId = exhibitorId;
    }

    public String getExhitor() {
        return exhitor;
    }

    public void setExhitor(String exhitor) {
        this.exhitor = exhitor;
    }
}
