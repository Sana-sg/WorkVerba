package com.sana.timetable;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class FirstFragment extends Fragment {
    CardView monday,tuesday,wednesday,thursday,friday,saturday,sunday;
    Context thiscontext;
    String $day;
    FloatingActionButton fab,fab1,fab2;
    Animation fabOpen,fabClose,rotateForward,rotateBackward;
    boolean isOpen = false;
    AlarmManager alarmManager;
    SimpleDateFormat dateFormat;
    private String date;
    int cyear,cmonth,cday,chour,cmin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_first,container,false);
        createNotificationChannel();
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
//            Window w = getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        monday=v.findViewById(R.id.mon);
        tuesday=v.findViewById(R.id.tue);
        wednesday=v.findViewById(R.id.wed);
        thursday=v.findViewById(R.id.thur);
        friday=v.findViewById(R.id.fri);
        saturday=v.findViewById(R.id.sat);
        sunday=v.findViewById(R.id.sun);
        thiscontext=container.getContext();
        fab=v.findViewById(R.id.fab);
        fab1=v.findViewById(R.id.fab1);
        fab2=v.findViewById(R.id.fab2);
        fabOpen= AnimationUtils.loadAnimation(thiscontext,R.anim.tab_open);
        fabClose= AnimationUtils.loadAnimation(thiscontext,R.anim.tab_close);
        rotateForward= AnimationUtils.loadAnimation(thiscontext,R.anim.rotate_forward);
        rotateBackward= AnimationUtils.loadAnimation(thiscontext,R.anim.rotate_backward);
        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                $day="Monday";
                sendDay();
            }
        });
        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                $day="Tuesday";
                sendDay();
            }
        });
        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                $day="Wednesday";
                sendDay();
            }
        });
        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                $day="Thursday";
                sendDay();
            }
        });
        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                $day="Friday";
                sendDay();
            }
        });
        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                $day="Saturday";
                sendDay();
            }
        });
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                $day="Sunday";
                sendDay();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(thiscontext,"5 minute break is set",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ring();
                    }
                },300000);
                animateFab();

            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(thiscontext,"10 minute break is set",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ring();
                    }
                },600000);
                animateFab();

            }
        });
        return  v;
    }

    private void animateFab(){
        if(isOpen){
            fab.startAnimation(rotateBackward);
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isOpen=false;

        }
        else{
            fab.startAnimation(rotateForward);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isOpen=true;

        }
    }
    public void sendDay(){
        Bundle bundle=new Bundle();
        bundle.putString("day",$day);
        Intent intent = new Intent(thiscontext,Monday.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void ring(){
        Calendar cal = Calendar.getInstance();
        cyear=cal.get(Calendar.YEAR);
        cmonth=cal.get(Calendar.MONTH);
        cday=cal.get(Calendar.DAY_OF_MONTH);
        chour=cal.get(Calendar.HOUR_OF_DAY);
        cmin=cal.get(Calendar.MINUTE);
        cal.set(cyear,cmonth,cday,chour,cmin);
        Intent intent = new Intent(thiscontext, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(thiscontext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager=(AlarmManager)thiscontext.getSystemService(thiscontext.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(), pendingIntent);
    }
    private  void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name="WorkVerba";
            String description =" Break time over";
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel("WorkVerba",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager=this.getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}