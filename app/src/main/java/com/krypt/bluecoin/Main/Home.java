package com.krypt.bluecoin.Main;

import static android.widget.LinearLayout.HORIZONTAL;

import static com.krypt.bluecoin.utils.Links.URL_GET_TRANS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krypt.bluecoin.Adapters.AdaptersTransactions;
import com.krypt.bluecoin.Models.TransModel;
import com.krypt.bluecoin.R;
import com.krypt.bluecoin.User.UserModel;
import com.krypt.bluecoin.utils.AddCard;
import com.krypt.bluecoin.utils.PaymentMethod_;
import com.krypt.bluecoin.utils.SessionHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends Fragment {
    TextView add,wlecometxt,txv_info;
    private SessionHandler session;
    private List<TransModel> list;
    private AdaptersTransactions adptTransactions;
    ProgressBar progressBar;
    private UserModel user;
//    Button card_,Deposit,send;
    ImageButton deposit,withdraw;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.home_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar=view.findViewById(R.id.progressBar);

        recyclerView=view.findViewById(R.id.recView);
        txv_info=view.findViewById(R.id.txt_info);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView = view.findViewById(R.id.recView);




        list = new ArrayList<>();
//        card_=view.findViewById(R.id.btn_card);
        deposit=view.findViewById(R.id.depo_btn);

        withdraw=view.findViewById(R.id.withdraw_btn);

        wlecometxt=view.findViewById(R.id.welcome_txt);
        try {
            session = new SessionHandler(getActivity());
            user = session.getUserDetails();


            wlecometxt.setText("Welcome back "+user.getFname()+".");




        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }


//        card_.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toCard();
//            }
//        });
        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getActivity(), PaymentMethod_.class));
            }
        });
        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PaymentMethod_.class));
            }
        });
        getUserTransaction();
    }

    private void getUserTransaction() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_TRANS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagtrans", "["+response+"]");
                            Log.e("Response", "" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");


                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("trans");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String transid = jsn.getString("transid");
                                    String amount = jsn.getString("amount");
                                    String currency = jsn.getString("currency");
                                    String date = jsn.getString("trandate");


                                    TransModel transModel = new TransModel(transid, amount, currency,date);

                                    list.add(transModel);

                                }
                                txv_info.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                             //   layout_bottom.setVisibility(View.VISIBLE);
                                adptTransactions = new AdaptersTransactions(getContext(), list);

                                recyclerView.setAdapter(adptTransactions);



                            } else if (status.equals("0")) {

                               progressBar.setVisibility(View.GONE);
                               txv_info.setVisibility(View.VISIBLE);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clientID", user.getUserID());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void toCard() {
        startActivity(new Intent(getContext(), CardActivity.class));
    }
}
