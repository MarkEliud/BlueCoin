package com.krypt.bluecoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krypt.bluecoin.User.UserModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
Button registerBtn;
 FirebaseDatabase firebaseDatabase;
 UserModel userModel;
 DatabaseReference databaseReference;
TextInputEditText edt_username,edt_phoneNo,edt_email,edt_password,edt_password_c;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerBtn=findViewById(R.id.register_btn);
        edt_username=findViewById(R.id.usernm);
        edt_phoneNo=findViewById(R.id.phoneno);
        edt_email=findViewById(R.id.email);
        edt_password=findViewById(R.id.pass);
        edt_password_c=findViewById(R.id.cpass);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        firebaseDatabase = FirebaseDatabase.getInstance();
        userModel=new UserModel();
   databaseReference = firebaseDatabase.getReference("Clients");
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();

            }
        });
    }

    public void register() {
        registerBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);


        final String username = edt_username.getText().toString().trim();
        final String phoneNo = edt_phoneNo.getText().toString().trim();
        final String email = edt_email.getText().toString().trim();
        final String password = edt_password.getText().toString().trim();
        final String password_c = edt_password_c.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Enter username", Toast.LENGTH_SHORT).show();
            registerBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;

        }
        if (TextUtils.isEmpty(phoneNo)) {
            Toast.makeText(getApplicationContext(), "Enter phone number", Toast.LENGTH_SHORT).show();
            registerBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;

        }
        if (phoneNo.length() > 10 || phoneNo.length() < 10) {
            Toast.makeText(getApplicationContext(), "Phone number should contain 10 digits", Toast.LENGTH_SHORT).show();
            registerBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address", Toast.LENGTH_SHORT).show();
            registerBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;

        }
        if (!email.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(), "In valid email address", Toast.LENGTH_SHORT).show();
            registerBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Password is required", Toast.LENGTH_SHORT).show();
            registerBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (!password.equals(password_c)) {
            Toast.makeText(getApplicationContext(), "Password mismatch", Toast.LENGTH_SHORT).show();
            registerBtn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }
        addDatatoFirebase(username,phoneNo,email,password);
    }
        private void addDatatoFirebase(String username, String phone,String email, String password) {
            userModel.setUsername(username);
            userModel.setEmail(email);
            userModel.setPhone(phone);
            userModel.setPass(password);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    databaseReference.child(username).setValue(userModel);


                    Toast.makeText(RegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(RegisterActivity.this, "Fail to Register.Try again! " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }

}