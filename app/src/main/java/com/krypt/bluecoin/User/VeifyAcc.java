package com.krypt.bluecoin.User;

import static com.krypt.bluecoin.utils.Links.file;
import static com.krypt.bluecoin.utils.Links.url;

import android.app.NotificationChannel;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.app.NotificationCompat;
import android.app.NotificationManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.Activity;

import android.app.ProgressDialog;

import android.content.Intent;

import android.graphics.Bitmap;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;



import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;


import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.krypt.bluecoin.R;
import com.krypt.bluecoin.utils.SessionHandler;

import java.io.IOException;

import java.io.File;
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
    private static final int REQUEST_VIDEO_CAPTURE = 101;
    private Uri videoUri;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private ProgressDialog progressBar;
    ImageView imageView, back;
   // private static final int REQUEST_VIDEO_CAPTURE = 101;

    Button id, video;
    Bitmap bitmap, bitback;
    private SessionHandler session;
    private UserModel user;

    private static final int REQUEST_STORAGE_PERMISSION = 300;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veify_acc);
        getSupportActionBar().setTitle("Verification");
        createNotificationChannel();

        back = findViewById(R.id.image_id_back);
        imageView = findViewById(R.id.image_id_front);
        id = findViewById(R.id.passport_id);
        video = findViewById(R.id.video_id);
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

                dispatchTakeVideoIntent();
//                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 3);
//                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
//                }
            }
        });

        ActivityResultLauncher<Intent> frontImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        ActivityResultLauncher<Intent> backImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        try {
                            bitback = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            back.setImageBitmap(bitback);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        imageView.setOnClickListener(v -> {
            Intent in = new Intent(Intent.ACTION_PICK);
            in.setType("image/*");
            frontImageLauncher.launch(in);
        });

        back.setOnClickListener(v -> {
            Intent in = new Intent(Intent.ACTION_PICK);
            in.setType("image/*");
            backImageLauncher.launch(in);
        });
    }
    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("upload_channel", "Video Uploads", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
private void dispatchTakeVideoIntent() {
    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
    }
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            videoUri = data.getData();
            new UploadVideoTask().execute(videoUri);
        }
    }

    private class UploadVideoTask extends AsyncTask<Uri, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
           // NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(VeifyAcc.this, "upload_channel")
            notificationBuilder = new NotificationCompat.Builder(VeifyAcc.this, "upload_channel")

                    .setSmallIcon(android.R.drawable.stat_sys_upload)
                    .setContentTitle("Uploading Video")
                    .setContentText("Progress...")
                    .setProgress(100, 0, false);
            notificationManager.notify(1, notificationBuilder.build());
        }

        @Override
        protected String doInBackground(Uri... params) {
            try {
                File videoFile = new File(FileUtils.getPath(VeifyAcc.this, params[0]));
               // Cannot resolve method 'publishProgress(long)'
               // ProgressRequestBody requestFile = new ProgressRequestBody(videoFile, percentage -> publishProgress(percentage));
                ProgressRequestBody requestFile = new ProgressRequestBody(videoFile, new ProgressRequestBody.ProgressListener() {
                    @Override
                    public void update(long bytesWritten, long contentLength) {
                        int percentage = (int) ((bytesWritten * 100) / contentLength);
                        publishProgress(percentage);
                    }
                });

                MultipartBody.Part videoPart = MultipartBody.Part.createFormData("video", videoFile.getName(), requestFile);
                RequestBody clientIdPart = RequestBody.create(MediaType.parse("text/plain"), user.getUserID());

                OkHttpClient client = new OkHttpClient.Builder().build();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url + file)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService apiService = retrofit.create(ApiService.class);
                Call<ResponseBody> call = apiService.uploadVideo(videoPart, clientIdPart);
                retrofit2.Response<ResponseBody> response = call.execute();

                if (response.isSuccessful()) {
                    return "Upload successful!";
                } else {
                    return "Upload failed: " + response.message();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Upload failed: " + e.getMessage();
            }
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            notificationBuilder.setProgress(100, values[0], false);
            notificationManager.notify(1, notificationBuilder.build());
        }
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Upload successful!")) {
                notificationManager.cancel(1); // This cancels the notification with ID 1
            } else {
                notificationBuilder.setContentText(result)
                        .setProgress(0, 0, false)
                        .setOngoing(false);
                notificationManager.notify(1, notificationBuilder.build());
            }
        }

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

