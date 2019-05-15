package com.cipher0007.twowheeler;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BookedSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_success);
        TextView booksuccess=findViewById(R.id.txtbooksuccess);
        Typeface bold = Typeface.createFromAsset(getAssets(),
                "Montserrat-Light.otf");
        booksuccess.setTypeface(bold);
        //Intent intent=new In

    }
}
