package com.dynamicobjx.visitorapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by rsbulanon on 3/5/15.
 */
public class EventSchedule implements Parcelable {

    private String objectId;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private String roomNumber;
    private Long julianDay;

    public EventSchedule(String objectId, String title, String description, Date startDate,
                         Date endDate, String roomNumber, Long julianDay) {
        this.objectId = objectId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomNumber = roomNumber;
        this.julianDay = julianDay;
    }

    public EventSchedule(Parcel in) {
        this.objectId = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.startDate = (Date)in.readSerializable();
        this.endDate = (Date)in.readSerializable();
        this.roomNumber = in.readString();
        this.julianDay = in.readLong();
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }



    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        // TODO Auto-generated method stub
        dest.writeString(this.objectId);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeSerializable(this.startDate);
        dest.writeSerializable(this.endDate);
        dest.writeString(this.roomNumber);
        dest.writeLong(this.julianDay);
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Date getStartDate() { return startDate; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }

    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getRoomNumber() { return roomNumber; }

    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber;}

    public Long getJulianDay() { return julianDay; }

    public void setJulianDay(Long julianDay) { this.julianDay = julianDay; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public EventSchedule createFromParcel(Parcel in) {
            return new EventSchedule (in);
        }

        @Override
        public Object[] newArray(int arg0) {
            return null;
        }
    };
}
