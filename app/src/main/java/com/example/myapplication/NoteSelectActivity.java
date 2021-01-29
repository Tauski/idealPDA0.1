package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.AdapterView;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteSelectActivity extends AppCompatActivity implements NotesAdapter.OnNoteListener{

    //TAG for error logs
    private static final String TAG = "FROM NOTE SELECT ACTIVITY";

    //Initializing UI and variables
    private ArrayList<String> title;
    private ArrayList<NotesBuilder> notes;
    private RecyclerView rvNotes;
    private LinearLayoutManager lManager;
    private String userString = "";
    private NotesAdapter adapter;

    //Custom url string for php script that retrieves all given users notes
    private final String urlAllNotes = "http://192.168.1.103:8012/project/userNotesGet.php";


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_select);
        //using credentials from shared preferences
        SharedPreferences sp2 = getSharedPreferences("Credentials",MODE_PRIVATE);
        userString = sp2.getString("username",null);

        //Init UI and variables
        title = new ArrayList<String>();
        rvNotes = (RecyclerView)findViewById(R.id.rvNotes);
        rvNotes.setLayoutManager(lManager = new LinearLayoutManager(NoteSelectActivity.this));
        DividerItemDecoration decoration = new DividerItemDecoration(rvNotes.getContext(),lManager.getOrientation());
        rvNotes.addItemDecoration(decoration);
        adapter = new NotesAdapter(this, title, this);
        rvNotes.setAdapter(adapter);

        retrieveData();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void retrieveData(){
        StringRequest request = new StringRequest(Request.Method.POST, urlAllNotes,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        title.clear();
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if(sucess.equals("1")){
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String note = object.getString("note");
                                    title.add(note);
                                    adapter.notifyDataSetChanged();
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
                Toast.makeText(NoteSelectActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void onNoteClick(int position) {

        Intent intent = new Intent(this, NotepadActivity.class);
        intent.putExtra("EXTRA_NOTE",title.get(position));
        startActivity(intent);
        finish();
    }
}
