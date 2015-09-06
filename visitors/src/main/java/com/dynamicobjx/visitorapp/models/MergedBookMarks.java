package com.dynamicobjx.visitorapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rsbulanon on 2/20/15.
 */
public class MergedBookMarks implements Parcelable {

    private String objectId;
    private String name;
    private String type;
    private Exhibitor exhibitor;
    private Categories category;

    public MergedBookMarks(String objectId, String name, String type, Exhibitor exhibitor, Categories category) {
        this.objectId = objectId;
        this.name = name;
        this.type = type;
        this.exhibitor = exhibitor;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Exhibitor getExhibitor() {
        return exhibitor;
    }

    public void setExhibitor(Exhibitor exhibitor) {
        this.exhibitor = exhibitor;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public MergedBookMarks(Parcel in) {
        this.objectId = in.readString();
        this.name = in.readString();
        this.type = in.readString();
        this.exhibitor = in.readParcelable(Exhibitor.class.getClassLoader());
        this.category = in.readParcelable(Categories.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        // TODO Auto-generated method stub
        dest.writeString(this.objectId);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeParcelable(this.exhibitor,flag);
        dest.writeParcelable(this.category,flag);
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MergedBookMarks createFromParcel(Parcel in) {
            return new MergedBookMarks(in);
        }

        @Override
        public Object[] newArray(int arg0) {
            return null;
        }
    };
}
