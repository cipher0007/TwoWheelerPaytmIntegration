package com.cipher0007.twowheeler;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cipher0007.twowheeler.OtpVerification.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Timer_Service extends Service {

    public static String str_receiver = "com.cipher0007.twowheeler.receiver";
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public static int MID = 3421;
    private Handler mHandler = new Handler();
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String strDate;
    Date date_current, date_diff;
    SharedPreferences mpref;
    SharedPreferences.Editor mEditor;

    private Timer mTimer = null;
    public static final long NOTIFY_INTERVAL = 1000;
    Intent intent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEditor = mpref.edit();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 5, NOTIFY_INTERVAL);
        intent = new Intent(str_receiver);

    }


    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    calendar = Calendar.getInstance();
                    simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    strDate = simpleDateFormat.format(calendar.getTime());
                    Log.e("strDate", strDate);
                    twoDatesBetweenTime();

                }

            });
        }

    }

    public String twoDatesBetweenTime() {


        try {
            date_current = simpleDateFormat.parse(strDate);
        } catch (Exception e) {

        }

        try {
            date_diff = simpleDateFormat.parse(mpref.getString("data", ""));
        } catch (Exception e) {

        }

        try {


            long diff = date_current.getTime() - date_diff.getTime();
            int int_hours = Integer.valueOf(mpref.getString("hours", ""));

            long int_timer = TimeUnit.HOURS.toMillis(int_hours);
            long long_hours = int_timer - diff;
            long diffSeconds2 = long_hours / 1000 % 60;
            long diffMinutes2 = long_hours / (60 * 1000) % 60;
            long diffHours2 = long_hours / (60 * 60 * 1000) % 24;


            if (long_hours > 0) {
                String str_testing = diffHours2 + ":" + diffMinutes2 + ":" + diffSeconds2;

                Log.e("TIME", str_testing);
                mEditor.putBoolean("finish", false).commit();
                fn_update(str_testing);
            } else {
                Notif(new SharedPrefManager(getApplicationContext()).getFirstName()+", You are out of Time!","Submit the Scooty at nearest station");
                mEditor.putBoolean("finish", true).commit();
                mTimer.cancel();
            }
        }catch (Exception e){
            mTimer.cancel();
            mTimer.purge();


        }

        return "";

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEditor.putBoolean("finish", false).commit();
        Log.e("Service finish","Finish");
    }

    private void fn_update(String str_time){

        intent.putExtra("time",str_time);
        sendBroadcast(intent);
    }
    private void Notif(String titile, String Content){
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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
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
