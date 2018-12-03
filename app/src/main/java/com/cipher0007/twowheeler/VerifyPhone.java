package com.cipher0007.twowheeler;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {
    TextView textView, subtext, edittextview, resendText;
    FloatingActionButton floatingActionButton;
    FABProgressCircle fabProgressCircle;
    PinView pinView;


    // EditText editText_one, editText_two, editText_three, editText_four, editText_five, editText_six;
    String otp, phoneNumber, _deviceToken;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;
    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);


        phoneNumber = getIntent().getStringExtra("phoneNumber");
        _deviceToken = getIntent().getStringExtra("deviceToken");
        String substr=phoneNumber.substring(3);
        new SharedPrefManager(this).savePhoneNumber(substr,true);
        initViews();

        startFirebaseLogin();


        //disable the submitOtp button unless the otp section is filled
        floatingActionButton.setEnabled(false);


        //this trigger every time the pin is change
        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (pinView.getText().length() == 6) {

                    floatingActionButton.setEnabled(true);
                } else {
                    floatingActionButton.setEnabled(false);
                }

            }
        });


        //sending sms to the user using Firebase phone authentication
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,                     // Phone number to verify
                60,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                VerifyPhone.this,        // Activity (for callback binding)
                mCallback);                      // OnVerificationStateChangedCallbacks


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabProgressCircle.show();
                fabProgressCircle.beginFinalAnimation();

                // Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
                otp = pinView.getText().toString();//editText_one.getText().toString().trim() + editText_two.getText().toString().trim() + editText_three.getText().toString().trim() + editText_four.getText().toString().trim() + editText_five.getText().toString().trim() + editText_six.getText().toString().trim();

                if (!otp.isEmpty() && otp.length() == 6) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                    signInWithPhone(credential);
                }

                //7456962537

            }
        });


    }

    private void ResendOtp() {
//

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                Typeface face1 = Typeface.createFromAsset(getAssets(),
                        "Montserrat-Light.otf");
                resendText.setTypeface(face1);
                resendText.setText("Resend in: " + millisUntilFinished / 1000);

                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                Typeface bold = Typeface.createFromAsset(getAssets(),
                        "Montserrat-SemiBold.otf");
                resendText.setText("Resend Now!");

                resendText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber,                     // Phone number to verify
                                60,                           // Timeout duration
                                TimeUnit.SECONDS,                // Unit of timeout
                                VerifyPhone.this,        // Activity (for callback binding)
                                mCallback);                      // OnVerificationStateChangedCallbacks

                        ResendOtp();
                    }
                });
            }

        }.start();
    }

    private void signInWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fabProgressCircle.beginFinalAnimation();
                        Intent intent = new Intent(VerifyPhone.this, UserDetail.class);
                        intent.putExtra("phoneNumber", phoneNumber);
                        intent.putExtra("deviceToken", _deviceToken);
                        // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    } else {
                        Toast.makeText(VerifyPhone.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void startFirebaseLogin() {
        ResendOtp();
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                fabProgressCircle.show();
                fabProgressCircle.beginFinalAnimation();
                fabProgressCircle.onCompleteFABAnimationEnd();
                // Toast.makeText(VerifyPhone.this, "verification completed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(VerifyPhone.this, UserDetail.class);
//                intent.putExtra("phoneNumber", phoneNumber);
//                intent.putExtra("deviceToken", _deviceToken);

                //  overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                fabProgressCircle.hide();
                Log.d("OTP", "onVerificationFailed: " + e.getMessage());
                Toast.makeText(VerifyPhone.this, "verification failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;

                Toast.makeText(VerifyPhone.this, "Code sent", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void initViews() {
        pinView = findViewById(R.id.firstPinView);
        textView = findViewById(R.id.verifitext);
        floatingActionButton = findViewById(R.id.fab);
        fabProgressCircle = findViewById(R.id.fabProgressCircle);
        subtext = findViewById(R.id.verifitextsub);
        edittextview = findViewById(R.id.edittext);
        resendText = findViewById(R.id.resendtext);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "Montserrat-Regular.otf");
        textView.setTypeface(face);

        Typeface face1 = Typeface.createFromAsset(getAssets(),
                "Montserrat-Light.otf");
        subtext.setText("Please type the verification code send to " + phoneNumber);
        subtext.setTypeface(face1);
        edittextview.setTypeface(face1);
        resendText.setTypeface(face1);
        edittextview.setOnClickListener(view -> {
            Intent intent = new Intent(VerifyPhone.this, OtpVerfication.class);
            startActivity(intent);
            finish();
        });
    }


}
