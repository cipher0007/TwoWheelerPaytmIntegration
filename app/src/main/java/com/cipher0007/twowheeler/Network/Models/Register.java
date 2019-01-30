package com.cipher0007.twowheeler.Network.Models;

import com.google.gson.annotations.SerializedName;

public class Register {

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }


}
