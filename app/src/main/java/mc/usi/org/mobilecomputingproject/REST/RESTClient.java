package mc.usi.org.mobilecomputingproject.REST;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Lucas on 17.12.17.
 */

public class RESTClient {
//    public static final String BASE_URL = "http://10.0.2.2:8080";
    public static final String BASE_URL = "http://10.62.131.48:8080";
    private static Retrofit sRetrofit = null;

    public static Retrofit getClient() {
        if (sRetrofit == null) {

            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient
                    .Builder()
                    .addInterceptor(new RESTInterceptor())
                    .addInterceptor(logInterceptor)
                    .build();

            Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
                    .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, jsonSerializationContext) -> new JsonPrimitive(date.getTime()))
                    .create();


            sRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return sRetrofit;
    }
}
