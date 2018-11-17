package com.cipher0007.twowheeler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ConfirmBook extends AppCompatActivity {
private LinearLayout btnconfirm;

public static final String MID="easysc61523958497276";
public static final String INDUSTRY_TYPE_ID="Retail";
public static final String CHANNEL_ID="WAP";
public static final String WEBSITE="APPSTAGING";
public static final String CALLBACK_URL="https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
public static final String MOBILE_NO="8958777028";

public String amt="1";
public String custid="121";
public String CHECKSUM="";
public String ORDERID="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_book);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        btnconfirm=findViewById(R.id.btnConfirmbook);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              GenerateCheckSum();
            }
        });



    }
    //This is to refresh the order id: Only for the Sample App's purpose.
    @Override
    protected void onStart() {
        super.onStart();
        //initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void GenerateCheckSum(){
        Random r = new Random(System.currentTimeMillis());
         ORDERID = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);

         String url="http://u431166694.hostingerapp.com/paytm/checksum.php";

        Map<String,String> params=new HashMap<String,String>();
        params.put("MID",MID);
        params.put("ORDER_ID",ORDERID);
        params.put("CUST_ID",custid);
        params.put("INDUSTRY_TYPE_ID",INDUSTRY_TYPE_ID);
        params.put("CHANNEL_ID",CHANNEL_ID);
        params.put("TXN_AMOUNT",amt);
        params.put("CALLBACK_URL",CALLBACK_URL);
        params.put("WEBSITE",WEBSITE);
        params.put("MOBILE_NO",MOBILE_NO);


        JSONObject param=new JSONObject(params);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                CHECKSUM=response.optString("CHECKSUMHASH");

                if (CHECKSUM.trim().length() !=0){
                    onStartTransaction();
                }
                Log.e("getresponse",String.valueOf(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("getresponse",String.valueOf(error));
            }
        });


        Volley.newRequestQueue(this).add(jsonObjectRequest);



    }

    public void onStartTransaction() {
        //PaytmPGService Service = PaytmPGService.getProductionService();
        PaytmPGService Service = PaytmPGService.getStagingService();
        Map<String, String> paramMap = new HashMap<String, String>();

        // these are mandatory parameters

//        paramMap.put("CALLBACK_URL",CALLBACK_URL);
//        paramMap.put("CHANNEL_ID",CHANNEL_ID);
//        paramMap.put("CHECKSUMHASH",CHECKSUM);
//        paramMap.put("CUST_ID",custid);
//        paramMap.put("INDUSTRY_TYPE_ID",INDUSTRY_TYPE_ID);
//        paramMap.put("MID",MID);
//        paramMap.put("ORDER_ID",ORDERID);
//        paramMap.put("TXN_AMOUNT",amt);
//        paramMap.put("WEBSITE",WEBSITE);
        paramMap.put("MID",MID);
        paramMap.put("ORDER_ID",ORDERID);
        paramMap.put("CHECKSUMHASH",CHECKSUM);
        paramMap.put("CUST_ID",custid);
        paramMap.put("INDUSTRY_TYPE_ID",INDUSTRY_TYPE_ID);
        paramMap.put("CHANNEL_ID",CHANNEL_ID);
        paramMap.put("TXN_AMOUNT",amt);
        paramMap.put("WEBSITE",WEBSITE);
        paramMap.put("CALLBACK_URL",CALLBACK_URL);
        paramMap.put("MOBILE_NO",MOBILE_NO);

/*
        paramMap.put("MID" , "WorldP64425807474247");
        paramMap.put("ORDER_ID" , "210lkldfka2a27");
        paramMap.put("CUST_ID" , "mkjNYC1227");
        paramMap.put("INDUSTRY_TYPE_ID" , "Retail");
        paramMap.put("CHANNEL_ID" , "WAP");
        paramMap.put("TXN_AMOUNT" , "1");
        paramMap.put("WEBSITE" , "worldpressplg");
        paramMap.put("CALLBACK_URL" , "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/


        PaytmOrder Order = new PaytmOrder(paramMap);

		/*PaytmMerchant Merchant = new PaytmMerchant(
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/

        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }

					/*@Override
					public void onTransactionSuccess(Bundle inResponse) {
						// After successful transaction this method gets called.
						// // Response bundle contains the merchant response
						// parameters.
						Log.d("LOG", "Payment Transaction is successful " + inResponse);
						Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
					}
					@Override
					public void onTransactionFailure(String inErrorMessage,
							Bundle inResponse) {
						// This method gets called if transaction failed. //
						// Here in this case transaction is completed, but with
						// a failure. // Error Message describes the reason for
						// failure. // Response bundle contains the merchant
						// response parameters.
						Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
						Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
					}*/

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        Toast.makeText(ConfirmBook.this,"Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }

                });
    }
}
