package com.cipher0007.twowheeler;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp_verfication);
        etphoneNumber = findViewById(R.id.phonenumber_otp);
        addspace(etphoneNumber);
        countrySpinner = findViewById(R.id.spinnerCountries);
        countrySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));

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
    protected void onStart() {
        super.onStart();

        if (new SharedPrefManager(getApplicationContext()).OtpVerified()) {
            Intent intent = new Intent(this, UserDetail.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Intent intent = new Intent(this, UserDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }


    }
}
