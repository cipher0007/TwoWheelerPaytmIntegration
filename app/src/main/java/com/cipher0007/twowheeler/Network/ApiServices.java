package com.cipher0007.twowheeler.Network;

import android.graphics.Bitmap;

import com.cipher0007.twowheeler.Network.Models.Register;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface ApiServices {
    @FormUrlEncoded
    @POST("android/androregis.php")
    Call<Register> createAccount(@Field("fname") String fname,
                                 @Field("lname") String lname,
                                 @Field("email") String email,
                                 @Field("number") String number,
                                 @Field("fileToUpload") String fileToUpload, @Field("fileToUpload1") String  fileToUpload1
    );
}
