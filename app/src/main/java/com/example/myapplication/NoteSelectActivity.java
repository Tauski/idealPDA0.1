package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteSelectActivity extends AppCompatActivity {

    //TAG for error logs
    private static final String TAG = "FROM NOTE SELECT ACTIVITY";

    //Initializing UI and variables
    private ArrayList<String> title;
    private RecyclerView rvNotes;
    private LinearLayoutManager lManager;
    private String userString;
    private NotesAdapter adapter;

    //Custom url string for php script that retrieves all given users notes
    private final String urlAllNotes = "http://192.168.1.103:8012/project/userNotesGet.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_select);
        //populate array list
        title = new ArrayList<String>();
        //initialize all UI elements and retrieve notes from db
        rvNotes = (RecyclerView)findViewById(R.id.rvNotes);
        rvNotes.setLayoutManager(lManager = new LinearLayoutManager(NoteSelectActivity.this));
        DividerItemDecoration decoration = new DividerItemDecoration(rvNotes.getContext(),lManager.getOrientation());
        rvNotes.addItemDecoration(decoration);
        adapter = new NotesAdapter(this,title);
        rvNotes.setAdapter(adapter);

        //using credentials from shared preferences
        SharedPreferences sp2 = getSharedPreferences("Credentials",MODE_PRIVATE);
        userString = sp2.getString("username",null);
    }

    //get from server side
    private void prepareNotes() {

        //Request handled by volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlAllNotes,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] notes = response.split(",");
                        for(int i = 0; i<notes.length;i++){
                            title.add(notes[i]);
                        }
                        Toast.makeText(NoteSelectActivity.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NoteSelectActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: FROM LOGIN -> " +error.toString());

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("f_name", userString);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(NoteSelectActivity.this);
        requestQueue.add(stringRequest);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //opening selected note for modifications
    public String Open(String fileName) {
        String content = "";
        try {
            InputStream in = openFileInput(fileName);
            if (in != null) {
                InputStreamReader tmp = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(tmp);
                String str;
                StringBuilder buf = new StringBuilder();
                while ((str = reader.readLine()) != null) {
                    buf.append(str + "\n");
                }
                in.close();

                content = buf.toString();
            }
        } catch (java.io.FileNotFoundException e) {
        } catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
        return content;
    }
}
