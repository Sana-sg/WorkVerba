package com.sana.timetable;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    ArrayList<DataModel> dataholder = new ArrayList<DataModel>();

    public MyAdapter(ArrayList<DataModel> dataholder) {
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.customrow,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.t3.setText(dataholder.get(position).getSubject());
        holder.t1.setText(dataholder.get(position).getStart_time());
        holder.t2.setText(dataholder.get(position).getEnd_time());
        holder.t4.setText(dataholder.get(position).getRoom());


    }



    @Override
    public int getItemCount() {
        return dataholder.size();
    }
    public ArrayList<DataModel> getList() {
        return dataholder;
    }

    public void removeItem(int position) {
        dataholder.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(DataModel item, int position) {
        dataholder.add(position, item);
        notifyItemInserted(position);
    }


}
