package com.krypt.bluecoin.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;

import com.krypt.bluecoin.R;

public class PaymentMethod_ extends AppCompatActivity {
    CardView card,mpesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        card=findViewById(R.id.card_card);
        mpesa=findViewById(R.id.card_mpesa);
        getSupportActionBar().setTitle("Payment Methods");

    }
}