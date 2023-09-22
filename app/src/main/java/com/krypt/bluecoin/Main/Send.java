package com.krypt.bluecoin.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.krypt.bluecoin.R;
import com.krypt.bluecoin.utils.EnterAmount;
import com.krypt.bluecoin.utils.PaymentMethod_;

public class Send extends Fragment {
    CardView mpesa,card;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.send_layout,container,false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mpesa=view.findViewById(R.id.mpesa_with);
        card=view.findViewById(R.id.card_with);
        //getView().getSupportActionBar().setTitle("Send");
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
