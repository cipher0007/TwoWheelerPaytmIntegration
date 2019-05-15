package com.cipher0007.twowheeler.Network.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cipher0007.twowheeler.ConfirmBookingFinal;
import com.cipher0007.twowheeler.Network.Models.BikeNo;
import com.cipher0007.twowheeler.OtpVerification.SharedPrefManager;
import com.cipher0007.twowheeler.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BikeNoAdapter extends RecyclerView.Adapter<BikeNoAdapter.BikeNoViewHolder> {
    Context context;
    public List<BikeNo> list;

    public BikeNoAdapter(Context context, List<BikeNo> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public BikeNoAdapter.BikeNoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext()).inflate(R.layout.bike_no_card_item, parent, false);
        BikeNoViewHolder viewHolder = new BikeNoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BikeNoAdapter.BikeNoViewHolder viewHolder, int i) {
        final BikeNo iitem = list.get(i);
        Picasso.get().load(iitem.getImg()).into(viewHolder.bikeImage);
        viewHolder.bikeName.setText(iitem.getBikename());
        viewHolder.bikeNumber.setText(iitem.getBikeNumber());
        if (iitem.getStatus().toString().trim().equalsIgnoreCase("free")) {
            viewHolder.circleImage.setImageResource(R.drawable.freebookedcircle);

            viewHolder.linerupdateview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,ConfirmBookingFinal.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        } else {
            viewHolder.circleImage.setImageResource(R.drawable.bookedcircle);
            viewHolder.linerupdateview.setCardBackgroundColor(context.getResources().getColor(R.color.placeholder_bg));
            viewHolder.bikeName.setTextColor(Color.WHITE);
            viewHolder.bikeNumber.setTextColor(Color.WHITE);
          //  viewHolder.linerupdateview.setClickable(false);
            viewHolder.linerupdateview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "This Scooter is already booked by someone!", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static class BikeNoViewHolder extends RecyclerView.ViewHolder {
        public ImageView  circleImage;
      //  public CircleImageView bikeImage;
      public ImageView bikeImage;
        public TextView bikeName, bikeNumber;
        public CardView linerupdateview;

        public BikeNoViewHolder(@NonNull View itemView) {
            super(itemView);

            bikeImage = itemView.findViewById(R.id.BikeNoImageRecycler);
            bikeName = itemView.findViewById(R.id.BikeNameRecycler);
            bikeNumber = itemView.findViewById(R.id.BikeNumberRecycler);
            circleImage = itemView.findViewById(R.id.bookcolor);
            linerupdateview = itemView.findViewById(R.id.linerupdateview);


        }
    }
}
