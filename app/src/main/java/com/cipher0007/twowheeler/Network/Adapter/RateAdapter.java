package com.cipher0007.twowheeler.Network.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.cipher0007.twowheeler.Network.Models.BikeNo;
import com.cipher0007.twowheeler.Network.Models.Rate;
import com.cipher0007.twowheeler.OtpVerification.OtpVerfication;
import com.cipher0007.twowheeler.OtpVerification.SharedPrefManager;
import com.cipher0007.twowheeler.R;
import com.cipher0007.twowheeler.SplashScreen;

import org.w3c.dom.Text;

import java.util.List;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateViewHolder> {
    Context context;
    public List<Rate> list;

    public RateAdapter(Context context, List<Rate> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext()).inflate(R.layout.rates_item_card, parent, false);
        RateAdapter.RateViewHolder viewHolder = new RateViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RateViewHolder rateViewHolder, int i) {
        final Rate iitem = list.get(i);
        rateViewHolder.txtHours.setText(iitem.getNoh());
        rateViewHolder.txtRate.setText("₹"+iitem.getPrice());
        rateViewHolder.btnRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, iitem.getNoh().toString(), Toast.LENGTH_SHORT).show();
                new SharedPrefManager(context).setSelectedRide(iitem.getNoh().toString(), iitem.getPrice().toString());
                Intent intent = new Intent(context, com.cipher0007.twowheeler.BikeNo.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static class RateViewHolder extends RecyclerView.ViewHolder {
        public TextView txtHours, txtRate;
        public LinearLayout btnRide;

        public RateViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHours = itemView.findViewById(R.id.txtHours);
            txtRate = itemView.findViewById(R.id.txtPrice);
            btnRide = itemView.findViewById(R.id.btnRide);

        }
    }
}
