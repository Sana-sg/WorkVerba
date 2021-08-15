package com.sana.timetable;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;


public class FourthFragment extends Fragment {
    private TextInputEditText subject,start_time,end_time,room;
   private AppCompatButton add;
    TimePickerDialog timePickerDialog;
    Context thiscontext;
    int ehour, emin, shour, smin;
    DatabaseHelper myDB;
    boolean status;
    Spinner droplist;
    String day;
    String $subject;
    int cyear,cmonth,cday;
    AlarmManager alarmManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_fourth,container,false);
        subject=v.findViewById(R.id.subject);
        start_time=v.findViewById(R.id.start_time);
        end_time=v.findViewById(R.id.end_time);
        room=v.findViewById(R.id.room);
        droplist=v.findViewById(R.id.spin);
        createNotificationChannel();
        $subject=subject.getText().toString();
       droplist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               day=parent.getItemAtPosition(position).toString();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
        add=v.findViewById(R.id.add);
        thiscontext=container.getContext();
        myDB=new DatabaseHelper(container.getContext());
        addStartTime();
        addEndTime();
        addData();
        return v;
    }
    public void addStartTime(){
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c1=Calendar.getInstance();
                int chour1=c1.get(Calendar.HOUR_OF_DAY);
                int cmin1=c1.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(thiscontext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        start_time.setText(hourOfDay+":"+minute);
                        shour=hourOfDay;
                        smin=minute;
                    }
                }, chour1,cmin1, false);
                timePickerDialog.show();

            }
        });
    }
    public void addEndTime(){
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c2=Calendar.getInstance();
                int chour2=c2.get(Calendar.HOUR_OF_DAY);
                int cmin2=c2.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(thiscontext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        end_time.setText(hourOfDay+":"+minute);
                        ehour=hourOfDay;
                        emin=minute;
                    }
                }, chour2,cmin2, false);
                timePickerDialog.show();

            }
        });

    }
    public void addData() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject1, start_time1, end_time1, room1;
                subject1 = subject.getText().toString();
                start_time1 = start_time.getText().toString();
                end_time1 = end_time.getText().toString();
                room1 = room.getText().toString();
                status = myDB.insertData(subject1, day, start_time1, end_time1, room1);
                if (!TextUtils.isEmpty(subject.getText().toString()) && !TextUtils.isEmpty(room.getText().toString())) {
                if (status) {
                    Toast.makeText(thiscontext, "Record Added", Toast.LENGTH_SHORT).show();
                    subject.setText("");
                    //day.setText("");
                    start_time.setText("");
                    end_time.setText("");
                    room.setText("");
                    addAlarm();
                } else

                    Toast.makeText(thiscontext, "Error!! Record not added", Toast.LENGTH_SHORT).show();

            }
                else {
                    Toast.makeText(thiscontext, "All fields required!!", Toast.LENGTH_SHORT).show();

                }     }
        });

    }
    public void addAlarm(){
        Calendar cal = Calendar.getInstance();
        if(day == "SUNDAY" || day == "Sunday") {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        }
        else if(day == "MONDAY" || day == "Monday"){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        }
        else if(day == "TUESDAY" || day == "Tuesday"){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        }
        else if(day == "WEDNESDAY" || day == "Wednesday"){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        }
        else if(day == "THURSDAY" || day == "Thursday"){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        }
        else if(day == "FRIDAY" || day == "Friday"){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        }
        else if(day == "SATURDAY" || day == "Saturday"){
            cal.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
        }
        cal.set(Calendar.HOUR_OF_DAY,shour);
        cal.set(Calendar.MINUTE,smin);
        Intent intent = new Intent(thiscontext, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(thiscontext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager=(AlarmManager)thiscontext.getSystemService(thiscontext.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),7*24*60*60*1000, pendingIntent);
    }
    private  void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name="WorkVerba";
            String description ="Class time";
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel("WorkVerba",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager=this.getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}