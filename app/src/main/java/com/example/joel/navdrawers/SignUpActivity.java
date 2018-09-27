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

public class SignUpActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private EditText firstname;
    private EditText lastName;
    private Button signUpBtn;

    private RequestQueue serverRequest;



    private AlertDialog progressDialog;

    @SuppressLint("CommitPrefEdits")
    public void saveSession(String token, String userId){
        SharedPreferences activeSession = this.getSharedPreferences("com.loveeconomy.loveeconomy", Context.MODE_PRIVATE);
        activeSession.edit().putString("auth_token", token).apply();
        activeSession.edit().putString("user_id", userId).apply();
    }

    public void RedirectToDashboard(){
        Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(dashboard);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailField =findViewById(R.id.email);
        passwordField= findViewById(R.id.pwd);
        firstname = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        signUpBtn = findViewById(R.id.signUpbtn);

        serverRequest = Volley.newRequestQueue(this);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new SpotsDialog(SignUpActivity.this, R.style.Custom);
                progressDialog.show();


                final String mail = emailField.getText().toString().trim();
                final String pass = passwordField.getText().toString();
                final String first = firstname.getText().toString();
                final String last = lastName.getText().toString();

                try {
                    String url = "http://loveapp-le.herokuapp.com/v1/users/new";
                    JSONObject jsonBody =  new JSONObject();
                    jsonBody.put("email", mail);
                    jsonBody.put("password", pass);
                    jsonBody.put("first", first);
                    jsonBody.put("last", last);

                    JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String token = response.getString("token");
                                JSONObject userObject = response.getJSONObject("user");
                                String userId = userObject.getString("id");

                                Intent signIntent = new Intent(SignUpActivity.this,MainActivity.class);
                                startActivity(signIntent);
                                Toast.makeText(SignUpActivity.this, userId, Toast.LENGTH_SHORT).show();
                                Toast.makeText(SignUpActivity.this, token, Toast.LENGTH_SHORT).show();
                                Toast.makeText(SignUpActivity.this, "Success", Toast.LENGTH_SHORT).show();

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
