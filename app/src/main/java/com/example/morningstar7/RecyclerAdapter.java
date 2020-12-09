package com.example.morningstar7;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<SyncEntry> arrayList;

    public RecyclerAdapter(ArrayList<SyncEntry> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.b_id_txt.setText(arrayList.get(position).getBarcode_id());
        holder.b_row_num.setText(String.valueOf(arrayList.get(position).getBarcode_row()));
        holder.b_sec_num.setText(String.valueOf(arrayList.get(position).getBarcode_sec()));
        int sync_status = arrayList.get(position).getSync_status();
        if(sync_status == DataBaseHelper.SYNC_STATUS_OK) {
            holder.b_sync_status.setImageResource(R.drawable.sync_success);
        }
        else {
            holder.b_sync_status.setImageResource(R.drawable.synchronize);
        }
    }

    @Override
    public int getItemCount() {
        return (arrayList == null) ? 0 : arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView b_id_txt, b_row_num, b_sec_num;
        ImageView b_sync_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            b_id_txt = itemView.findViewById(R.id.barcode_id_txt);
            b_row_num = itemView.findViewById(R.id.barcode_id_row_txt);
            b_sec_num = itemView.findViewById(R.id.bardcode_id_section);
            b_sync_status = itemView.findViewById(R.id.syn_status_img);
        }
    }
}
