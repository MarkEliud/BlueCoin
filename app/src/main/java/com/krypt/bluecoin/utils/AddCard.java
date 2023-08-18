package com.krypt.bluecoin.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.krypt.bluecoin.R;
import com.krypt.bluecoin.User.UserModel;

public class AddCard extends AppCompatActivity {
    Button add;
    EditText billno,cvc,accountno,expno;
    private SessionHandler session;
    UserModel user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        add=findViewById(R.id.addcard);
        billno=findViewById(R.id.billadd);
        cvc=findViewById(R.id.cvcno);
        accountno=findViewById(R.id.cardno);
        expno=findViewById(R.id.exdate);
        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardAdd(billno.getText().toString(),cvc.getText().toString(),accountno.getText().toString(),expno.getText().toString());
            }
        });
    }

    private void cardAdd(String bill,String acc,String cvc,String expno) {
        if (bill.isEmpty()||acc.isEmpty()||cvc.isEmpty()||expno.isEmpty()){
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Was Succesful", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            session = new SessionHandler(getApplicationContext());
            user = session.getUserDetails();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}