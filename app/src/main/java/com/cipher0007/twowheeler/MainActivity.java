package com.cipher0007.twowheeler;

import android.content.Intent;
import android.app.Activity;
import instamojo.library.InstapayListener;
import instamojo.library.InstamojoPay;
import instamojo.library.Config;
import org.json.JSONObject;
import org.json.JSONException;
import org.w3c.dom.Text;

import android.content.IntentFilter;
import android.widget.Toast;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
private RelativeLayout bookNow;
private String SERVER_KEY="AIzaSyDtcoeXo7YyBSzrMp3LuD6UM5giIGg0JWM";

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txtbook=findViewById(R.id.txtbook);
        TextView txtprice=findViewById(R.id.txtprice);
        Typeface bold = Typeface.createFromAsset(getAssets(),
                "Montserrat-Light.otf");
        txtbook.setTypeface(bold);
        txtprice.setTypeface(bold);


        RelativeLayout bookNow=findViewById(R.id.btnBookNow);
        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ConfirmBookingFinal.class);
                startActivity(intent);

            }
        });


    }
}
