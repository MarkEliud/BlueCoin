package com.krypt.bluecoin.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.Call;
public interface ApiService {
    @Multipart
    @POST("vidupload.php")
    Call<ResponseBody> uploadVideo(@Part MultipartBody.Part video, @Part("client_id") RequestBody clientId);

}

