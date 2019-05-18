package com.cipher0007.twowheeler.Network.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetProfilePhotoItem {

@SerializedName("profimage")
@Expose
private String profimage;

public String getProfimage() {
return profimage;
}

public void setProfimage(String profimage) {
this.profimage = profimage;
}

}