package socala.app.services;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import socala.app.serializers.CalendarDeserializer;
import socala.app.serializers.CalendarSerializer;

public class SocalaClient {

    private static String ENDPOINT = "http://130.160.152.68:8080";

    private static ISocalaService dataService = null;
    private static Context context = null;

    public SocalaClient() {

    }

    public static void setContext(Context context) {
        SocalaClient.context = context;
    }

    public static ISocalaService getClient() {

        if (SocalaClient.context == null) {
            throw new RuntimeException("Set context before getting client!");
        }

        if (dataService == null) {

            ClearableCookieJar cookieJar =
                    new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));

            // TODO: REMOVE THIS TO USE REAL SERVER
            final OkHttpClient client = new OkHttpClient().newBuilder()
//                    .addInterceptor(new MockInterceptor())
                    .cookieJar(cookieJar)
                    .build();

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZZ")
                    .registerTypeAdapter(Calendar.class, new CalendarDeserializer())
                    .registerTypeHierarchyAdapter(Calendar.class, new CalendarSerializer()).create();

            final Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(SocalaClient.ENDPOINT)
                    .client(client)
                    .build();

            dataService = retrofit.create(ISocalaService.class);
        }

        return dataService;
    }
}
