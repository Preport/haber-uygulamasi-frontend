package net.perport.haberuygulamasi.APIEndpoints;

import net.perport.haberuygulamasi.APIEndpoints.DTOs.createCommentDTO;
import net.perport.haberuygulamasi.APIEndpoints.DTOs.createUserDTO;
import net.perport.haberuygulamasi.APIEndpoints.DTOs.loginDTO;
import net.perport.haberuygulamasi.APIEndpoints.Responses;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebService {

    @POST("kullanici/giris")
    Call<Responses.LoginResponse> login(@Body() loginDTO data);

    @POST("kullanici/token")
    Call<Responses.RefreshTokenResponse> refreshAccessToken(@Header("Authorization") String refreshToken);

    @POST("kullanici/")
    Call<Responses.PostAccountResponse> createAccount(@Body() createUserDTO user);

    @GET("haber")
    Call<Responses.GetHaberResponse> getHaber(@Query("before") String before, @Query("after") String after,@Query("count") int count);

    @GET("haber")
    Call<Responses.GetHaberResponse> getHaberBefore(@Query("before") String before,@Query("count") int count);

    @GET("haber")
    Call<Responses.GetHaberResponse> getHaberAfter(@Query("after") String after,@Query("count") int count);

    @GET("haber")
    Call<Responses.GetHaberResponse> getHaber(@Query("count") int count);

    @GET("haber/ara/{search}")
    Call<Responses.GetHaberSearchResponse> searchHaber(@Path("search") String search);

    @GET("haber/{id}")
    Call<Responses.GetSingleHaberResponse> getHaber(@Path("id") String id);

    @GET("yorum/{id}")
    Call<Responses.GetCommentsResponse> getComments(@Path("id") String haberID);

    @POST("yorum/")
    Call<Responses.PostCommentResponse> createComment(@Header("Authorization") String accessToken, @Body() createCommentDTO comment);

    @GET("bildirim/")
    Call<Responses.GetNotificationsResponse> getNotifications(@Header("Authorization") String accessToken, @Query("notify") boolean notify);

    @DELETE("bildirim/{id}")
    Call<Responses.BaseResponse> deleteNotification(@Header("Authorization") String accessToken, @Path("id") String notificationID);
}

