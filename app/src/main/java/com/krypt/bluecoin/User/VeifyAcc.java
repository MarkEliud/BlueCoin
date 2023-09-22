package com.krypt.bluecoin.User;

import static com.krypt.bluecoin.utils.Links.URL_UPLOAD_ID;
import static com.krypt.bluecoin.utils.Links.userid;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krypt.bluecoin.R;
import com.krypt.bluecoin.utils.SessionHandler;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VeifyAcc extends AppCompatActivity {
    private ProgressDialog progressBar;
    ImageView imageView,back;
    Button id,video;
    Bitmap bitmap,bitback;
    private SessionHandler session;
    private UserModel user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veify_acc);
        getSupportActionBar().setTitle("Verification");

         back=findViewById(R.id.image_id_back);
        imageView=findViewById(R.id.image_id_front);
        id=findViewById(R.id.passport_id);
        video=findViewById(R.id.video_id);
        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadId();

            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()== Activity.RESULT_OK){
                    Intent data=result.getData();
                    Uri uri=data.getData();
                    try {
                        bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
        ActivityResultLauncher<Intent> activityResultLauncher_=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()== Activity.RESULT_OK){
                    Intent data=result.getData();
                    Uri uri=data.getData();
                    try {
                        bitback= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                        back.setImageBitmap(bitback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Intent.ACTION_PICK);
                in.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(in);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Intent.ACTION_PICK);
                in.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher_.launch(in);

            }
        });

    }



    private void uploadId() {
        progressBar=new ProgressDialog(VeifyAcc.this);
        progressBar.setMessage("Uploading");
        progressBar.setTitle("nat'ID/passport");
        progressBar.show();




        ByteArrayOutputStream byteArrayOutputStream,byteArrayOutputStream_;
        byteArrayOutputStream=new ByteArrayOutputStream();
        byteArrayOutputStream_=new ByteArrayOutputStream();
        if (bitmap!=null||bitback!=null){
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            bitback.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream_);
            byte[] bytes=byteArrayOutputStream.toByteArray();
            byte[] bytes_=byteArrayOutputStream_.toByteArray();
            final String base64= Base64.encodeToString(bytes,Base64.DEFAULT);
            final String base64_= Base64.encodeToString(bytes_,Base64.DEFAULT);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", "is" + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("message");
                                if (status.equals("1")) {
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                    finish();
                                    progressBar.cancel();
                                } else {
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                                    progressBar.cancel();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                progressBar.cancel();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    progressBar.cancel();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("idpass", base64);
                    params.put("idback", base64_);
                    params.put("clientID", user.getUserID());


                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);


        }
        else Toast.makeText(VeifyAcc.this, "Select your ID back and front", Toast.LENGTH_SHORT).show();
    }


    }

