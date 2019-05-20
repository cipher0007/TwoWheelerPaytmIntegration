package com.cipher0007.twowheeler.Network;

import android.graphics.Bitmap;

import com.cipher0007.twowheeler.Network.Models.AmountItem;
import com.cipher0007.twowheeler.Network.Models.BikeNo;
import com.cipher0007.twowheeler.Network.Models.FirstCheck;
import com.cipher0007.twowheeler.Network.Models.GetProfilePhotoItem;
import com.cipher0007.twowheeler.Network.Models.LocationItem;
import com.cipher0007.twowheeler.Network.Models.ProfilePhotoItem;
import com.cipher0007.twowheeler.Network.Models.Rate;
import com.cipher0007.twowheeler.Network.Models.Register;
import com.cipher0007.twowheeler.Network.Models.YourBookingItem;

import java.util.List;

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

    @FormUrlEncoded
    @POST("android/showbikeswithstatus.php")
    Call<List<BikeNo>> bikeno(@Field("number") String number);

    @FormUrlEncoded
    @POST("android/rates.php")
    Call<List<Rate>> BikeRates(@Field("number") String number);

    @FormUrlEncoded
    @POST("android/bookinghistory.php")
    Call<List<YourBookingItem>> Bookings(@Field("number") String number);

    @FormUrlEncoded
    @POST("android/profilepicupload.php")
    Call<ProfilePhotoItem> UploadProfilePhoto(@Field("number") String number,
                                                    @Field("fileToUpload") String fileToUpload);

    @FormUrlEncoded
    @POST("android/getprofpic.php")
    Call<GetProfilePhotoItem> GetProfilePhoto(@Field("number") String number);

    @FormUrlEncoded
    @POST("android/firstcheck.php")
    Call<FirstCheck> FirstCheckData(@Field("number") String number);

    @FormUrlEncoded
    @POST("android/fetchlocation.php")
    Call<List<LocationItem>> LocationAll(@Field("number") String number);

    @FormUrlEncoded
    @POST("android/billdetails.php")
    Call<AmountItem> AmountPay(@Field("number") String number,
                                     @Field("noh") String noh);


}
