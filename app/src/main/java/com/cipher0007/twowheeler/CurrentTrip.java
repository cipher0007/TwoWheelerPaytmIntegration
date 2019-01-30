package com.cipher0007.twowheeler;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cipher0007.twowheeler.OtpVerification.OtpVerfication;
import com.cipher0007.twowheeler.OtpVerification.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.cipher0007.twowheeler.Timer_Service.MID;
import static com.cipher0007.twowheeler.Timer_Service.NOTIFICATION_CHANNEL_ID;

public class CurrentTrip extends AppCompatActivity {
    String date_time;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    SharedPreferences mpref;
    SharedPreferences.Editor mEditor;
    private Button btnStartRide, btnEndRide;
    private TextView txtTimer,txtTimer1,txtTimer2,txtdes;
    private String et_hours = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_trip);
        init();
        btnStartRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_hours.toString().trim() != "0") {
                    startRide();
                    Notif(new SharedPrefManager(getApplicationContext()).getFirstName() + ", Happy Journey", "Enjoy your ride has been started");
                }
            }
        });

        btnEndRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Timer_Service.class);
                stopService(intent);

                mEditor.clear().commit();
                mEditor.putBoolean("finish", true).commit();
                et_hours = null;
                // et_hours.setEnabled(true);
                btnEndRide.setEnabled(true);
                txtTimer.setText("00");
                txtTimer1.setText("00");
                txtTimer2.setText("00");
                txtdes.setVisibility(View.GONE);

                Notif(new SharedPrefManager(getApplicationContext()).getFirstName() + ", Hope You Enjoyed!", "We Hope you will come back soon. We have exciting offer for you");
                Intent i = new Intent(CurrentTrip.this, MapActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                // close this activity
                finish();

            }
        });

    }

    private void init() {
        btnStartRide = findViewById(R.id.btnStartRide);
        btnEndRide = findViewById(R.id.btnEndRide);
        txtTimer = findViewById(R.id.txtTimer);
        txtdes = findViewById(R.id.txtdes);
        txtTimer1 = findViewById(R.id.txtTimer1);
        txtTimer2 = findViewById(R.id.txtTimer2);
        Typeface light = Typeface.createFromAsset(getAssets(),
                "Montserrat-Light.otf");
        Typeface semibold = Typeface.createFromAsset(getAssets(),
                "Montserrat-SemiBold.otf");
        Typeface regular = Typeface.createFromAsset(getAssets(),
                "Montserrat-Regular.otf");
        txtTimer.setTypeface(semibold);
        txtTimer1.setTypeface(regular);
        txtTimer2.setTypeface(light);
        txtdes.setTypeface(light);
        txtdes.setVisibility(View.GONE);
        SharedPrefManager get = new SharedPrefManager(getApplicationContext());
        et_hours = get.getRideTime();
        mpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEditor = mpref.edit();
        if (et_hours.toString().trim() != "0") {
            try {
                String str_value = mpref.getString("data", "");
                if (str_value.matches("")) {
                    // et_hours.setEnabled(true);
                    btnStartRide.setEnabled(true);
                    txtTimer.setText("00");
                    txtTimer1.setText("00");
                    txtTimer2.setText("00");
                    txtdes.setVisibility(View.GONE);
                } else {

                    if (mpref.getBoolean("finish", false)) {
                        // et_hours.setEnabled(true);
                        btnStartRide.setEnabled(true);
                        txtTimer.setText("00");
                        txtTimer1.setText("00");
                        txtTimer2.setText("00");
                        txtdes.setVisibility(View.GONE);
                    } else {

                        //  et_hours.setEnabled(false);
                        txtdes.setVisibility(View.VISIBLE);
                        btnStartRide.setEnabled(false);
                        String[] time1 = str_value.split(":");
                        String[] time2 = time1[1].split(" ");
                        String[] time3 = time1[2].split(" ");
                        txtTimer.setText(time1[0].toString());
                        txtTimer1.setText(time2[0].toString());
                        txtTimer2.setText(time3[0].toString());
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    private void startRide() {
        if (et_hours.length() > 0) {

            int int_hours = Integer.valueOf(et_hours.toString());

            if (int_hours <= 24) {


                btnStartRide.setEnabled(false);
                mEditor.putBoolean("finish", false).commit();

                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                date_time = simpleDateFormat.format(calendar.getTime());

                mEditor.putString("data", date_time).commit();
                mEditor.putString("hours", et_hours).commit();


                Intent intent_service = new Intent(getApplicationContext(), Timer_Service.class);
                startService(intent_service);
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Data", Toast.LENGTH_SHORT).show();
            }
/*
                    mTimer = new Timer();
                    mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 5, NOTIFY_INTERVAL);*/
        } else {
            Toast.makeText(getApplicationContext(), "Please select value", Toast.LENGTH_SHORT).show();
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str_time = intent.getStringExtra("time");
            String[] time1 = str_time.split(":");
            String[] time2 = time1[1].split(" ");
            String[] time3 = time1[2].split(" ");
            txtTimer.setText(time1[0].toString());
            txtTimer1.setText(time2[0].toString());
            txtTimer2.setText(time3[0].toString());
            txtdes.setVisibility(View.VISIBLE);


        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(Timer_Service.str_receiver));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void Notif(String titile, String Content) {
        Intent notificationIntent = new Intent(getApplicationContext(), SplashScreen.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                getApplicationContext()).setSmallIcon(R.drawable.ic_menu_camera)
                .setContentTitle(titile)
                .setContentText(Content)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{100, 200, 300, 400});
        // notificationManager.notify(MID, mNotifyBuilder.build());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400});
            assert notificationManager != null;
            mNotifyBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        assert notificationManager != null;
        notificationManager.notify(MID /* Request Code */, mNotifyBuilder.build());


        MID++;
    }
}
