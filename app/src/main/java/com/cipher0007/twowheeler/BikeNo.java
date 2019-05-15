package com.cipher0007.twowheeler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.cipher0007.twowheeler.Network.ApiClient;
import com.cipher0007.twowheeler.Network.ApiServices;
import com.cipher0007.twowheeler.Network.Adapter.BikeNoAdapter;
import com.cipher0007.twowheeler.OtpVerification.SharedPrefManager;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BikeNo extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_no);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container1);
        mShimmerViewContainer.startShimmer();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.BikeRecyclerView);
        recyclerView.setHasFixedSize(true);
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

        LinearLayoutManager manager =  new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(manager);
        NetworkCall();
    }

    private void NetworkCall() {
        ApiServices apiService = ApiClient.getClient(getApplicationContext()).create(ApiServices.class);

        Call<List<com.cipher0007.twowheeler.Network.Models.BikeNo>> call = apiService.bikeno(new SharedPrefManager(getApplicationContext()).getPhoneNumber());
        call.enqueue(new Callback<List<com.cipher0007.twowheeler.Network.Models.BikeNo>>() {
            @Override
            public void onResponse(Call<List<com.cipher0007.twowheeler.Network.Models.BikeNo>> call, Response<List<com.cipher0007.twowheeler.Network.Models.BikeNo>> response) {
                List<com.cipher0007.twowheeler.Network.Models.BikeNo> bikeno = response.body();
                Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
                recyclerView.setAdapter(new BikeNoAdapter(getApplicationContext(), bikeno));

            }

            @Override
            public void onFailure(Call<List<com.cipher0007.twowheeler.Network.Models.BikeNo>> call, Throwable t) {
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);

            }
        });
    }
}
