package com.cipher0007.twowheeler.Network.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationItem {

@SerializedName("locationame")
@Expose
private String locationame;
@SerializedName("longitude")
@Expose
private String longitude;
@SerializedName("latiitude")
@Expose
private String latitude;

public String getLocationame() {
return locationame;
}

public void setLocationame(String locationame) {
this.locationame = locationame;
}

public String getLongitude() {
return longitude;
}

public void setLongitude(String longitude) {
this.longitude = longitude;
}

public String getLatitude() {
return latitude;
}

public void setLatitude(String latitude) {
this.latitude = latitude;
}


    @Override
    public String toString() {
        return locationame;
    }
}