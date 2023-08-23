package com.krypt.bluecoin.Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.krypt.bluecoin.R;
import com.krypt.bluecoin.User.UserModel;
import com.krypt.bluecoin.utils.AddCard;
import com.krypt.bluecoin.utils.SessionHandler;

public class Home extends Fragment {
    TextView add,wlecometxt;
    private SessionHandler session;
    private UserModel user;
//    Button card_,Deposit,send;
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
//        card_=view.findViewById(R.id.btn_card);
//        Deposit=view.findViewById(R.id.btn_depo);

        wlecometxt=view.findViewById(R.id.welcome_txt);
        try {
            session = new SessionHandler(getActivity());
            user = session.getUserDetails();


            wlecometxt.setText("Welcome  "+user.getFname());




        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }


//        card_.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toCard();
//            }
//        });
//        Deposit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "Deposit here", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void toCard() {
        startActivity(new Intent(getContext(), CardActivity.class));
    }
}
