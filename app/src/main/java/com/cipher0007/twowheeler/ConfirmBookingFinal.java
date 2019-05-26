package com.cipher0007.twowheeler;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cipher0007.twowheeler.Network.Adapter.RateAdapter;
import com.cipher0007.twowheeler.Network.Adapter.YourBookingAdapter;
import com.cipher0007.twowheeler.Network.ApiClient;
import com.cipher0007.twowheeler.Network.ApiServices;
import com.cipher0007.twowheeler.Network.Models.AmountItem;
import com.cipher0007.twowheeler.Network.Models.BookSuccess;
import com.cipher0007.twowheeler.Network.Models.Rate;
import com.cipher0007.twowheeler.Network.Models.YourBookingItem;
import com.cipher0007.twowheeler.OtpVerification.SharedPrefManager;
import com.github.jorgecastilloprz.FABProgressCircle;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmBookingFinal extends AppCompatActivity {
    private FloatingActionButton btnbook;
    FABProgressCircle fabProgressCircle;
    private String price;
    ViewDialog viewDialog;
    private TextView txtBikeName, txtBikeNumber, txtbikeNoOfHour, txtTotalAmount, txtTotalDiscount, txtToPay,txtcnrmbook1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_finl);
        viewDialog = new ViewDialog(ConfirmBookingFinal.this);
        btnbook = findViewById(R.id.btnCnfrmbook);
        fabProgressCircle = findViewById(R.id.fabProgressCircle11);
        TextView txtcnrmbook = findViewById(R.id.txtconfrmbook);
         txtcnrmbook1 = findViewById(R.id.txtconfrmbook1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), YourBooking.class));
            }
        });
//        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//        Calendar cal = Calendar.getInstance();
//        String time=dateFormat.format(cal.getTime());
//        Toast.makeText(this, time, Toast.LENGTH_SHORT).show();


        txtBikeName = findViewById(R.id.txtBikeName);
        txtBikeNumber = findViewById(R.id.txtBikeNumber);
        txtbikeNoOfHour = findViewById(R.id.txtNoOfHour);
        txtTotalAmount = findViewById(R.id.txtItemTotal);
        txtTotalDiscount = findViewById(R.id.txtTotalDiscount);
        txtToPay = findViewById(R.id.txtToPay);

        txtBikeName.setText(new SharedPrefManager(getApplicationContext()).getBikeName());
        txtBikeNumber.setText(new SharedPrefManager(getApplicationContext()).getBikeNumber());
        txtbikeNoOfHour.setText(new SharedPrefManager(getApplicationContext()).getSelectedhour());

        Thread thread1 = new Thread() {
            @Override
            public void run() {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NetworkCall();
                    }
                });
            }
        };
        thread1.start();

        Typeface bold = Typeface.createFromAsset(getAssets(),
                "Montserrat-Light.otf");
        txtcnrmbook.setTypeface(bold);
        txtcnrmbook1.setTypeface(bold);

        Bundle extras = getIntent().getExtras();
        price = new SharedPrefManager(getApplicationContext()).getSelectedPrice().substring(2);

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
                        sharedPrefManager.getFirstName());
            }
        });


        //BookNetworkCall("7879876789", "987889808", "8778987", "02:00", "2019/03/20", "76678978", "9870987","1");
    }

    private void NetworkCall() {

        viewDialog.showDialog();
        ApiServices apiService = ApiClient.getClient(getApplicationContext()).create(ApiServices.class);

        Call<AmountItem> call = apiService.AmountPay(new SharedPrefManager(getApplicationContext()).getPhoneNumber(), new SharedPrefManager(getApplicationContext()).getSelectedhour());
        call.enqueue(new Callback<AmountItem>() {
            @Override
            public void onResponse(Call<AmountItem> call, Response<AmountItem> response) {
                btnbook.setClickable(true);
                AmountItem book = response.body();
                txtTotalAmount.setText("₹" + book.getPrice().toString());
                txtTotalDiscount.setText("- " + "₹" + book.getDiscount().toString());
                txtToPay.setText(" ₹" + book.getTopay().toString());
                txtcnrmbook1.setText("Confirm your payment of " + " ₹" + book.getTopay().toString() + " and enjoy the ride in the lap of nature");
                Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                viewDialog.hideDialog();
            }

            @Override
            public void onFailure(Call<AmountItem> call, Throwable t) {
                // Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_LONG).show();
//                Toast.makeText(MapActivity.this, "No timing are available!", Toast.LENGTH_SHORT).show();
//                mShimmerViewContainer.stopShimmer();
//                mShimmerViewContainer.setVisibility(View.GONE);
                viewDialog.hideDialog();
                btnbook.setClickable(false);
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

                // }


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
                Date c = Calendar.getInstance().getTime();
                //System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                String formattedDate = df.format(c);

                Log.d("BKC", "Orderid " + orderid + "\npaymentid " + paymentId + "\ntoken " + token);
                Log.d("BKC", response);

                Toast.makeText(ConfirmBookingFinal.this, "Orderid " + orderid + "\npaymentid " + paymentId + "\ntoken " + token, Toast.LENGTH_SHORT).show();
                try {

                    Thread thread1 = new Thread() {
                        @Override
                        public void run() {


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String rearray[] = response.split(":");
                                    String orderid = rearray[1].substring(rearray[1].indexOf("=") + 1);
                                    //Toast.makeText(getApplicationContext(), orderid, Toast.LENGTH_LONG).show();


                                    String paymentId = rearray[3].substring(rearray[3].indexOf("=") + 1);
                                    String token = rearray[4].substring(rearray[4].indexOf("=") + 1);
                                    Date c = Calendar.getInstance().getTime();
                                    //System.out.println("Current time => " + c);

                                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                                    Calendar cal = Calendar.getInstance();
                                     String time=dateFormat.format(cal.getTime());

                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                    String formattedDate = df.format(c);

                                    Log.d("BKC", "Orderid " + orderid + "\npaymentid " + paymentId + "\ntoken " + token);
                                    Log.d("BKC", response);

                                    Toast.makeText(ConfirmBookingFinal.this, "Orderid " + orderid + "\npaymentid " + paymentId + "\ntoken " + token, Toast.LENGTH_SHORT).show();

                                    BookNetworkCall(orderid, paymentId, new SharedPrefManager(getApplicationContext()).getBikeNumber(), time, formattedDate, new SharedPrefManager(getApplicationContext()).getPhoneNumber(),token,new SharedPrefManager(getApplicationContext()).getSelectedhour());
                                }
                            });
                        }
                    };
                    thread1.start();

                } catch (Exception e) {
                    onFailure(1, "Failed To Book Your Ride");


                    fabProgressCircle.beginFinalAnimation();
                    fabProgressCircle.onCompleteFABAnimationEnd();
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
                            .show();
                }

            }

            @Override
            public void onFailure(int code, String reason) {
                fabProgressCircle.onArcAnimationComplete();
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();
            }
        };
    }

    private void BookNetworkCall(String bno, String paymentid, String bikeno, String time, String date, String number, String token,String noh) {

        ApiServices apiService = ApiClient.getClient(getApplicationContext()).create(ApiServices.class);

        Call<BookSuccess> call = apiService.BookSuccessful(number,bno,noh,paymentid,date,time,bikeno,token);
        call.enqueue(new Callback<BookSuccess>() {
            @Override
            public void onResponse(Call<BookSuccess> call, Response<BookSuccess> response) {

                BookSuccess book = response.body();
               Intent i = new Intent(ConfirmBookingFinal.this, BookedSuccess.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.swipe_up);
                finish();

                Toast.makeText(getApplicationContext(), book.getError(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<BookSuccess> call, Throwable t) {
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


//        }
        });


    }
}
