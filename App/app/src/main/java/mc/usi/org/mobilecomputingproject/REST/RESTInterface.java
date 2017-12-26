package mc.usi.org.mobilecomputingproject.REST;

import java.util.List;

import mc.usi.org.mobilecomputingproject.Models.Ride;
import mc.usi.org.mobilecomputingproject.REST.models.PlainCredentials;
import mc.usi.org.mobilecomputingproject.REST.models.UserSignup;
import mc.usi.org.mobilecomputingproject.REST.responses.TokenResponse;
import mc.usi.org.mobilecomputingproject.REST.responses.UserResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Lucas on 17.12.17.
 */

public interface RESTInterface {
    @POST("/login")
    Call<TokenResponse> loginWithPlainCredentials(@Body PlainCredentials credentials);

    @GET("/users/me")
    Call<UserResponse> getUserInfo();


    // signup
    @POST("/signup")
    Call<Void> createUser(@Body UserSignup userSignup);



    // Rides
    @POST("/rides")
    Call<Void> postRide(@Body Ride ride);

    @GET("/rides/public")
    Call<List<Ride>> getPublicRides();

    @GET("/rides/mine")
    Call<List<Ride>> getPersonalRides();

    @PUT("/rides/{id}/makePublic")
    Call<Void> makeRoutePublic(@Path("id") String id);

    @Multipart
    @PUT("/users/picture")
    Call<Void> uploadProfilePic(@Part MultipartBody.Part imageFile);
}
