package com.sana.timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ViewNote extends AppCompatActivity {
    TextView v_title,descp;
    ImageView edit_note,back;
    String id;
    String $v_title,$descp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        setContentView(R.layout.activity_view_note);

        v_title=(TextView)findViewById(R.id.v_title);
        descp=(TextView)findViewById(R.id.descp);

        edit_note=(ImageView)findViewById(R.id.edit_note);
        back=(ImageView)findViewById(R.id.back);
        Intent i =getIntent();
        v_title.setText(i.getStringExtra("title"));
        descp.setText(i.getStringExtra("description"));
        id=i.getStringExtra("id");

        $v_title=v_title.getText().toString();
        $descp=descp.getText().toString();

        edit_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                Intent intent=new Intent(ViewNote.this,UpdateNotesActivity.class);
                bundle.putString("title",$v_title);
                bundle.putString("description",$descp);
                bundle.putString("id",id);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
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