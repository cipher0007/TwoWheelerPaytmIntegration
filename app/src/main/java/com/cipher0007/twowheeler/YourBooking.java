package com.cipher0007.twowheeler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.cipher0007.twowheeler.Network.Adapter.YourBookingAdapter;
import com.cipher0007.twowheeler.Network.ApiClient;
import com.cipher0007.twowheeler.Network.ApiServices;
import com.cipher0007.twowheeler.Network.Models.YourBookingItem;
import com.cipher0007.twowheeler.OtpVerification.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourBooking extends AppCompatActivity {
RecyclerView recyclerView;
    ViewDialog viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_bk);
        recyclerView=findViewById(R.id.recyclerViewBookings);
        viewDialog = new ViewDialog(YourBooking.this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MapActivity.class));
            }
        });

        NetworkCall();
    }

    private void NetworkCall() {
        viewDialog.showDialog();
        ApiServices apiService = ApiClient.getClient(getApplicationContext()).create(ApiServices.class);

        Call<List<YourBookingItem>> call = apiService.Bookings(new SharedPrefManager(getApplicationContext()).getPhoneNumber());
        call.enqueue(new Callback<List<YourBookingItem>>() {
            @Override
            public void onResponse(Call<List<YourBookingItem>> call, Response<List<YourBookingItem>> response) {
//                mShimmerViewContainer.stopShimmer();
//                mShimmerViewContainer.setVisibility(View.GONE);
                List<YourBookingItem> book = response.body();
                Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
               recyclerView.setAdapter(new YourBookingAdapter(getApplicationContext(), book));
               viewDialog.hideDialog();
            }

            @Override
            public void onFailure(Call<List<YourBookingItem>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_LONG).show();
//                Toast.makeText(MapActivity.this, "No timing are available!", Toast.LENGTH_SHORT).show();
//                mShimmerViewContainer.stopShimmer();
//                mShimmerViewContainer.setVisibility(View.GONE);
                viewDialog.hideDialog();
            }
        });

    }
}
