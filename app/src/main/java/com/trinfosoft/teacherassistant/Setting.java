package com.trinfosoft.teacherassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

public class Setting extends AppCompatActivity {

    LinearLayout class_setting, subject_setting;
    SwitchCompat mswitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        this.setTitle("Setting");

        class_setting = (LinearLayout) findViewById(R.id.set_class);
        class_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Setting.this, Classes.class);
                startActivity(intent);
            }
        });

        subject_setting = (LinearLayout) findViewById(R.id.set_subject);
        subject_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Setting.this, Subjects.class);
                startActivity(intent);
            }
        });

        mswitch = (SwitchCompat) findViewById(R.id.timerSwitch);
        int status = 0;

        SharedPreferences prefs = getSharedPreferences("status", MODE_PRIVATE);
        if(prefs!=null)
            status = prefs.getInt("st", 0);

        if (status != 0) {
            mswitch.setChecked(true);
        }
        mswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked) {
                    // do something when checked is selected
                    SharedPreferences.Editor editor = getSharedPreferences("status", MODE_PRIVATE).edit();
                    editor.putInt("st", 1);
                    editor.apply();

                } else {
                    //do something when unchecked
                    SharedPreferences.Editor editor = getSharedPreferences("status", MODE_PRIVATE).edit();
                    editor.putInt("st", 0);
                    editor.apply();

                }
            }
        });
    }

}
