package com.cipher0007.twowheeler.Network.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rate {

@SerializedName("noh")
@Expose
private String noh;
@SerializedName("price")
@Expose
private String price;

public String getNoh() {
return noh;
}

public void setNoh(String noh) {
this.noh = noh;
}

public String getPrice() {
return price;
}

public void setPrice(String price) {
this.price = price;
}

}