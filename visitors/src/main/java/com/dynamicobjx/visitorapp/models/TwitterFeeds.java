package com.dynamicobjx.visitorapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by rsbulanon on 2/6/15.
 */
public class TwitterFeeds implements Parcelable {

    private String twitterName;
    private String twitterBody;
    private Date twitterTime;
    private String url;

    public TwitterFeeds(String twitterName, String twitterBody, Date twitterTime,String url) {
        this.twitterName = twitterName;
        this.twitterBody = twitterBody;
        this.twitterTime = twitterTime;
        this.url = url;
    }

    public TwitterFeeds(Parcel in) {
        this.twitterName = in.readString();
        this.twitterBody = in.readString();
        this.twitterTime = (Date)in.readSerializable();
        this.url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1) {
        // TODO Auto-generated method stub
        dest.writeString(this.twitterName);
        dest.writeString(this.twitterBody);
        dest.writeSerializable(this.twitterTime);
        dest.writeString(this.url);
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TwitterFeeds createFromParcel(Parcel in) {
            return new TwitterFeeds (in);
        }

        @Override
        public Object[] newArray(int arg0) {
            return null;
        }
    };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getTwitterName() {
        return twitterName;
    }

    public void setTwitterName(String twitterName) {
        this.twitterName = twitterName;
    }

    public String getTwitterBody() {
        return twitterBody;
    }

    public void setTwitterBody(String twitterBody) {
        this.twitterBody = twitterBody;
    }

    public Date getTwitterTime() {
        return twitterTime;
    }

    public void setTwitterTime(Date twitterTime) {
        this.twitterTime = twitterTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
