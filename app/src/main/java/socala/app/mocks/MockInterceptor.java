package socala.app.mocks;

import android.util.Log;

import java.io.IOException;
import java.net.URI;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MockInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.d("SignInActivity", "CALLED");
        Response response = null;

        String responseString;

        final URI uri = chain.request().url().uri();
        final String query = chain.request().url().query();

        if (query != null) {
            final String[] parsedQuery = query.split("=");

//        TODO: Write Handlers (This is an example of how to write one)
//        if(parsedQuery[0].equalsIgnoreCase("id") && parsedQuery[1].equalsIgnoreCase("1")) {
//            responseString = TEACHER_ID_1;
//        }
        }


        responseString = "{}";

        response = new Response.Builder()
                .code(200)
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                .addHeader("content-type", "application/json")
                .build();

        return response;
    }
}
