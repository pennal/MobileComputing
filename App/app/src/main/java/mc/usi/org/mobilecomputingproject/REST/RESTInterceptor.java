package mc.usi.org.mobilecomputingproject.REST;

import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import mc.usi.org.mobilecomputingproject.Utils.Singleton;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Lucas on 17.12.17.
 */

public class RESTInterceptor implements Interceptor {
    public static final String TAG = "INTERCEPTOR";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request modReq = originalRequest;


        if (!containsAny(Arrays.asList("/login", "/email"), originalRequest.url().toString()) && !alreadyHasAuthorizationHeader(modReq)) {
            modReq = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer " + Prefs.getString("token", ""))
                    .build();
            Log.d(TAG, "Adding token to the request");
        }
        // Start by executing the actual request
        Log.d(TAG, "Executing call to address " + modReq.url().toString());
        Response res = chain.proceed(modReq);
        Log.d(TAG, "Call finished. Code: " + res.code());


//        // WARNING: DON'T YOU DARE CALL res.body.string() AS IT WILL MAKE THE BLOODY LIBRARY FAIL!
//        Log.v(TAG, "Body: " + resDebug.body().string());

        // Return the result
        return res;
    }

    private boolean authorizationTokenIsEmpty(Request req) {
        return req.header("Authorization") == null || req.header("Authorization").equals("");
    }

    private boolean alreadyHasAuthorizationHeader(Request req) {
        return req.header("Authorization") != null && !req.header("Authorization").equals("");
    }

    private boolean containsAny(List<String> urls, String checkUrl) {
        boolean contains = false;

        for (String u : urls) {
            if (u.equals(checkUrl)) {
                contains = true;
            }
        }

        return contains;
    }


}