package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//Calendar activity holds UI interactions with calendar and has adding events button
public class CalendarActivity extends AppCompatActivity implements EventsAdapter.OnEventPressListener {

    private static final String TAG = "FROM CALENDAR ACTIVITY";

    //Initializing UI elements and variables
    private ArrayList<String> eventList;
    private CalendarView ui_calendar;
    private FloatingActionButton ui_addNewEvent;
    private RecyclerView ui_events;
    private LinearLayoutManager lManager;
    private EventsAdapter eventsAdapter;
    private String userString = "";
    private String date;

    //Custom url string for php script that retrieves all users events for day
    private final String urlEvents = "http://192.168.1.103:8012/project/userEventsGet.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //using credentials from shared preferences
        SharedPreferences sp2 = getSharedPreferences("Credentials",MODE_PRIVATE);
        userString = sp2.getString("username",null);

        //Init UI and variables
        eventList = new ArrayList<String>();
        ui_calendar = (CalendarView)findViewById(R.id.cvMain);
        ui_addNewEvent = (FloatingActionButton)findViewById(R.id.fabAddEvent) ;

        ui_events = (RecyclerView)findViewById(R.id.rvEvents);
        ui_events.setLayoutManager(lManager = new LinearLayoutManager(CalendarActivity.this));
        DividerItemDecoration decoration = new DividerItemDecoration(ui_events.getContext(),lManager.getOrientation());
        ui_events.addItemDecoration(decoration);
        eventsAdapter = new EventsAdapter(this, eventList,this);
        ui_events.setAdapter(eventsAdapter);

        //keeps hold of date changes on calendar, also for some reason calendarview months start at 0 so add 1 to get correct month.
        ui_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

                String curYear = String.valueOf(year);
                String curMonth = String.valueOf(month+1);
                String curDay = String.valueOf(day);
                date = curYear + "-" + curMonth + "-" + curDay;
                Toast.makeText(getApplicationContext(),date,Toast.LENGTH_SHORT).show();
                retrieveEvents();
            }
        });
        //Opens a new event page
        ui_addNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CalendarActivity.this,CalendarAddActivity.class);
                intent.putExtra("EXTRA_CALENDAR_DATE",date);
                startActivity(intent);
            }
        });
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void retrieveEvents(){
        StringRequest request = new StringRequest(Request.Method.POST, urlEvents,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        eventList.clear();
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if(sucess.equals("1")){

                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String note = object.getString("event");
                                    eventList.add(note);
                                    eventsAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                        catch (JSONException e){

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(CalendarActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("f_name",userString);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onEventPressClick(int position) {
        Toast.makeText(this,"click notified", Toast.LENGTH_SHORT).show();
    }
}