package com.krypt.bluecoin.Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.krypt.bluecoin.LoginActivity;
import com.krypt.bluecoin.R;
import com.krypt.bluecoin.User.UserModel;
import com.krypt.bluecoin.utils.SessionHandler;

public class Profile extends Fragment {
    private SessionHandler session;
    private UserModel user;
    ImageView profilepic;
    TextView usnm,logout,verfyacc_;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profilepic=view.findViewById(R.id.profileimg_id);
        usnm=view.findViewById(R.id.username_id);
        logout=view.findViewById(R.id.id_logout);
        verfyacc_=view.findViewById(R.id.verify_accoutid);

        try {
            session = new SessionHandler(getActivity());
            user = session.getUserDetails();


            usnm.setText(user.getFname() + " " + user.getSname());

        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }
}
