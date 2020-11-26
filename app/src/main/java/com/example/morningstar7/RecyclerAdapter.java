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


    private ArrayList<SyncEntry> arrayList = new ArrayList<>();

    public RecyclerAdapter(ArrayList<SyncEntry> arrayList){
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.entry.setText(arrayList.get(position).getScan_id());
        int sync_status = arrayList.get(position).getSync_status();
        if(sync_status == DataBaseHelper.SYNC_STATUS_OK){
            holder.Sync_Status.setImageResource(R.drawable.sync_success);
        }
        else{
            holder.Sync_Status.setImageResource(R.drawable.synchronize);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView Sync_Status;
        TextView entry;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Sync_Status = (ImageView)itemView.findViewById(R.id.imgSync);
            entry = (TextView)itemView.findViewById(R.id.textView);
        }
    }
}
