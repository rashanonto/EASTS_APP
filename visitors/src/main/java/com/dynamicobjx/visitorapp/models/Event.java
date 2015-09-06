package com.dynamicobjx.visitorapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by rsbulanon on 2/3/15.
 */
public class Event implements Parcelable {

    private String objectId;
    private String title;
    private Date eventDate;
    private String venue;
    private String description;
    private String officialHashTag;
    private String officialFBPage;
    private String aboutUs;

    public Event(String objectId, String title, Date eventDate, String venue,String description,
                 String officialHashTag, String officialFBPage, String aboutUs) {
        this.objectId = objectId;
        this.title = title;
        this.eventDate = eventDate;
        this.venue = venue;
        this.description = description;
        this.officialHashTag = officialHashTag;
        this.officialFBPage = officialFBPage;
        this.aboutUs = aboutUs;
    }

    public Event(Parcel in) {
        this.objectId = in.readString();
        this.title = in.readString();
        this.eventDate = (Date)in.readSerializable();
        this.venue = in.readString();
        this.description = in.readString();
        this.officialHashTag = in.readString();
        this.officialFBPage = in.readString();
        this.aboutUs = in.readString();
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1) {
        // TODO Auto-generated method stub
        dest.writeString(this.objectId);
        dest.writeString(this.title);
        dest.writeSerializable(this.eventDate);
        dest.writeString(this.venue);
        dest.writeString(this.description);
        dest.writeString(this.officialHashTag);
        dest.writeString(this.officialFBPage);
        dest.writeString(this.aboutUs);
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Event createFromParcel(Parcel in) {
            return new Event (in);
        }

        @Override
        public Object[] newArray(int arg0) {
            return null;
        }
    };

    public Date getEventDate() { return eventDate; }

    public void setEventDate(Date eventDate) { this.eventDate = eventDate; }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOfficialHashTag() { return officialHashTag; }

    public void setOfficialHashTag(String officialHashTag) { this.officialHashTag = officialHashTag;}

    public String getOfficialFBPage() { return officialFBPage; }

    public void setOfficialFBPage(String officialFBPage) { this.officialFBPage = officialFBPage; }

    public String getAboutUs() { return aboutUs; }

    public void setAboutUs(String aboutUs) { this.aboutUs = aboutUs; }
}
