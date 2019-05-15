package com.cipher0007.twowheeler.Network.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.cipher0007.twowheeler.Network.Models.Rate;
import com.cipher0007.twowheeler.Network.Models.YourBookingItem;
import com.cipher0007.twowheeler.R;

import java.util.List;

public class YourBookingAdapter extends RecyclerView.Adapter<YourBookingAdapter.BookingViewHolder>{
    Context context;
    public List<YourBookingItem> list;

    public YourBookingAdapter(Context context, List<YourBookingItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.from(parent.getContext()).inflate(R.layout.your_booking_item, parent, false);
        YourBookingAdapter.BookingViewHolder viewHolder = new BookingViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder bookingViewHolder, int i) {
        final YourBookingItem iitem = list.get(i);
        bookingViewHolder.txtBikeNumber.setText(iitem.getBikeno());
        bookingViewHolder.txtBookingNumber.setText(iitem.getBno());
        //bookingViewHolder.txtPhoneNumber.setText(iitem.getPno());
        bookingViewHolder.txtNumberOfHours.setText(iitem.getNoh());
        bookingViewHolder.txtDateOfBook.setText(iitem.getDobook());
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        public TextView txtBikeNumber, txtPhoneNumber,txtBookingNumber, txtNumberOfHours, txtDateOfBook;
        //public LinearLayout btnRide;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBikeNumber=itemView.findViewById(R.id.bikeno);
            txtBookingNumber=itemView.findViewById(R.id.bno);
           // txtPhoneNumber=itemView.findViewById(R.id.pno);
            txtNumberOfHours=itemView.findViewById(R.id.noh);
            txtDateOfBook=itemView.findViewById(R.id.dobook);
            //btnRide=itemView.findViewById(R.id.btnRide);

        }
    }
}
