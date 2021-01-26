package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView ui_calendar;
    private FloatingActionButton ui_addNewEvent;
    private ScrollView ui_scrollview;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ui_calendar = (CalendarView)findViewById(R.id.cvMain);
        ui_addNewEvent = (FloatingActionButton)findViewById(R.id.fabAddEvent) ;
        ui_scrollview = (ScrollView)findViewById(R.id.svEventHolder);


        //keeps hold of date changes on calendar, also for some reason calendarview months start at 0 so add 1 to get correct month.
        ui_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String curYear = String.valueOf(year);
                String curMonth = String.valueOf(month+1);
                String curDay = String.valueOf(day);
                date = curYear + "-" + curMonth + "-" + curDay;
                Toast.makeText(getApplicationContext(),date,Toast.LENGTH_SHORT).show();
            }
        });
        //adds event to database
        ui_addNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this,CalendarAddActivity.class);
                intent.putExtra("EXTRA_CALENDAR_DATE",date);
                startActivity(intent);
            }
        });
    }
}