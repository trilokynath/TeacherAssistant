package com.trinfosoft.teacherassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class TimeTable extends AppCompatActivity {

    LinearLayout monday_click, tuesday_click, wednesday_click, thursday_click, friday_click, saturday_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        setTitle("Select Day");
        monday_click = (LinearLayout) findViewById(R.id.monday_click);
        monday_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimeTable.this, Monday.class);
                startActivity(intent);
            }
        });
        tuesday_click = (LinearLayout) findViewById(R.id.tuesday_click);
        tuesday_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimeTable.this, Tuesday.class);
                startActivity(intent);
            }
        });
        wednesday_click = (LinearLayout) findViewById(R.id.wednesday_click);
        wednesday_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimeTable.this, Wednesday.class);
                startActivity(intent);
            }
        });
        thursday_click = (LinearLayout) findViewById(R.id.thursday_click);
        thursday_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimeTable.this, Thursday.class);
                startActivity(intent);
            }
        });
        friday_click = (LinearLayout) findViewById(R.id.friday_click);
        friday_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimeTable.this, Friday.class);
                startActivity(intent);
            }
        });
        saturday_click = (LinearLayout) findViewById(R.id.saturday_click);
        saturday_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimeTable.this, Saturday.class);
                startActivity(intent);
            }
        });
    }
}
