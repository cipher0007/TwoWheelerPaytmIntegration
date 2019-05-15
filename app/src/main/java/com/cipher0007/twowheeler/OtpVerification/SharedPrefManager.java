package com.cipher0007.twowheeler.OtpVerification;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    Context context;

    public SharedPrefManager(Context context) {
        this.context = context;

    }

    public void Ridetime(String time) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RideTime", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("time", time);
        editor.commit();
    }
public void setSelectedRide(String hour,String price){
    SharedPreferences sharedPreferences = context.getSharedPreferences("RideSelected", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("hour", hour);
    editor.putString("price", price);
    editor.commit();

}
    public String getSelectedhour() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RideSelected", Context.MODE_PRIVATE);
        return sharedPreferences.getString("hour", "");
    }
    public String getSelectedPrice() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RideSelected", Context.MODE_PRIVATE);
        return sharedPreferences.getString("price", "");
    }
    public String getRideTime() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RideTime", Context.MODE_PRIVATE);
        return sharedPreferences.getString("time", "");
    }

    public void saveLoginDetails(String firstName, String lastName, String email, Boolean isLoggedin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putString("email", email);
        editor.putBoolean("islogin", isLoggedin);
        editor.commit();
    }

    public void savePhoneNumber(String phn, Boolean isLogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phn", phn);
        editor.putBoolean("numberchck", isLogin);
        editor.commit();
    }

    public boolean OtpVerified() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("numberchck", false);
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("islogin", false);
    }

    public String getPhoneNumber() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("phn", "");
    }

    public String getEmail() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("email", "");
    }

    public String getFirstName() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("firstName", "");


    }

    public String getLastName() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("lastName", "");
    }


}
