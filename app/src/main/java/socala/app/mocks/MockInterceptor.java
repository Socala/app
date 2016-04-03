package socala.app.mocks;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import socala.app.models.Event;
import socala.app.models.PrivacyLevel;
import socala.app.models.SocalaCalendar;
import socala.app.models.User;

public class MockInterceptor implements Interceptor {

    private static int friendId = 0;

    @Override
    public Response intercept(Chain chain) throws IOException {
        String responseString;

        final HttpUrl url = chain.request().url();
        final Request request = chain.request();
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
        } else if (path.equals("event/") && request.method().equals("POST")) {
            Event e = gson.fromJson(bodyToString(request), Event.class);
            e.id = UUID.randomUUID().toString();
            responseString = gson.toJson(e);
        } else if (path.contains("event/") && request.method().equals("PUT")) {
            responseString = bodyToString(request);
        } else {
            responseString = "";
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
        user.email ="bromano@crimson.ua.edu";
        user.friends = friends;
        user.calendar = createFakeCalendar();
        user.displayName = "Benjamin Romano";
        user.id = UUID.randomUUID().toString();

        return user;
    }

    private User createFakeFriend() {
        User user = new User();
        user.email = "email" + MockInterceptor.friendId + "@gmail.com";
        user.calendar = createFakeCalendar();
        user.displayName = "Name " + MockInterceptor.friendId;
        user.id = UUID.randomUUID().toString();

        MockInterceptor.friendId++;

        return user;
    }

    private SocalaCalendar createFakeCalendar() {

        SocalaCalendar c = new SocalaCalendar();
        c.id = UUID.randomUUID().toString();
        c.events = createFakeEvents();
        return c;
    }

    private List<Event> createFakeEvents() {
        List<Event> events = new ArrayList<>();

        java.util.Calendar c = java.util.Calendar.getInstance();

        for (int i = 0; i < 50; i++ ) {
            Event e = new Event();
            e.id = UUID.randomUUID().toString();
            e.title = "Event " + i;
            e.location = "";
            e.privacyLevel = PrivacyLevel.FRIEND;
            e.rsvpable = true;
            e.attendees = null;
            e.color = "#111111";
            e.start = (Calendar) c.clone();
            c.add(java.util.Calendar.HOUR, 2);
            e.end = (Calendar) c.clone();
            c.add(java.util.Calendar.HOUR, 2);

            events.add(e);
        }

        return events;
    }

    private String bodyToString(final Request request){

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
