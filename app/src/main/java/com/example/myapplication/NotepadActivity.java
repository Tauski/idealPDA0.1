package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class NotepadActivity extends AppCompatActivity {

    //Define audioRequestCode
    public static final Integer RecordAudioRequestCode = 1;

    //TAG for error logs
    private static final String TAG = "FROM NOTEPAD ACTIVITY";

    //Initializing UI and variables
    private SpeechRecognizer speechRecognizer;
    private ImageView ui_micButton;
    private EditText ui_text;
    private Button ui_button;
    private Button ui_openButton;
    private Button ui_deleteButton;
    private String saveString;
    private String userString;
    private String oldString;

    //URL string for saving,updating,deleting notes to database
    String urlInsert = "http://192.168.1.103:8012/project/Notepad/userNotesInsert.php";
    String urlUpdate = "http://192.168.1.103:8012/project/Notepad/userNotesUpdate.php";
    String urlDelete = "http://192.168.1.103:8012/project/Notepad/userNotesDelete.php";


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        //get user credentials from shared preferences, to use username in database queries
        SharedPreferences sp1 = getSharedPreferences("Credentials",MODE_PRIVATE);
        userString = sp1.getString("username",null);

        //UI elements
        ui_button = (Button)findViewById(R.id.bSave);
        ui_openButton = (Button)findViewById(R.id.bOpen);
        ui_deleteButton = (Button)findViewById(R.id.bDelete);
        ui_micButton = (ImageView)findViewById(R.id.micButton);
        ui_text = (EditText) findViewById(R.id.etWrite);


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //STT init (recognize speech and insert into ui_text)
        final Intent sttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                ui_text.setText("");
                ui_text.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                ui_text.setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //Check if activity started with intent extra (opened existing note),
        //if yes set url as update type, and if no set oldString as something as volley cant handle null parameters
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


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ui_micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    speechRecognizer.startListening(sttIntent);
                }
                return false;
            }
        });
    }

}