package com.dynamicobjx.visitorapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by graciosa on 2/6/15.
 */
public class News implements Parcelable {

    private String headline;
    private String body;
    private Date publishDate;

    public News(String headline, String body, Date publishDate) {
        this.headline = headline;
        this.body = body;
        this.publishDate = publishDate;
    }

    public News(Parcel in) {
        this.headline = in.readString();
        this.body = in.readString();
        this.publishDate = (Date)in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1) {
        // TODO Auto-generated method stub
        dest.writeString(this.headline);
        dest.writeString(this.body);
        dest.writeSerializable(this.publishDate);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public News createFromParcel(Parcel in) {
            return new News (in);
        }

        @Override
        public Object[] newArray(int arg0) {
            return null;
        }
    };

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }
}
