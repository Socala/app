package socala.app.services;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import socala.app.mocks.MockInterceptor;

public class SocalaClient {

    private static String ENDPOINT = "http://www.endpoint.com/";

    private static ISocalaService dataService = null;

    public SocalaClient() {

    }

    public static ISocalaService getClient() {
        if (dataService == null) {

            // TODO: REMOVE THIS TO USE REAL SERVER
            final OkHttpClient client = new OkHttpClient().newBuilder()
                    .addInterceptor(new MockInterceptor())
                    .build();

            final Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(SocalaClient.ENDPOINT)
                    .client(client)
                    .build();

            dataService = retrofit.create(ISocalaService.class);
        }

        return dataService;
    }
}
