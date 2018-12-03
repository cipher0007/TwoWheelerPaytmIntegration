package com.cipher0007.twowheeler;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class UserDetail extends AppCompatActivity {
EditText editTextfirstName,editTextlastName,editTextemail;
FloatingActionButton SubmitButtonUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        editTextfirstName=findViewById(R.id.FirstNameUser);
        editTextlastName=findViewById(R.id.LastNameUser);
        editTextemail=findViewById(R.id.EmailIdUser);
        SubmitButtonUser=findViewById(R.id.SubmitButtonUser);


        SubmitButtonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SharedPrefManager(getApplicationContext()).saveLoginDetails(editTextfirstName.getText().toString().trim(),editTextlastName.getText().toString().trim(),editTextemail.getText().toString().trim(),true);
                Intent i = new Intent(UserDetail.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                // close this activity
                finish();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        if (new SharedPrefManager(getApplicationContext()).isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
