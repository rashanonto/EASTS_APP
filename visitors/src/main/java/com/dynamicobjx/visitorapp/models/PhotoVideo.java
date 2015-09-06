package com.dynamicobjx.visitorapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;

/**
 * Created by graciosa on 2/11/15.
 */
public class PhotoVideo implements Serializable {

    private String fileName;
    private String mediaType;
    private ParseFile asset;
    private String objectId;

    public PhotoVideo(String objectId, String fileName, String mediaType, ParseFile asset) {
        this.fileName = fileName;
        this.mediaType = mediaType;
        this.asset = asset;
        this.objectId = objectId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public ParseFile getAsset() {
        return asset;
    }

    public void setAsset(ParseFile asset) {
        this.asset = asset;
    }

}
