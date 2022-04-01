package dev.onprojek.com.realtimeabsensiapp.services;

import java.util.List;

import dev.onprojek.com.realtimeabsensiapp.models.LocationRangeResponseModel;
import dev.onprojek.com.realtimeabsensiapp.models.RealtimeAbsensiResponseModel;
import dev.onprojek.com.realtimeabsensiapp.models.ResponseUbahPassword;
import dev.onprojek.com.realtimeabsensiapp.models.UserAbsensiResponseModel;
import dev.onprojek.com.realtimeabsensiapp.models.UserLoginResponseModel;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RealtimeAbsensiInterface {

    @GET("login")
    Call<List<UserLoginResponseModel>> login(@Query("nip") String nip, @Query("password") String password);

    @GET("LocationRange")
    Call<List<LocationRangeResponseModel>> getLocationRange();

    @Multipart
    @POST("absensi")
    Call<UserAbsensiResponseModel> absen(@Part("nama") RequestBody rNama,
                                         @Part("pangkat") RequestBody rPangkat,
                                         @Part("nip") RequestBody rNip,
                                         @Part("latitude") RequestBody rLatitude,
                                         @Part("longitude") RequestBody rLongitude);


    @Multipart
    @POST("realtime")
    Call<RealtimeAbsensiResponseModel> realtime(@Part("nip") RequestBody rNip,
                                                @Part("longitude") RequestBody rLongitude,
                                                @Part("latitude") RequestBody rLatitude);


    @FormUrlEncoded
    @PUT("user")
    Call<ResponseUbahPassword> updatePassword(@Field("nip") String nip,
                                              @Field("password") String password);

}
