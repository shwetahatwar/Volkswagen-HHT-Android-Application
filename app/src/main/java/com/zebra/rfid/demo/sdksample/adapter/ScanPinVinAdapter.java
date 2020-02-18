package com.zebra.rfid.demo.sdksample.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zebra.rfid.demo.sdksample.R;
import com.zebra.rfid.demo.sdksample.database.ScanPinVin;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanPinVinAdapter extends RecyclerView.Adapter<ScanPinVinAdapter.ViewHolder> {

    private final Listener listener;

    public interface Listener {
        void onAdd(int index);
    }

    private final Context context;
    private final ArrayList<ScanPinVin> scanpinvin;


    public ScanPinVinAdapter(Context context, ArrayList<ScanPinVin> scanpinvin, Listener listener) {
        this.context = context;
        this.scanpinvin = scanpinvin;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_scanpinvin, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        ScanPinVin scanPinVin = scanpinvin.get(i);

        viewHolder.textTitle.setText(scanPinVin.getId());
        viewHolder.textPrice.setText("" + scanPinVin.getPinno());
        viewHolder.textCompany.setText(scanPinVin.getVinno());
        viewHolder.textUnit.setText(scanPinVin.getTag());

        //Network.loadImage(context, item.getThumbnail(), viewHolder.imageView);

        viewHolder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAdd(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scanpinvin.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textCompany) TextView textCompany;
        @BindView(R.id.textTitle) TextView textTitle;
        @BindView(R.id.textUnit) TextView textUnit;
        @BindView(R.id.textPrice) TextView textPrice;
        @BindView(R.id.buttonAdd) Button buttonAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
