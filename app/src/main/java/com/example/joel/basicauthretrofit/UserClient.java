package com.example.joel.basicauthretrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {
    @POST("user/register")
    Call<User> login(@Body Login login);

    @POST("user/login")
    Call<User> signup(@Body SignUp signup);


    @GET("users")
    Call<ResponseBody> getSecret(@Header("Authorization") String authToken);
}
