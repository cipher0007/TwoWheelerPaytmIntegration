package com.cipher0007.twowheeler;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.cipher0007.twowheeler.OtpVerification.OtpVerfication;
import com.cipher0007.twowheeler.OtpVerification.SharedPrefManager;
import com.cipher0007.twowheeler.OtpVerification.UserDetail;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    private TextView txtview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        txtview=findViewById(R.id.txtAppName);
        Typeface bold = Typeface.createFromAsset(getAssets(),
                "Montserrat-Light.otf");
        txtview.setTypeface(bold);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(SplashScreen.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity


               SharedPreferences mpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if (!mpref.getBoolean("finish", false)) {
                    Intent i = new Intent(SplashScreen.this, OtpVerfication.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    // close this activity
                    finish();
                }else{
//                    Intent i = new Intent(SplashScreen.this, CurrentTrip.class);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                    // close this activity
//                    finish();
                    Intent i = new Intent(SplashScreen.this, UserDetail.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    // close this activity
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
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
