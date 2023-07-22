package com.krypt.bluecoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krypt.bluecoin.User.UserModel;

public class LoginActivity extends AppCompatActivity {
    TextView toreg,forgotpas;
    EditText username,password;
    Button loginbtn;
    ProgressBar progressBar;
    FirebaseDatabase firebaseDatabase;
    UserModel userModel;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        username=findViewById(R.id.edt_username);
        password=findViewById(R.id.edt_password);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        forgotpas=findViewById(R.id.txt_link_forgotpass);
        toreg=findViewById(R.id.txt_link_reg);
        loginbtn=findViewById(R.id.btn_login);
        firebaseDatabase = FirebaseDatabase.getInstance();
        userModel=new UserModel();
        databaseReference = firebaseDatabase.getReference("Clients");
        forgotpas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Still in development",Toast.LENGTH_LONG).show();


            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            login(username.getText().toString().trim(),password.getText().toString().trim());

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
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel post = dataSnapshot.getValue(UserModel.class);
                System.out.println(post);
                if(post.getUsername()==usernm && post.getPass()==pass){
                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }else Toast.makeText(LoginActivity.this, "Incorrect credientials", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Something went wrong.Try again!", Toast.LENGTH_SHORT).show();
            }
        });



    }


}