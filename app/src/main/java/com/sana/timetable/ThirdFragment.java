package com.sana.timetable;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;


public class ThirdFragment extends Fragment {
    ImageButton b1, b2, b3;
    Chronometer counter;
    Animation anchor_move;
    ImageView anchor;
    long stopTime=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_third, container, false);
                b1=v.findViewById(R.id.button1);
                b2=v.findViewById(R.id.button2);
                b3=v.findViewById(R.id.button3);
                anchor=v.findViewById(R.id.anchor);
                counter=v.findViewById(R.id.counter);
                anchor_move= AnimationUtils.loadAnimation(container.getContext(),R.anim.anchor_move);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counter.setBase(SystemClock.elapsedRealtime()+stopTime);
                        counter.start();
                        anchor.startAnimation(anchor_move);
                    }
                });
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stopTime=counter.getBase()-SystemClock.elapsedRealtime();
                        counter.stop();
                        anchor.clearAnimation();
                    }
                });
                b3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counter.setBase(SystemClock.elapsedRealtime());
                        counter.start();
                        stopTime=0;
                        anchor.startAnimation(anchor_move);
                    }
                });

        return v;
        }
}