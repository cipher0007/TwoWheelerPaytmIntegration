package com.cipher0007.twowheeler.Network.Models;

public class MyNotifIcation {
    String title;
    String intent_Code;

    public MyNotifIcation(String title, String intent_Code) {
        this.title = title;
        this.intent_Code = intent_Code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntent_Code() {
        return intent_Code;
    }

    public void setIntent_Code(String intent_Code) {
        this.intent_Code = intent_Code;
    }
}