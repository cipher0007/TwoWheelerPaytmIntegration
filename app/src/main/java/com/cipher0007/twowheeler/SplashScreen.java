package com.cipher0007.twowheeler;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.cipher0007.twowheeler.Network.ApiClient;
import com.cipher0007.twowheeler.Network.ApiServices;
import com.cipher0007.twowheeler.Network.Models.FirstCheck;
import com.cipher0007.twowheeler.Network.Models.GetProfilePhotoItem;
import com.cipher0007.twowheeler.Network.Models.MyNotifIcation;
import com.cipher0007.twowheeler.OtpVerification.OtpVerfication;
import com.cipher0007.twowheeler.OtpVerification.SharedPrefManager;
import com.cipher0007.twowheeler.OtpVerification.UserDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {
    private TextView txtview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

//        txtview = findViewById(R.id.txtAppName);
//        Typeface bold = Typeface.createFromAsset(getAssets(),
//                "Montserrat-Light.otf");
//        txtview.setTypeface(bold);
        Thread t = new Thread() {
            @Override
            public void run() {
                FirstCheckMyData();

            }
        };

        t.start();


        //  new Handler().postDelayed(new Runnable() {


//            @Override
//            public void run() {
//                // This method will be executed once the timer is over
//                // Start your app main activity
//
//
//               SharedPreferences mpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
////                if (mpref.getBoolean("finish", false)) {
//                    Intent i = new Intent(SplashScreen.this, OtpVerfication.class);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.fade_in, R.anim.swipe_up);
//                    // close this activity
//                    finish();
////                }else{
////                    Intent i = new Intent(SplashScreen.this, CurrentTrip.class);
////                    startActivity(i);
////                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
////                    // close this activity
////                    finish();
//////                    Intent i = new Intent(SplashScreen.this, UserDetail.class);
//////                    startActivity(i);
//////                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//////                    // close this activity
//////                    finish();
////                }
//            }
//        }, SPLASH_TIME_OUT);
    }


    private void FirstCheckMyData() {
        ApiServices apiService = ApiClient.getClient(getApplicationContext()).create(ApiServices.class);

        Call<FirstCheck> call = apiService.FirstCheckData(new SharedPrefManager(getApplicationContext()).getPhoneNumber());
        call.enqueue(new Callback<FirstCheck>() {
            @Override
            public void onResponse(Call<FirstCheck> call, Response<FirstCheck> response) {
                FirstCheck book = response.body();
                String ch = book.getError().trim().toString();
                Toast.makeText(SplashScreen.this, ch, Toast.LENGTH_SHORT).show();
                switch (ch) {
                    case "notregis": {
                        new SharedPrefManager(getApplicationContext()).removeAll();
                        Toast.makeText(SplashScreen.this, book.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SplashScreen.this, OtpVerfication.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_in, R.anim.swipe_up);
                        // close this activity
                        finish();
                        break;
                    }

                    case "false": {
                        Toast.makeText(SplashScreen.this, book.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SplashScreen.this, BookingCurrentTrip.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_in, R.anim.swipe_up);
                        // close this activity
                        finish();
                        break;
                    }
                    case "true": {
                        Toast.makeText(SplashScreen.this, book.getMessage(), Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(SplashScreen.this, MapActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_in, R.anim.swipe_up);
                        // close this activity
                        finish();
                        Toast.makeText(SplashScreen.this, book.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                    }

                    //Toast.makeText(getApplicationContext(), book.getProfimage().toString(), Toast.LENGTH_LONG).show();
//                viewDialog.hideDialog();
                    // Picasso.get().load(book.getProfimage()).into(headerProfileImage);
                    //viewDialog.hideDialog();

                }
            }

            @Override
            public void onFailure(Call<FirstCheck> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_LONG).show();

                CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator);
                Snackbar.make(coordinatorLayout, "Oops! No internet connection", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                retry();
                            }
                        }).setActionTextColor(Color.RED).show();


            }

            public void retry() {
                call.clone().enqueue(this);

            }


        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(SplashScreen.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
