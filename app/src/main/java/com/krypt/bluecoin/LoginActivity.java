package com.krypt.bluecoin;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krypt.bluecoin.User.UserModel;

public class LoginActivity extends AppCompatActivity {
    TextView toreg,forgotpas;
    private ProgressDialog loadingBar;
    EditText username,password;
    String parentDbName="Clients";
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
        loadingBar=new ProgressDialog(this);

        forgotpas=findViewById(R.id.txt_link_forgotpass);
        toreg=findViewById(R.id.txt_link_reg);
        loginbtn=findViewById(R.id.btn_login);
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference();
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

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(parentDbName).child(usernm).exists())
                {
                    UserModel usersData ;
                    usersData = dataSnapshot.child(parentDbName).child(usernm).getValue(UserModel.class);


                    if (usersData.getPassword().equals(pass))
                    {

                        Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                              //  Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);

                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, " incorrect details.", Toast.LENGTH_SHORT).show();
                        }
                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


}