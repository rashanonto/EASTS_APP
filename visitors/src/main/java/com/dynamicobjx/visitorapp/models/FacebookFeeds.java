package com.dynamicobjx.visitorapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by rsbulanon on 2/5/15.
 */
public class FacebookFeeds implements Parcelable {

    private String message;
    private String from;
    private String datePosted;
    private String url;

    public FacebookFeeds(String message, String from) {
        this.message = message;
        this.from = from;
    }

    public FacebookFeeds(String message, String from, String datePosted, String url) {
        this.message = message;
        this.from = from;
        this.datePosted = datePosted;
        this.url = url;
    }

    public FacebookFeeds(Parcel in) {
        this.message = in.readString();
        this.from = in.readString();
        this.datePosted = in.readString();
        this.url = in.readString();
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1) {
        // TODO Auto-generated method stub
        dest.writeString(this.message);
        dest.writeString(this.from);
        dest.writeSerializable(this.datePosted);
        dest.writeString(this.url);
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public FacebookFeeds createFromParcel(Parcel in) {
            return new FacebookFeeds (in);
        }

        @Override
        public Object[] newArray(int arg0) {
            return null;
        }
    };

}
