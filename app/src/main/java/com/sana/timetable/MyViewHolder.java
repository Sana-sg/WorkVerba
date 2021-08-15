package com.sana.timetable;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView t3,t4;
    Chip t1,t2;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        t1=(Chip)itemView.findViewById(R.id.txt1);
        t2=(Chip)itemView.findViewById(R.id.txt2);
        t3=(TextView)itemView.findViewById(R.id.txt3);
        t4=(TextView)itemView.findViewById(R.id.txt4);
    }
}
