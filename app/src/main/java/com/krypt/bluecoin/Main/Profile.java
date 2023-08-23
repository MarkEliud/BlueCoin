package com.krypt.bluecoin.Main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.krypt.bluecoin.LoginActivity;
import com.krypt.bluecoin.MainActivity;
import com.krypt.bluecoin.R;
import com.krypt.bluecoin.User.UserModel;
import com.krypt.bluecoin.User.VeifyAcc;
import com.krypt.bluecoin.utils.SessionHandler;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends Fragment {
    private SessionHandler session;
    private UserModel user;
    ImageView profilepic;
    CircleImageView circleImageView;
    TextView usnm,logout,verfyacc_,txt_status;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.account_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
     //   profilepic=view.findViewById(R.id.profileimg_id);
        circleImageView=view.findViewById(R.id.account_id);
        usnm=view.findViewById(R.id.username_id);
        logout=view.findViewById(R.id.id_logout);
        verfyacc_=view.findViewById(R.id.verify_accoutid);
        txt_status=view.findViewById(R.id.txt_status);


        try {
            session = new SessionHandler(getActivity());
            user = session.getUserDetails();


            usnm.setText(user.getFname() + " " + user.getSname());
            txt_status.setText(user.getStatus());



        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        verfyacc_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), VeifyAcc.class));

            }
        });
    }
    private void logout() {
        AlertDialog dialog = new AlertDialog.Builder(getContext()).setMessage("Are sure you want exit?")

                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getContext(),LoginActivity.class));
                        getActivity().finish();

                    }
                }) //Set to null. We override the onclick
                .setNegativeButton("No", null)
                .create();
        dialog.show();
    }
}
