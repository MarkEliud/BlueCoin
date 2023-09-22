package com.krypt.bluecoin.User.RegisterPages;

import static com.krypt.bluecoin.utils.Links.URL_REGISTER;
import static com.krypt.bluecoin.utils.Links.emails;
import static com.krypt.bluecoin.utils.Links.fnms;
import static com.krypt.bluecoin.utils.Links.snames;
import static com.krypt.bluecoin.utils.Links.usnms;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.krypt.bluecoin.R;
import com.krypt.bluecoin.User.UserModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Secondpage extends Fragment {
    EditText phoneno,pass,cpass,edt_select;
    ProgressBar progressBar;
    UserModel userModel;
    Button register;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.second_page,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        register=view.findViewById(R.id.register_btn);
        phoneno=view.findViewById(R.id.phoneno);
        pass=view.findViewById(R.id.pass);
        edt_select = view.findViewById(R.id.edt_select);
        edt_select.setFocusable(false);
        cpass=view.findViewById(R.id.cpass);
        progressBar=view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        userModel=new UserModel();

        edt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                select(v);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pass.getText().toString().equals(cpass.getText().toString()))
                {
                    Toast.makeText(getContext(), "password do not match", Toast.LENGTH_SHORT).show();
                }
                userModel.setPhone(phoneno.getText().toString());
                userModel.setPassword(pass.getText().toString());
                userModel.setGender(edt_select.getText().toString());
              // register(phoneno.getText().toString(),pass.getText().toString());
                register();
            }
        });
    }

    private void register() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", "is" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (status.equals("1")) {

                                getActivity().finish();
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                              e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                error.printStackTrace();
              //  Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", emails);
                params.put("phoneNo", userModel.getPhone());
                params.put("username",usnms);
                params.put("password", userModel.getPassword());
                params.put("firstname", fnms);
                params.put("lastname", snames);
                params.put("gender", userModel.getGender());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    public void select(View v) {
        final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Select");
        final String[] array = {"Male", "Female","Custom"};

        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                edt_select.setText(array[i]);
                Toast.makeText(getContext(), array[i], Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();

                builder.setNegativeButton("Close", null);


            }
        });
        builder.show();

    }
}
