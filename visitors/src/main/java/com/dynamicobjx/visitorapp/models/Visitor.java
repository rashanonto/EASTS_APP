package com.dynamicobjx.visitorapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;

@ParseClassName("Visitor")
public class Visitor extends ParseObject {



    public String getFname() {
        return getString("fname");
    }

    public void setFname(String fname) {
        put("fname", fname);
    }

    public String getLname() {
        return getString("lname");
    }

    public void setLname(String lname) {
        put("lname", lname);
    }

    public String getCompany() {
        return getString("company");
    }

    public void setCompany(String company) {
        put("company", company);
    }

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        put("email", email);
    }

    public String getPosition() {
        return getString("position");
    }

    public void setPosition(String position) {
        put("position", position);
    }

    public String getAddress() {
        return getString("address");
    }

    public void setAddress(String address) {
        put("address", address);
    }

    public String getCountry() {
        return getString("country");
    }

    public void setCountry(String country) {
        put("country", country);
    }

    public String getZipCode() {
        return getString("zipCode");
    }

    public void setZipCode(String zipCode) {
        put("zipCode", zipCode);
    }

    public String getMobileNo() {
        return getString("mobileNo");
    }

    public void setMobileNo(String mobileNo) {
        put("mobileNo", mobileNo);
    }

    public String getHomeNo() {
        return getString("homeNo");
    }

    public void setHomeNo(String homeNo) {
        put("homeNo", homeNo);
    }

    public String getOfficeNo() {
        return getString("officeNo");
    }

    public void setOfficeNo(String officeNo) {
        put("officeNo", officeNo);
    }

    public String getFaxNo() {
        return getString("faxNo");
    }

    public void setFaxNo(String faxNo) {
        put("faxNo", faxNo);
    }

    public void setOrg(String Org) {
        put("organization", Org);
    }

    public void setAge(String age) {
        put("age", age);
    }

    public void setHowDid(JSONArray howDid) {
        put("how_did_you_learn_about_the_exhibition", howDid);
    }

    public void setJobFunction(JSONArray jobFunction) {
        put("job_function", jobFunction);
    }

    public void setStatus(String status) {
        put("status", status);
    }

    public void setInterest(JSONArray interest) {
        put("main_product_interest", interest);
    }

    public void setPurpose(JSONArray purpose) {
        put("purpose_of_visit", purpose);
    }

    public void setRole(JSONArray role) {
        put("role_in_purchasing", role);
    }

    public void setBusiness(JSONArray business) {
        put("type_of_business_industry", business);
    }

    public void setContactNo(String contactNo) {
        put("contactNo", contactNo);
    }

    public void setGender(String gender) {
        put("gender", gender);
    }

}
