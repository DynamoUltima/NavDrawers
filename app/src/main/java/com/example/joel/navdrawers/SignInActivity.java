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

    @SuppressLint("CommitPrefEdits")
    public void saveSession(String token, String userId){
        SharedPreferences activeSession = this.getSharedPreferences("com.loveeconomy.loveeconomy", Context.MODE_PRIVATE);
        activeSession.edit().putString("auth_token", token).apply();
        activeSession.edit().putString("user_id", userId).apply();
    }

    public void RedirectToDashboard(){
        Intent dashboard = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(dashboard);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        submitButton = findViewById(R.id.signInbtn);
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


                progressDialog = new SpotsDialog(SignInActivity.this, R.style.Custom);
                progressDialog.show();
                try {
                    String url = "http://loveapp-le.herokuapp.com/v1/users/login";
                    JSONObject jsonBody =  new JSONObject();
                    jsonBody.put("email", mail);
                    jsonBody.put("password", pass);

                    JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String token = response.getString("token");
                                JSONObject userObject = response.getJSONObject("user");
                                String userId = userObject.getString("id");

                                saveSession(token, userId);
                                RedirectToDashboard();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("login_error",error.toString());
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

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
