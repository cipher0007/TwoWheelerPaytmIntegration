package com.cipher0007.twowheeler.Network.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AmountItem {

@SerializedName("price")
@Expose
private String price;
@SerializedName("discount")
@Expose
private String discount;
@SerializedName("topay")
@Expose
private Integer topay;

public String getPrice() {
return price;
}

public void setPrice(String price) {
this.price = price;
}

public String getDiscount() {
return discount;
}

public void setDiscount(String discount) {
this.discount = discount;
}

public Integer getTopay() {
return topay;
}

public void setTopay(Integer topay) {
this.topay = topay;
}

}