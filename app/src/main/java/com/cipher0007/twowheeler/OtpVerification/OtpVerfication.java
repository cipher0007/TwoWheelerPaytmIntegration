package com.cipher0007.twowheeler.OtpVerification;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cipher0007.twowheeler.CurrentTrip;
import com.cipher0007.twowheeler.MapActivity;
import com.cipher0007.twowheeler.R;
import com.cipher0007.twowheeler.SplashScreen;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpVerfication extends AppCompatActivity {
    private Spinner countrySpinner;
    EditText etphoneNumber;
    FABProgressCircle fabProgressCircle;
    FloatingActionButton floatingActionButton;
    TextView starttext;
    int count = 0;

    String phoneNumber, otp;
    FirebaseAuth auth;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp_verfication);
        etphoneNumber = findViewById(R.id.phonenumber_otp);
        addspace(etphoneNumber);
        countrySpinner = findViewById(R.id.spinnerCountries);
        countrySpinner.setPopupBackgroundResource(R.color.colorPrimaryDark);
        countrySpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        countrySpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, CountryData.countryNames));

        starttext = findViewById(R.id.textstart);


        Typeface face = Typeface.createFromAsset(getAssets(),
                "Montserrat-Regular.otf");

        etphoneNumber.setTypeface(face);
        starttext.setTypeface(face);
        Typeface face1 = Typeface.createFromAsset(getAssets(),
                "Montserrat-Light.otf");

        fabProgressCircle = findViewById(R.id.fabProgressCircle1);
        floatingActionButton = findViewById(R.id.sendfab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etphoneNumber.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();

                } else {
                    String code = CountryData.countryAreaCodes[countrySpinner.getSelectedItemPosition()];
                    String number = etphoneNumber.getText().toString().trim();
                    String result = number.replaceAll("[-+.^:,]", "");
                    phoneNumber = "+" + code + result;
                    sendDataForVerification(phoneNumber);

                }
            }
        });


    }

    private void sendDataForVerification(String phoneNumber) {

//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneNumber,                     // Phone number to verify
//                60,                           // Timeout duration
//                TimeUnit.SECONDS,                // Unit of timeout
//                OtpVerfication.this,        // Activity (for callback binding)
//                mCallback);                      // OnVerificationStateChangedCallbacks

        //  auth = FirebaseAuth.getInstance();


//        String _deviceToken = SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken();

        Intent intent = new Intent(OtpVerfication.this, VerifyPhone.class);

        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("deviceToken", "hgggjhgj");

        startActivity(intent);
        finish();
    }


    private void addspace(final EditText ccEditText) {
        ccEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (count <= ccEditText.getText().toString().length()
                        && (ccEditText.getText().toString().length() == 4
                        || ccEditText.getText().toString().length() == 9
                        || ccEditText.getText().toString().length() == 14)) {
                    ccEditText.setText(ccEditText.getText().toString() + "-");
                    int pos = ccEditText.getText().length();
                    ccEditText.setSelection(pos);
                } else if (count >= ccEditText.getText().toString().length()
                        && (ccEditText.getText().toString().length() == 4
                        || ccEditText.getText().toString().length() == 9
                        || ccEditText.getText().toString().length() == 14)) {
                    ccEditText.setText(ccEditText.getText().toString().substring(0, ccEditText.getText().toString().length() - 1));
                    int pos = ccEditText.getText().length();
                    ccEditText.setSelection(pos);
                }
                count = ccEditText.getText().toString().length();
            }
        });

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

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(OtpVerfication.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }
//        if (new SharedPrefManager(getApplicationContext()).OtpVerified()) {
//            Intent intent = new Intent(this, UserDetail.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        } else {
//            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//                Intent intent = new Intent(this, UserDetail.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//            }
//        }
//        if (new SharedPrefManager(getApplicationContext()).isLoggedIn()) {
//            Intent intent = new Intent(this, MapActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        }
// else {
//            Intent i = new Intent(OtpVerfication.this, CurrentTrip.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            // close this activity
//            finish();
//        }

    }
}
