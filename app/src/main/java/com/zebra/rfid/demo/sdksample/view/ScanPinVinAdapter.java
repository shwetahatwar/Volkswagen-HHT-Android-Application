package com.zebra.rfid.demo.sdksample.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zebra.rfid.demo.sdksample.R;
import com.zebra.rfid.demo.sdksample.database.ScanPinVin;

import java.util.List;

public class ScanPinVinAdapter extends RecyclerView.Adapter<ScanPinVinAdapter.MyViewHolder> {

    private Context context;
    private List<ScanPinVin> scanpinvinList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView pin;
        public TextView vin;
        public TextView tag;

        public MyViewHolder(View view) {
            super(view);
            pin = view.findViewById(R.id.pin);
            vin = view.findViewById(R.id.vinValue);
            tag = view.findViewById(R.id.tagPinNumber);
        }
    }

    public ScanPinVinAdapter(Context context, List<ScanPinVin> scanpinvinList) {
        this.context = context;
        this.scanpinvinList = scanpinvinList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scanvinpin_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ScanPinVin scanvinpin = scanpinvinList.get(position);
        //holder.scanvinpin.setText(scanvinpin.getScanPinVin());

        // Displaying dot from HTML character code
        holder.pin.setText(Html.fromHtml("&#8226;"));
        holder.vin.setText(Html.fromHtml("#000000"));

        // Formatting and displaying timestamp
        holder.tag.setText((scanvinpin.getTag()));
    }

    @Override
    public int getItemCount() {
        return scanpinvinList.size();
    }

}
