package com.krypt.bluecoin.Main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.krypt.bluecoin.LoginActivity;
import com.krypt.bluecoin.R;
import com.krypt.bluecoin.User.UserModel;
import com.krypt.bluecoin.User.VeifyAcc;
import com.krypt.bluecoin.utils.SessionHandler;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends Fragment {
    private SessionHandler session;
    private UserModel user;
    ImageView profilepic;
    Bitmap bitmap;
    CardView upload_card_id;
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
       profilepic=view.findViewById(R.id.edit_prof);
        circleImageView=view.findViewById(R.id.account_id);
        upload_card_id=view.findViewById(R.id.upload_card_id);
        usnm=view.findViewById(R.id.username_id);
        logout=view.findViewById(R.id.id_logout);
        verfyacc_=view.findViewById(R.id.verify_accoutid);
        txt_status=view.findViewById(R.id.txt_status);


        try {
            session = new SessionHandler(getActivity());
            user = session.getUserDetails();


            usnm.setText(user.getFname() + " " + user.getSname()+ " " + user.getUserID());
            txt_status.setText(user.getStatus());



        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
//                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//                pDialog.setTitleText("Loading");
//                pDialog.setCancelable(false);
//                pDialog.show();
            }
        });
        upload_card_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), VeifyAcc.class));

            }
        });
        ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()== Activity.RESULT_OK){
                    Intent data=result.getData();
                    Uri uri=data.getData();
                    try {
                        bitmap= MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
                        circleImageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });

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
