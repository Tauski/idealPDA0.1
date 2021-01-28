package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Credentials;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

///This activity handles registering new user with credentials as name, email, password.
///Simple UI, 3 TextEdits for credentials and button for registering. Getting out of this activity is handled by platform side buttons or registering
///Requires network permissions(androidManifest), calls php commands through network. Uses volley API for sending and getting requests
///For now only local network, set url for your local host and remember precise folder order. (If your port is not apache default: 80, also include it in url)
public class RegistrationActivity extends AppCompatActivity {

    //Tag for error logs
    private static final String TAG = "FROM REGISTRATION ACTIVITY";
    //url should be http not https while doing local stuff
    String url = "http://192.168.1.103:8012/project/insert.php";

    //define ui elements
    private EditText eRegName;
    private EditText eRegPassword;
    private EditText eRegEmail;
    private Button eRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //connecting ui elements
        eRegName = findViewById(R.id.etRegName);
        eRegPassword = findViewById(R.id.etRegPassword);
        eRegEmail = findViewById(R.id.etRegEmail);
        eRegister = findViewById(R.id.btnRegister);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*Use this side when google signing is implemented

                //alert dialogs to give credentials with custom layout
                final AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                LayoutInflater inflater = RegistrationActivity.this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog_signing, null));
                        // Add action buttons
                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(RegistrationActivity.this, "You account is now connected", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(RegistrationActivity.this, "You canceled the option, you can still connect the google account in settings", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            }
                        });
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                final AlertDialog alert = builder.create();

                final AlertDialog.Builder builder2 = new AlertDialog.Builder(RegistrationActivity.this);
                builder2.setMessage("Do you want to combine Google account with your user?");
                        builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                        builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            }
                        });
                builder2.setIcon(android.R.drawable.ic_dialog_alert);
                final AlertDialog alert2 = builder2.create();
        */

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //register button listener
        eRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get inputs
                final String registeredName = eRegName.getText().toString();
                final String registeredEmail = eRegEmail.getText().toString();
                final String registeredPassword = eRegPassword.getText().toString();

                //first validate from client side,
                if (validate(registeredName,registeredEmail,registeredPassword)) {

                    //then send to database
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.matches("user created successfully. You may now login using your credentials")){
                                        startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(RegistrationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                    Log.i(TAG, "onErrorResponse: ERROR FROM INSERT REGISTRATION -> " + error.toString());
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("f_name",registeredName);
                            params.put("email", registeredEmail);
                            params.put("password",registeredPassword);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
                    requestQueue.add(stringRequest);
                }
            }
        });

        finish();
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Email validation check (catches most but not all corner cases) https://stackoverflow.com/questions/153716/verify-email-in-java
    private static final Pattern rfc2822 = Pattern.compile(
            "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
    );
    //Function for validating the input credentials from the user
    boolean validate(String name, String email, String password)
    {
        //Check if the name is empty and password field is 8 characters long, also including email check
        if(name.isEmpty() || !rfc2822.matcher(email).matches() || (password.length() < 8))
        {
            Toast.makeText(this, "Please enter your name and email. Ensure password is more than 8 characters long!", Toast.LENGTH_SHORT).show();

            //reset TextEdits
            eRegName.setText(null);
            eRegEmail.setText(null);
            eRegPassword.setText(null);

            return false;
        }
        return true;
    }
}