package com.krypt.bluecoin.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.krypt.bluecoin.R;
import com.krypt.bluecoin.utils.AddCard;

public class Home extends Fragment {
    TextView add;
    Button card_;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.home_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.recView);
        card_=view.findViewById(R.id.btn_card);


        card_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCard();
            }
        });
    }

    private void toCard() {
        startActivity(new Intent(getContext(), CardActivity.class));
    }
}
