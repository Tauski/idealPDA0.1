package com.example.myapplication;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.Selection;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.util.Clock;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

//CalendarAddactivity holds UI interactions with adding events to database
public class CalendarAddActivity extends AppCompatActivity {

    //Initializing UI elements and variables
    private static final String TAG = "FROM NOTEPAD ACTIVITY";
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private EditText ui_eventName;
    private EditText ui_eventLocation;
    private EditText ui_eventDescription;
    private EditText ui_eventTime;
    private EditText ui_eventDate;
    private Button ui_saveButton;
    private Button ui_deleteButton;
    private String calendarDate;

    //holder for addresses to php script that inserts calendar event into database
    private String urlCalendarEventAdd="http://192.168.1.103:8012/project/Calendar/userEventsInsert.php";
    private String urlCalendarEventDelete="http://192.168.1.103:8012/project/Calendar/userEventsDelete.php";
    private String urlCalendarEventUpdate="htto://192.168.1.103:8012/project/Calendar/userEventsUpdate.php";
    private String urlString; //Choose between add and update
    private String userString;
    private String oldString;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        //Find corresponding objects
        ui_eventName = (EditText)findViewById(R.id.etEventName);
        ui_eventDescription = (EditText)findViewById(R.id.etEventDescription);
        ui_eventLocation = (EditText)findViewById(R.id.etEventLocation);
        ui_eventDate = (EditText)findViewById(R.id.etEventDate);
        ui_eventTime = (EditText)findViewById(R.id.etEventTime);
        ui_saveButton = (Button)findViewById(R.id.bSaveEvent);
        ui_deleteButton = (Button)findViewById(R.id.bEventDelete);

        //get Extras if any (always atleast date) also check if activity should be "update" or "new" type
        boolean modify = false;

        if(getIntent().getStringExtra("EXTRA_EVENTDATE") == null) {
            calendarDate = getIntent().getStringExtra("EXTRA_CALENDAR_DATE");
            urlString = urlCalendarEventAdd;
            oldString = "placeholder for volley parameters";
        }
        else{
            modify = true;
            calendarDate = getIntent().getStringExtra("EXTRA_EVENTDATE");
            ui_eventTime.setText(getIntent().getStringExtra("EXTRA_EVENTTIME"));
            ui_eventLocation.setText(getIntent().getStringExtra("EXTRA_EVENTLOCATION"));
            ui_eventDescription.setText(getIntent().getStringExtra("EXTRA_EVENTDESCRIPTION"));
            ui_eventName.setText(getIntent().getStringExtra("EXTRA_EVENTNAME"));
            urlString = urlCalendarEventUpdate;
            oldString = ui_eventName.getText().toString();
        }
        ui_eventDate.setText(calendarDate);
        ui_eventDate.setInputType(InputType.TYPE_NULL);

        if (modify){
            ui_deleteButton.setVisibility(View.VISIBLE);
        }else{
            ui_deleteButton.setVisibility(View.INVISIBLE);
        }



        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //Custom handlers for time and date pickers for their EditText fields, much better UX than number pad, but not 100% necessary
        ui_eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                //Catch the given date and set it in EditText field, also refactor the string so that mysql database can handle it
                datePickerDialog = new DatePickerDialog(CalendarAddActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String dateString = "";
                                if(monthOfYear < 9){
                                     dateString = Integer.toString(year) + "-0" + Integer.toString(monthOfYear+1) + "-" + Integer.toString(dayOfMonth);
                                }else{
                                     dateString = Integer.toString(year) + "-" + Integer.toString(monthOfYear+1) + "-" + Integer.toString(dayOfMonth);
                                }
                                ui_eventDate.setText(dateString);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        //time picker
        ui_eventTime = (EditText)findViewById(R.id.etEventTime);
        ui_eventTime.setInputType(InputType.TYPE_NULL);
        ui_eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr2 = Calendar.getInstance();
                int hour = cldr2.get(Calendar.HOUR_OF_DAY);
                int minute = cldr2.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(CalendarAddActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                if( selectedMinute < 9){
                                    ui_eventTime.setText(selectedHour + ":0" + selectedMinute + ":00");
                                }
                                ui_eventTime.setText(selectedHour + ":" + selectedMinute + ":00");
                            }
                        },hour,minute,true);
                timePickerDialog.setTitle("Select time");
                timePickerDialog.show();
            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //Get current user from shared preferences, use this user to add row for calendar event in database
        SharedPreferences sp2 = getSharedPreferences("Credentials",MODE_PRIVATE);
        userString = sp2.getString("username",null);

        //send event data to database
        ui_saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlString,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(CalendarAddActivity.this,response,Toast.LENGTH_LONG).show();

                                if(response.matches("Event saved")){
                                    Toast.makeText(CalendarAddActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(CalendarAddActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(CalendarAddActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                                Log.i(TAG, "onErrorResponse: FROM CALENDARADDACTIVITY -> " +error.toString());
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("fl_name",userString);
                        params.put("event",ui_eventName.getText().toString());
                        params.put("oldevent", oldString);
                        params.put("elocation",ui_eventLocation.getText().toString());
                        params.put("edescription",ui_eventDescription.getText().toString());
                        params.put("edate",ui_eventDate.getText().toString());
                        params.put("etime",ui_eventTime.getText().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(CalendarAddActivity.this);
                requestQueue.add(stringRequest);
                //start new calendar activity so we have updated lists and close this add activity
                startActivity(new Intent(CalendarAddActivity.this, CalendarActivity.class));
                finish();
            }
        });



        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ui_deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlCalendarEventDelete,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(CalendarAddActivity.this,response,Toast.LENGTH_LONG).show();

                                if(response.matches("Event Deleted")){
                                    Toast.makeText(CalendarAddActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(CalendarAddActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(CalendarAddActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                                Log.i(TAG, "onErrorResponse: FROM CALENDARADDACTIVITY -> " +error.toString());
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("fl_name",userString);
                        params.put("oldevent",oldString);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(CalendarAddActivity.this);
                requestQueue.add(stringRequest);
                //start new calendar activity so we have updated lists and close this add activity
                startActivity(new Intent(CalendarAddActivity.this, CalendarActivity.class));
                finish();
            }
        });
    }
}
