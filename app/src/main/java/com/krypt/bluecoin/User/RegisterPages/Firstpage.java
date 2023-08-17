package com.krypt.bluecoin.User.RegisterPages;

import static com.krypt.bluecoin.utils.Links.VAL_USRNM;
import static com.krypt.bluecoin.utils.Links.emails;
import static com.krypt.bluecoin.utils.Links.fnms;
import static com.krypt.bluecoin.utils.Links.usnms;
import static com.krypt.bluecoin.utils.Links.snames;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.krypt.bluecoin.R;
import com.krypt.bluecoin.User.UserModel;
import com.krypt.bluecoin.utils.PaymentMethod;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Firstpage extends Fragment {
    Button nextpg,validateusrnm;
    public UserModel userModel=new UserModel();
    EditText fnm,snm,usrnm,email;
    ProgressBar progressBar;
    Secondpage secondpage=new Secondpage();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.first_page,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextpg=view.findViewById(R.id.next_btn);
        fnm=view.findViewById(R.id.fname);
        snm=view.findViewById(R.id.sname);
        usrnm=view.findViewById(R.id.usernm);
        email=view.findViewById(R.id.email);
        //validateusrnm=view.findViewById(R.id.validateusrnm);
//        nextpg.setVisibility(View.GONE);
//        email.setVisibility(View.GONE);
        progressBar=new ProgressBar(getContext());
        userModel=new UserModel();

//        validateusrnm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                validateusernm(usrnm.getText().toString());
//            }
//        });
        nextpg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key", "value");

//               Fragment fragment = new AnotherFragment();
//                fragment.setArguments(bundle);
//
//// Replace or add the fragment
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment);
//                transaction.commit();
                userModel=new UserModel();
                userModel.setFname(fnm.getText().toString());
                fnms=fnm.getText().toString();
                snames=snm.getText().toString();
                usnms=usrnm.getText().toString();
                emails=email.getText().toString();
                userModel.setSname(snm.getText().toString());
                userModel.setUsername(usrnm.getText().toString());
                userModel.setEmail(email.getText().toString());

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.cointainer,secondpage).commit();
            }
        });
    }

//    private void validateusernm(String usernm) {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, VAL_USRNM,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("Response", "is" + response);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String status = jsonObject.getString("status");
//                            String msg = jsonObject.getString("message");
//                            if (status.equals("0")) {
//
//                                getActivity().finish();
//                                nextpg.setVisibility(View.VISIBLE);
//                                email.setVisibility(View.VISIBLE);
//                                progressBar.setVisibility(View.GONE);
//                            } else {
//                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
//
//                                usrnm.setError("Username not Available");
//
//                                nextpg.setVisibility(View.GONE);
//                                progressBar.setVisibility(View.GONE);
//
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                error.printStackTrace();
//                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("usrnm", usernm);
//
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
//    }

}
