package com.cipher0007.twowheeler;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cipher0007.twowheeler.OtpVerification.SharedPrefManager;
import com.github.jorgecastilloprz.FABProgressCircle;

import org.json.JSONException;
import org.json.JSONObject;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

public class ConfirmBookingFinal extends AppCompatActivity {
    private FloatingActionButton btnbook;
    FABProgressCircle fabProgressCircle;
    private String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking_final);
        btnbook = findViewById(R.id.btnCnfrmbook);
        fabProgressCircle = findViewById(R.id.fabProgressCircle11);
        TextView txtcnrmbook = findViewById(R.id.txtconfrmbook);
        TextView txtcnrmbook1 = findViewById(R.id.txtconfrmbook1);

        Typeface bold = Typeface.createFromAsset(getAssets(),
                "Montserrat-Light.otf");
        txtcnrmbook.setTypeface(bold);
        txtcnrmbook1.setTypeface(bold);

        Bundle extras = getIntent().getExtras();
        price = new SharedPrefManager(getApplicationContext()).getSelectedPrice().substring(2);
        txtcnrmbook1.setText("Confirm your payment of ₹ " + price + " and enjoy the ride in the lap of nature");
//        if (extras != null) {
//            price = extras.getString("price");
//
//            // and get whatever type user account id is
//        }
        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabProgressCircle.show();
                fabProgressCircle.beginFinalAnimation();
                SharedPrefManager sharedPrefManager = new SharedPrefManager(ConfirmBookingFinal.this);
                //Toast.makeText(ConfirmBookingFinal.this, sharedPrefManager.getEmail()+"\n"+sharedPrefManager.getPhoneNumber(), Toast.LENGTH_SHORT).show();
                callInstamojoPay(sharedPrefManager.getEmail(), sharedPrefManager.getPhoneNumber(), "11", "Rent a Scooty from Easy Scooter for " + sharedPrefManager.getSelectedhour(),
                        sharedPrefManager.getFirstName() + " " + sharedPrefManager.getLastName());
            }
        });
    }

    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = this;
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
            pay.put("send_sms", true);
            pay.put("send_email", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;


    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                // status=success:orderId=a089f02724ed4a8db6c069f6d30b3245:txnId=None:paymentId=MOJO7918005A76494611:token=qyFwLidQ0aBNNWlsmwHx1gHFhlt6A1
                String rearray[] = response.split(":");
                String orderid = rearray[1].substring(rearray[1].indexOf("=") + 1);
                //Toast.makeText(getApplicationContext(), orderid, Toast.LENGTH_LONG).show();


                String paymentId = rearray[3].substring(rearray[3].indexOf("=") + 1);
                String token = rearray[4].substring(rearray[4].indexOf("=") + 1);


                Log.d("BKC", "Orderid " + orderid + "\npaymentid " + paymentId + "\ntoken " + token);
                Log.d("BKC", response);

                Toast.makeText(ConfirmBookingFinal.this, "Orderid " + orderid + "\npaymentid " + paymentId + "\ntoken " + token, Toast.LENGTH_SHORT).show();


                fabProgressCircle.beginFinalAnimation();
                fabProgressCircle.onCompleteFABAnimationEnd();
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
                        .show();
                Intent i = new Intent(ConfirmBookingFinal.this, BookedSuccess.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                // close this activity
                finish();
            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();
            }
        };
    }
}
