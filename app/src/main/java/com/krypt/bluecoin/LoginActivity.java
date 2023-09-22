package com.krypt.bluecoin;

import static com.krypt.bluecoin.utils.Links.URL_LOGIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krypt.bluecoin.User.UserModel;
import com.krypt.bluecoin.utils.SessionHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    TextView toreg,forgotpas;
    private ProgressDialog loadingBar;
    EditText username,password;
    String parentDbName="Clients";
    private SessionHandler session;
    Button loginbtn;
    ProgressBar progressBar;
    //FirebaseDatabase firebaseDatabase;
    UserModel userModel;
    //DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        username=findViewById(R.id.edt_username);
        password=findViewById(R.id.edt_password);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        loadingBar=new ProgressDialog(this);

        forgotpas=findViewById(R.id.txt_link_forgotpass);
        toreg=findViewById(R.id.txt_link_reg);
        loginbtn=findViewById(R.id.btn_login);
        session = new SessionHandler(LoginActivity.this);
        userModel = session.getUserDetails();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//
//        databaseReference = firebaseDatabase.getReference();
        forgotpas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Still in development",Toast.LENGTH_LONG).show();


            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            login(username.getText().toString(),password.getText().toString());

            }
        });
        toreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

            }
        });
    }

    private void login(String usernm,String pass) {
        progressBar.setVisibility(View.VISIBLE);
        loginbtn.setVisibility(View.GONE);

        if (TextUtils.isEmpty(usernm)) {
            Toast.makeText(getApplicationContext(), "Enter username", Toast.LENGTH_SHORT).show();
            loginbtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;

        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
            loginbtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;

        }




        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("Response", "" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String clientID = jsn.getString("clientID");
                                    String firstname = jsn.getString("firstname");
                                    String lastname = jsn.getString("lastname");
                                    String username = jsn.getString("username");
                                    String phoneNo = jsn.getString("phoneNo");
                                    String email = jsn.getString("email");
                                    String dateCreated = jsn.getString("dateCreated");
                                    String statususr = jsn.getString("status");

                                    session.loginUser(clientID, firstname, lastname, username, phoneNo, email, dateCreated,statususr);
                                }


                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();

                                progressBar.setVisibility(View.GONE);
                                loginbtn.setVisibility(View.VISIBLE);
                                LoginActivity.this.onBackPressed();
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();


                            } else if (status.equals("0")) {
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                loginbtn.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            loginbtn.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
              //  Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                loginbtn.setVisibility(View.VISIBLE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_name", usernm);
                params.put("pass_word", pass);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);

    }


}