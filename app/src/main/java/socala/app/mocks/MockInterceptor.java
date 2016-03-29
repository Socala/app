package socala.app.mocks;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import socala.app.models.User;

public class MockInterceptor implements Interceptor {

    private static int friendId = 0;

    @Override
    public Response intercept(Chain chain) throws IOException {
        String responseString;

        final HttpUrl url = chain.request().url();
        Gson gson = new Gson();

        String path = "";

        for (String p : url.pathSegments()) {
            if (!p.equals("")) {
                path += p + "/";
            }
        }

        if (path.equals("user/")) {
            responseString = gson.toJson(createFakeUser());
        } else if (path.equals("user/friend/remove/")) {
            responseString = gson.toJson(true);
        } else if (path.equals("user/friend/add/")) {
            responseString = gson.toJson(createFakeFriend());
        } else {
            responseString = "{}";
        }

        return new Response.Builder()
                .code(200)
                .message(responseString)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
                .addHeader("content-type", "application/json")
                .build();
    }

    private User createFakeUser() {
        List<User> friends = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            friends.add(createFakeFriend());
        }

        User user = new User();
        user.setEmail("bromano@crimson.ua.edu");
        user.setFriends(friends);
        user.setCalendar(null);
        user.setDisplayName("Benjamin Romano");
        user.setId("12345");

        return user;
    }

    private User createFakeFriend() {
        User user = new User();
        user.setEmail("email" + MockInterceptor.friendId + "@gmail.com");
        user.setCalendar(null);
        user.setDisplayName("Name " + MockInterceptor.friendId);
        user.setId(String.valueOf(MockInterceptor.friendId));

        MockInterceptor.friendId++;

        return user;
    }
}
