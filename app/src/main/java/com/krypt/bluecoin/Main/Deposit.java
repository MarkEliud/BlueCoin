package com.krypt.bluecoin.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.krypt.bluecoin.R;
import com.krypt.bluecoin.utils.EnterAmount;
import com.krypt.bluecoin.utils.PaymentMethod_;

public class Deposit  extends Fragment {
 CardView mpesa,card;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.deposit_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mpesa=view.findViewById(R.id.mpesa_depo);
        card=view.findViewById(R.id.card_depo);
        //getView().getSupportActionBar().setTitle("Deposit");
        mpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EnterAmount.class));
            }
        });
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EnterAmount.class));
            }
        });


    }
}
