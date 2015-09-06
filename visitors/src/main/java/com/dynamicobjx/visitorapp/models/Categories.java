package com.dynamicobjx.visitorapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rsbulanon on 2/20/15.
 */
public class Categories implements Parcelable {

    private String objectId;
    private String name;
    private boolean isFavorite;

    public Categories(String objectId, String name, boolean isFavorite) {
        this.objectId = objectId;
        this.name = name;
        this.isFavorite = isFavorite;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public Categories(Parcel in) {
        this.objectId = in.readString();
        this.name = in.readString();
        this.isFavorite = in.readByte() != 0;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        // TODO Auto-generated method stub
        dest.writeString(this.objectId);
        dest.writeString(this.name);
        dest.writeByte((byte)(this.isFavorite ? 1: 0));
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Categories createFromParcel(Parcel in) {
            return new Categories(in);
        }

        @Override
        public Object[] newArray(int arg0) {
            return null;
        }
    };
}
