package com.dynamicobjx.visitorapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by graciosa on 2/4/15.
 */
public class Exhibitor implements Parcelable {

    private String objectId;
    private String companyName;
    private String decsription;
    private String contactNo;
    private String fax;
    private String email;
    private String website;
    private boolean isFavorite;
    private ArrayList<String> categories;
    private ArrayList<String> categoriesId;
    private String boothNo;
    private String packageType;
    private String contactPerson;
    private String scheduleEvent;
    private String address;
    private String logoUrl;

    public Exhibitor(String objectId, String companyName, String description, String contactNo,String fax,
                     String email, String website, boolean isFavorite, ArrayList<String> categories,
                     ArrayList<String> categoriesId, String boothNo, String packageType, String contactPerson,
                     String scheduleEvent, String address, String logoUrl) {
        this.objectId = objectId;
        this.companyName = companyName;
        this.decsription = description;
        this.contactNo = contactNo;
        this.fax = fax;
        this.email = email;
        this.website = website;
        this.isFavorite = isFavorite;
        this.categories = categories;
        this.categoriesId = categoriesId;
        this.boothNo = boothNo;
        this.packageType = packageType;
        this.contactPerson = contactPerson;
        this.scheduleEvent = scheduleEvent;
        this.address = address;
        this.logoUrl = logoUrl;
    }

    public Exhibitor(Parcel in) {
        this.objectId = in.readString();
        this.companyName = in.readString();
        this.decsription = in.readString();
        this.contactNo = in.readString();
        this.fax = in.readString();
        this.email = in.readString();
        this.website = in.readString();
        this.isFavorite = in.readByte() != 0;
        this.categories = in.readArrayList(String.class.getClassLoader());
        this.categoriesId = in.readArrayList(String.class.getClassLoader());
        this.boothNo = in.readString();
        this.packageType = in.readString();
        this.contactPerson = in.readString();
        this.scheduleEvent = in.readString();
        this.address = in.readString();
        this.logoUrl = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1) {
        // TODO Auto-generated method stub
        dest.writeString(this.objectId);
        dest.writeString(this.companyName);
        dest.writeString(this.decsription);
        dest.writeString(this.contactNo);
        dest.writeString(this.fax);
        dest.writeString(this.email);
        dest.writeString(this.website);
        dest.writeByte((byte) (this.isFavorite ? 1 : 0));
        dest.writeList(this.categories);
        dest.writeList(this.categoriesId);
        dest.writeString(this.boothNo);
        dest.writeString(this.packageType);
        dest.writeString(this.contactPerson);
        dest.writeString(this.scheduleEvent);
        dest.writeString(this.address);
        dest.writeString(this.logoUrl);

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Exhibitor createFromParcel(Parcel in) {
            return new Exhibitor (in);
        }

        @Override
        public Object[] newArray(int arg0) {
            return null;
        }
    };




    public String getObjectId() {
        return objectId;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setObjectId(String objectId) { this.objectId = objectId; }

    public String getCompanyName() { return companyName; }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDecsription() {
        return decsription;
    }

    public void setDecsription(String decsription) {
        this.decsription = decsription;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getFax() { return fax; }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email = email; }

    public String getWebsite() { return website; }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isFavorite() { return isFavorite; }

    public void setFavorite(boolean isFavorite) { this.isFavorite = isFavorite; }

    public ArrayList<String> getCategories() { return categories; }

    public void setCategories(ArrayList<String> categories) { this.categories = categories; }

    public ArrayList<String> getCategoriesId() { return categoriesId; }

    public void setCategoriesId(ArrayList<String> categoriesId) { this.categoriesId = categoriesId; }

    public String getBoothNo() { return boothNo; }

    public void setBoothNo(String boothNo) { this.boothNo = boothNo; }

    public String getPackageType() { return packageType; }

    public void setPackageType(String packageType) { this.packageType = packageType; }

    public String getContactPerson() { return contactPerson;}

    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson;}

    public String getScheduleEvent() { return scheduleEvent; }

    public void setScheduleEvent(String scheduleEvent) { this.scheduleEvent = scheduleEvent; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }
}
