package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


//Class to hold all buttons that open different activities
public class FrontActivity extends AppCompatActivity {

    //Initialize UI elements and variables
    private ImageButton ui_logout;
    private ImageButton ui_calendar;
    private ImageButton ui_map;
    private ImageButton ui_notepad;
    private ImageButton ui_search;
    private ImageButton ui_drive;
    private ImageButton ui_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        //connect ui elements
        ui_logout = (ImageButton)findViewById(R.id.bLogout);
        ui_calendar = (ImageButton)findViewById(R.id.bCalendar);
        ui_map = (ImageButton)findViewById(R.id.bMap);
        ui_notepad = (ImageButton)findViewById(R.id.bNotepad);
        ui_search = (ImageButton)findViewById(R.id.bSearch);
        ui_drive = (ImageButton)findViewById(R.id.bDrive);
        ui_camera = (ImageButton)findViewById(R.id.bCamera);

        //all onclick methods that open different activities
        ui_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ui_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FrontActivity.this,CalendarActivity.class));
            }
        });
        ui_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Will be implemented after I figure out Google billing and api usage
                //startActivity(new Intent(FrontActivity.this,MapsActivity.class));
            }
        });
        ui_notepad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FrontActivity.this,NotepadActivity.class));
            }
        });
        ui_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Will be implemented after I figure out Google billing and api usage
                //startActivity(new Intent(FrontActivity.this,SearchActivity.class));
            }
        });
        ui_drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Will be implemented after I figure out Google billing and api usage
                //startActivity(new Intent(FrontActivity.this,DriveActivity.class));
            }
        });
        ui_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}