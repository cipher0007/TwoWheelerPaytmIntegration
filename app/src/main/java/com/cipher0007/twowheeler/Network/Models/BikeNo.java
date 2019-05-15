package com.cipher0007.twowheeler.Network.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BikeNo {

    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("bikename")
    @Expose
    private String bikename;
    @SerializedName("bike_number")
    @Expose
    private String bikeNumber;
    @SerializedName("status")
    @Expose
    private String status;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBikename() {
        return bikename;
    }

    public void setBikename(String bikename) {
        this.bikename = bikename;
    }

    public String getBikeNumber() {
        return bikeNumber;
    }

    public void setBikeNumber(String bikeNumber) {
        this.bikeNumber = bikeNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}