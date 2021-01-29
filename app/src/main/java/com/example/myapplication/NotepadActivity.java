package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class NotepadActivity extends AppCompatActivity {

    //TAG for error logs
    private static final String TAG = "FROM NOTEPAD ACTIVITY";

    //Initializing UI and variables
    private EditText ui_text;
    private Button ui_button;
    private Button ui_openButton;
    private Button ui_deleteButton;
    private String saveString;
    private String userString;
    private String oldString;

    //URL string for saving,updating,deleting notes to database
    String urlInsert = "http://192.168.1.103:8012/project/userNotesInsert.php";
    String urlUpdate = "http://192.168.1.103:8012/project/userNotesUpdate.php";
    String urlDelete = "http://192.168.1.103:8012/project/userNotesDelete.php";


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        //UI elements
        ui_button = (Button)findViewById(R.id.bSave);
        ui_openButton = (Button)findViewById(R.id.bOpen);
        ui_deleteButton = (Button)findViewById(R.id.bDelete);

        //get user credentials from shared preferences, to use username in database queries
        SharedPreferences sp1 = getSharedPreferences("Credentials",MODE_PRIVATE);
        userString = sp1.getString("username",null);

        //Check if activity started with intent extra (opened existing note),
        //if yes set url as update type, and if no set oldString as something
        ui_text = (EditText) findViewById(R.id.etWrite);
        oldString = getIntent().getStringExtra("EXTRA_NOTE");
        if(oldString == null){

            oldString = "filling";
        }
        else
        {
            ui_openButton.setVisibility(View.INVISIBLE);
            ui_deleteButton.setVisibility(View.VISIBLE);
            urlInsert = urlUpdate;
            ui_text.setText(oldString);
        }




        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ui_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveString = ui_text.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlInsert,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(NotepadActivity.this,response,Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(NotepadActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("fl_name",userString);
                        params.put("incnote",saveString);
                        params.put("oldnote",oldString);

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(NotepadActivity.this);
                requestQueue.add(stringRequest);

                //open note list after save button press
                startActivity( new Intent(NotepadActivity.this,NoteSelectActivity.class));
                finish();
            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //list all notes from this user in new activity
        ui_openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(NotepadActivity.this,NoteSelectActivity.class));
                finish();
            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ui_deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlDelete,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(NotepadActivity.this,response,Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(NotepadActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("fl_name",userString);
                        params.put("oldnote",oldString);

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(NotepadActivity.this);
                requestQueue.add(stringRequest);

                //open note list after Delete button press
                startActivity( new Intent(NotepadActivity.this,NoteSelectActivity.class));
                finish();
            }
        });
    }
}