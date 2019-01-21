package com.example.joel.navdrawers;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class SignInActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private Button submitButton;
    private RequestQueue serverRequest;
    private AlertDialog progressDialog;
    private TextView SignUp;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    UserSessionManager session;


    @SuppressLint("CommitPrefEdits")
    public void saveSession(String token, String userId){
        SharedPreferences activeSession = this.getSharedPreferences("com.loveeconomy.loveeconomy", Context.MODE_PRIVATE);
        activeSession.edit().putString("auth_token", token).apply();
        activeSession.edit().putString("user_id", userId).apply();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        SharedPreferences prefs = getSharedPreferences("com.loveeconomy",MODE_PRIVATE);
        //SharedPreferences.Editor editor = prefs.edit();

        session = new UserSessionManager(getApplicationContext());



       // editor.putBoolean("logged",false);

        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        submitButton = findViewById(R.id.signInbtn);


//        boolean firstStart = prefs.getBoolean("logged",false);
//        final String mail = emailField.getText().toString().trim();
//        final String pass = passwordField.getText().toString();

//        if (!firstStart){
//
//            Toast.makeText(this, "sign in", Toast.LENGTH_SHORT).show();
//            //startActivity(new Intent(this,SignUpActivity.class));
//        }else  {
//
//            RedirectToDashboard();
//        }


        serverRequest = Volley.newRequestQueue(this);

        SignUp = findViewById(R.id.NewAccounts);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIntent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(signIntent);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitButton.setEnabled(false);

                final String mail = emailField.getText().toString().trim();
                final String pass = passwordField.getText().toString();

                if(mail.trim().length() > 0 && pass.trim().length() > 0) {
                    progressDialog = new SpotsDialog(SignInActivity.this, R.style.Custom);
                    progressDialog.show();
                    try {
                        String url = "http://loveapp-le.herokuapp.com/v1/users/login";
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("email", mail);
                        jsonBody.put("password", pass);

                        JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    if (!response.getString("token").isEmpty()) {
                                        String token = response.getString("token");
                                        JSONObject userObject = response.getJSONObject("user");
                                        String userId = userObject.getString("id");
                                        progressDialog.dismiss();

                                        session.createUserLoginSession(mail, userId);
                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        // Add new Flag to start new Activity
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);

                                        finish();


                                        saveSession(token, userId);
                                    }else{

                                        // username / password doesn't match
                                        Toast.makeText(getApplicationContext(), "Error response no token", Toast.LENGTH_LONG).show();

                                    }


                                    // RedirectToDashboard();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                progressDialog.dismiss();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("login_error", error.toString());
                            }
                        }) {
//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            final Map<String, String> headers = new HashMap<>();
//                            headers.put("Authorization", "Bearer " + "c2FnYXJAa2FydHBheS5jb206cnMwM2UxQUp5RnQzNkQ5NDBxbjNmUDgzNVE3STAyNzI=");//put your token here
//                            return headers;
//                        }
                        };
                        serverRequest.add(jsonOblect);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{

                    // user didn't entered username or password
                    Toast.makeText(getApplicationContext(), "Please enter username and password", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
