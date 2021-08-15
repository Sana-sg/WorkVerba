package com.sana.timetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.zip.DataFormatException;

public class Monday extends AppCompatActivity {
    RecyclerView rcv;
    MyAdapter adapter;
    DatabaseHelper myDB;
    Cursor cursor;
    String day;
    ImageView back;
    CoordinatorLayout coordinatorLayout;
    LottieAnimationView anim1;

    ArrayList<DataModel> dataholder = new ArrayList<DataModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monday);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        back = (ImageView) findViewById(R.id.back);
        coordinatorLayout = findViewById(R.id.layout_rcv);
        anim1=findViewById(R.id.anim1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Monday.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            day = bundle.getString("day");
        }

        rcv = (RecyclerView) findViewById(R.id.rclview);
        rcv.setLayoutManager(new LinearLayoutManager(this));

        myDB = new DatabaseHelper(this);
        cursor = myDB.readData(day);
        if(cursor.getCount()==0){
            anim1.setVisibility(View.VISIBLE);

        }

        while (cursor.moveToNext()) {
            DataModel obj = new DataModel(cursor.getString(1), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getString(0));
            dataholder.add(obj);
        }
        adapter = new MyAdapter(dataholder);
        rcv.setAdapter(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rcv);
    }
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT|
            ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        int position = viewHolder.getAdapterPosition();
        DataModel item = adapter.getList().get(position);

        adapter.removeItem(viewHolder.getAdapterPosition());

        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Item Deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.restoreItem(item, position);
                        rcv.scrollToPosition(position);
                    }
                }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);

                        if (!(event == DISMISS_EVENT_ACTION)) {
                            DatabaseHelper db = new DatabaseHelper(Monday.this);
                            boolean result=db.deleteItem(item.getId());
                            if (result == false ) {
                                Toast.makeText(Monday.this, "Item Not Deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Monday.this, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }

                        }


                    }
                });

        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();

    }
};
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}