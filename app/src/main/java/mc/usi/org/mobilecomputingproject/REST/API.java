package mc.usi.org.mobilecomputingproject.REST;


import java.util.List;

import mc.usi.org.mobilecomputingproject.Models.Ride;
import mc.usi.org.mobilecomputingproject.REST.models.PlainCredentials;
import mc.usi.org.mobilecomputingproject.REST.models.UserSignup;
import mc.usi.org.mobilecomputingproject.REST.responses.TokenResponse;
import mc.usi.org.mobilecomputingproject.REST.responses.UserResponse;
import mc.usi.org.mobilecomputingproject.Views.RidesList.RideListFragment;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Lucas on 17.12.17.
 */

public class API {

    private static RESTInterface getClient() {
        return RESTClient.getClient().create(RESTInterface.class);
    }

    public static void loginWithPlainCredentials(String username, String password, Callback<TokenResponse> callback) {
        PlainCredentials credentials = new PlainCredentials(username, password);

        Call<TokenResponse> call = getClient().loginWithPlainCredentials(credentials);

        call.enqueue(callback);

    }

    public static void getUserInfo(Callback<UserResponse> callback) {
        Call<UserResponse> call = getClient().getUserInfo();
        call.enqueue(callback);
    }

    public static void postRide(Ride ride, Callback<Void> callback) {
        Call<Void> call = getClient().postRide(ride);
        call.enqueue(callback);
    }

    public static void getRides(RideListFragment.RideType rideType, Callback<List<Ride>> callback) {
        Call<List<Ride>> call = null;

        switch (rideType) {
            case PUBLIC:
                call = getClient().getPublicRides();
                break;
            case PERSONAL:
                call = getClient().getPersonalRides();
                break;
            default:
                call = getClient().getPublicRides();
                break;
        }

        call.enqueue(callback);
    }

    public static void makeRoutePublic(String rideId, Callback<Void> callback) {
        Call<Void> call = getClient().makeRoutePublic(rideId);
        call.enqueue(callback);
    }

    public static void userSignup(UserSignup userSignup, Callback<Void> callback) {
        Call<Void> call = getClient().createUser(userSignup);
        call.enqueue(callback);
    }

    public static void uploadProfilePicture(MultipartBody.Part imageFile, Callback<Void> callback) {
        Call<Void> call = getClient().uploadProfilePic(imageFile);

        call.enqueue(callback);
    }
}
