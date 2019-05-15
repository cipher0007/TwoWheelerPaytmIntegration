package com.cipher0007.twowheeler.Network.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YourBookingItem {

@SerializedName("bno")
@Expose
private String bno;
@SerializedName("pno")
@Expose
private String pno;
@SerializedName("bikeno")
@Expose
private String bikeno;
@SerializedName("noh")
@Expose
private String noh;
@SerializedName("dobook")
@Expose
private String dobook;

public String getBno() {
return bno;
}

public void setBno(String bno) {
this.bno = bno;
}

public String getPno() {
return pno;
}

public void setPno(String pno) {
this.pno = pno;
}

public String getBikeno() {
return bikeno;
}

public void setBikeno(String bikeno) {
this.bikeno = bikeno;
}

public String getNoh() {
return noh;
}

public void setNoh(String noh) {
this.noh = noh;
}

public String getDobook() {
return dobook;
}

public void setDobook(String dobook) {
this.dobook = dobook;
}

}