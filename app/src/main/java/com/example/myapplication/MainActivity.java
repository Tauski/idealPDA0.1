package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


///This activity is projects main activity
///Mainly user for login action, also holds register button, which sends user to register activity
///Simple UI with 2 TextEdits, 1 Button, 1 TextView.

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FROM MAIN ACTIVITY";
    //url should be http not https while doing local stuff
    String url2 = "http://192.168.1.103:8012/project/UserLogin.php";

    //ui elements
    private EditText ui_name;
    private EditText ui_password;
    private Button ui_login;
    private TextView ui_register;

    private SignInButton ui_signInButton;

    //Strings to hold name and pw
    String s_name;
    String s_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //google activity sign in stuff
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        final GoogleSignInClient m_googleSignInClient = GoogleSignIn.getClient(this,gso);


        //implementing ui elements
        ui_name = (EditText)findViewById(R.id.etName);
        ui_password = (EditText)findViewById(R.id.etPassword);
        ui_login = (Button)findViewById(R.id.btnLogin);
        ui_register = (TextView)findViewById(R.id.tvRegister);
        ui_signInButton = (SignInButton)findViewById(R.id.sign_in_button);

        //if client presses register textview, send into new activity
        ui_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //if client wants to login using google account
        ui_signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }

            private void signIn() {
                Intent signInIntent = m_googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 1);
            }
        });


        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Non google login
        ui_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get inputs
                s_name = ui_name.getText().toString();
                s_password = ui_password.getText().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url2,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.matches("Data Matched")){

                                    //using sharedPreferences to hold on to user credentials for further database usage
                                    SharedPreferences sp = getSharedPreferences("Credentials",MODE_PRIVATE);
                                    SharedPreferences.Editor ed = sp.edit();
                                    ed.putString("username",s_name);
                                    ed.putString("password",s_password);
                                    ed.commit();

                                    startActivity(new Intent(MainActivity.this,FrontActivity.class));
                                }
                                else{
                                    Toast.makeText(MainActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                                Log.i(TAG, "onErrorResponse: FROM LOGIN -> " +error.toString());
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("f_name", s_name);
                        params.put("password", s_password);

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(stringRequest);
        }
        });

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == 1){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(MainActivity.this, account.getEmail(),Toast.LENGTH_LONG).show();
            }catch (ApiException e){
                Log.w(TAG, "Google sign in failed",e);
            }
            //startActivity(new Intent(MainActivity.this,FrontActivity.class));
        }
    }

    //handle account details. Not used at the moment

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        //GoogleSignInAccount account = completedTask.getResult(ApiException.class);


    }
}