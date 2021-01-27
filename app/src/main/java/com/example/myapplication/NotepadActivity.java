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

public class NotepadActivity extends AppCompatActivity {

    //TAG for error logs
    private static final String TAG = "FROM NOTEPAD ACTIVITY";

    //Initializing UI and variables
    private EditText ui_text;
    private Button ui_button;
    private Button ui_openButton;
    private String saveString;
    private String userString;

    //URL string for saving notes to database
    String urlNotepad = "http://192.168.1.103:8012/project/userNotepad.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        //connect variables
        ui_text = (EditText) findViewById(R.id.etWrite);
        ui_button = (Button)findViewById(R.id.bSave);
        ui_openButton = (Button)findViewById(R.id.bOpen);

        //get user credentials from shared preferences, to use username in database
        SharedPreferences sp1 = getSharedPreferences("Credentials",MODE_PRIVATE);
        userString = sp1.getString("username",null);


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ui_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveString = ui_text.getText().toString();
                //Handling request with volley
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlNotepad,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.matches("Note saved")){
                                    Toast.makeText(NotepadActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(NotepadActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(NotepadActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                                Log.i(TAG, "onErrorResponse: FROM NOTEPADACTIVITY -> " +error.toString());
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("fl_name",userString);
                        params.put("savednote",saveString);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(NotepadActivity.this);
                requestQueue.add(stringRequest);
            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //list all notes from this user
        ui_openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(NotepadActivity.this,NoteSelectActivity.class));
            }
        });
    }
}