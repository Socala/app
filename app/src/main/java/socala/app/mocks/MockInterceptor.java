package socala.app.mocks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import socala.app.serializers.CalendarDeserializer;
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

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZZ").registerTypeAdapter(Calendar.class, new CalendarDeserializer()).create();

        String path = "";

        for (String p : url.pathSegments()) {
            if (!p.equals("")) {
                path += p + "/";
            }
        }

        if (path.equals("users/") && url.queryParameter("email") != null) {
            responseString = gson.toJson(createFakeFriend());
        } else if (path.equals("users/signin/")) {
            responseString = createFakeUser();
        } else if (path.equals("users/friends/remove/")) {
            responseString = gson.toJson(true);
        } else if (path.equals("users/friends/add/")) {
            responseString = gson.toJson(createFakeFriend());
        } else if (path.equals("events/") && request.method().equals("POST")) {
            Event e = gson.fromJson(bodyToString(request), Event.class);
            e.id = UUID.randomUUID().toString();
            responseString = gson.toJson(e);
        } else if (path.contains("events/") && request.method().equals("PUT")) {
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

    private String createFakeUser() {
        return "{\n" +
                "  \"displayName\": \"\",\n" +
                "  \"id\": \"40122576-2c8b-4faf-9dcc-2951fd0446c2\",\n" +
                "  \"email\": \"socalatest@gmail.com\",\n" +
                "  \"friends\": [\n" +
                "    {\n" +
                "      \"id\": \"cd086e49-2a4d-49be-b8d5-c00cf894ad85\",\n" +
                "      \"email\": \"bromano@crimson.ua.edu\",\n" +
                "      \"friends\": [],\n" +
                "      \"calendar\": {\n" +
                "        \"events\": [\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"COE Mentor Program Meeting\",\n" +
                "            \"start\": \"2013-10-11T10:00:00-05:00\",\n" +
                "            \"end\": \"2013-10-11T11:00:00-05:00\",\n" +
                "            \"location\": \"Bevill 1000\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"CAPS Halloween Party\",\n" +
                "            \"start\": \"2013-10-31T11:00:00-05:00\",\n" +
                "            \"end\": \"2013-10-31T13:00:00-05:00\",\n" +
                "            \"location\": \"2038 Shelby Hall\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Apple Engineer Presentation\",\n" +
                "            \"start\": \"2013-11-06T15:30:00-06:00\",\n" +
                "            \"end\": \"2013-11-06T17:30:00-06:00\",\n" +
                "            \"location\": \"Lloyd 38\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Dine.Net\",\n" +
                "            \"start\": \"2013-11-14T11:30:00-06:00\",\n" +
                "            \"end\": \"2013-11-14T13:00:00-06:00\",\n" +
                "            \"location\": \"Gorgas 205\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Dean Karr All Hands Meeting\",\n" +
                "            \"start\": \"2013-12-05T13:00:00-06:00\",\n" +
                "            \"end\": \"2013-12-05T15:00:00-06:00\",\n" +
                "            \"location\": \"Bevill 1000\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"CAPS Holiday Party\",\n" +
                "            \"start\": \"2013-12-18T14:00:00-06:00\",\n" +
                "            \"end\": \"2013-12-18T16:00:00-06:00\",\n" +
                "            \"location\": \"Bevill 1000\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"All-Hands meeting\",\n" +
                "            \"start\": \"2014-01-29T14:00:00-06:00\",\n" +
                "            \"end\": \"2014-01-29T15:00:00-06:00\",\n" +
                "            \"location\": \"Shelby 1092 \",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"All-Hands Meeting\",\n" +
                "            \"start\": \"2014-02-10T14:00:00-06:00\",\n" +
                "            \"end\": \"2014-02-10T15:00:00-06:00\",\n" +
                "            \"location\": \"Shelby 1092 \",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"CAPS All Hands Meeting\",\n" +
                "            \"start\": \"2014-02-17T14:00:00-06:00\",\n" +
                "            \"end\": \"2014-02-17T15:00:00-06:00\",\n" +
                "            \"location\": \"Shelby 1092\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"All Hands Meeting\",\n" +
                "            \"start\": \"2014-04-28T14:00:00-05:00\",\n" +
                "            \"end\": \"2014-04-28T15:30:00-05:00\",\n" +
                "            \"location\": \"Shelby 1092\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Dine.Net\",\n" +
                "            \"start\": \"2014-03-20T11:30:00-05:00\",\n" +
                "            \"end\": \"2014-03-20T13:00:00-05:00\",\n" +
                "            \"location\": \"NERC 1012\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"GoToMeeting Invitation - All Hands Meeting\",\n" +
                "            \"start\": \"2014-05-12T14:00:00-05:00\",\n" +
                "            \"end\": \"2014-05-12T15:30:00-05:00\",\n" +
                "            \"location\": \"Shelby 1092\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"FW: CAPS All Hands Meeting\",\n" +
                "            \"start\": \"2014-09-15T13:30:00-05:00\",\n" +
                "            \"end\": \"2014-09-15T15:00:00-05:00\",\n" +
                "            \"location\": \"Shelby 1092\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Acm meeting\",\n" +
                "            \"start\": \"2014-09-30T17:15:11-05:00\",\n" +
                "            \"end\": \"2014-09-30T18:15:11-05:00\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Automotive meeting \",\n" +
                "            \"start\": \"2014-10-03T11:00:00-05:00\",\n" +
                "            \"end\": \"2014-10-03T12:00:00-05:00\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Dixon Myers meeting \",\n" +
                "            \"start\": \"2014-10-06T14:00:51-05:00\",\n" +
                "            \"end\": \"2014-10-06T15:00:51-05:00\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Bender meeting\",\n" +
                "            \"start\": \"2014-10-06T16:00:00-05:00\",\n" +
                "            \"end\": \"2014-10-06T17:00:00-05:00\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Weather lecture \",\n" +
                "            \"start\": \"2014-10-09T15:15:00-05:00\",\n" +
                "            \"end\": \"2014-10-09T16:15:00-05:00\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Resume Review \",\n" +
                "            \"start\": \"2014-10-15T13:30:00-05:00\",\n" +
                "            \"end\": \"2014-10-15T14:00:00-05:00\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Lunch with Taylor Gordon \",\n" +
                "            \"start\": \"2014-10-16T12:00:00-05:00\",\n" +
                "            \"end\": \"2014-10-16T13:00:00-05:00\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"CAPS All Hands Meeting\",\n" +
                "            \"start\": \"2014-10-20T14:00:00-05:00\",\n" +
                "            \"end\": \"2014-10-20T15:30:00-05:00\",\n" +
                "            \"location\": \"Shelby 1092\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Going Away Party for Zach Parker\",\n" +
                "            \"start\": \"2014-10-15T15:00:00-05:00\",\n" +
                "            \"end\": \"2014-10-15T16:00:00-05:00\",\n" +
                "            \"location\": \"NERC 1012\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Advising\",\n" +
                "            \"start\": \"2014-10-21T09:00:00-05:00\",\n" +
                "            \"end\": \"2014-10-21T10:00:00-05:00\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Cbh background slides\",\n" +
                "            \"start\": \"2014-10-27T11:00:00-05:00\",\n" +
                "            \"end\": \"2014-10-27T13:30:00-05:00\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Cbh presentation\",\n" +
                "            \"start\": \"2014-11-12T12:00:00-06:00\",\n" +
                "            \"end\": \"2014-11-12T13:00:00-06:00\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"CAPS All Hands Meeting\",\n" +
                "            \"start\": \"2014-12-08T14:00:00-06:00\",\n" +
                "            \"end\": \"2014-12-08T15:30:00-06:00\",\n" +
                "            \"location\": \"Shelby 1092\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Acm meeting\",\n" +
                "            \"start\": \"2014-11-02T17:00:00-06:00\",\n" +
                "            \"end\": \"2014-11-02T18:00:00-06:00\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Stay at Hilton Madison Monona Terrace\",\n" +
                "            \"start\": \"2015-10-25T00:00:00-00:00\",\n" +
                "            \"end\": \"2015-10-27T00:00:00-00:00\",\n" +
                "            \"location\": \"Hilton Madison Monona Terrace\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to Chicago\",\n" +
                "            \"start\": \"2015-10-25T09:32:00-05:00\",\n" +
                "            \"end\": \"2015-10-25T11:35:00-05:00\",\n" +
                "            \"location\": \"Birmingham BHM\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to Madison\",\n" +
                "            \"start\": \"2015-10-25T12:20:00-05:00\",\n" +
                "            \"end\": \"2015-10-25T13:13:00-05:00\",\n" +
                "            \"location\": \"Chicago ORD\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to Chicago\",\n" +
                "            \"start\": \"2015-10-26T17:16:00-05:00\",\n" +
                "            \"end\": \"2015-10-26T18:13:00-05:00\",\n" +
                "            \"location\": \"Madison MSN\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to Birmingham\",\n" +
                "            \"start\": \"2015-10-26T19:18:00-05:00\",\n" +
                "            \"end\": \"2015-10-26T21:07:00-05:00\",\n" +
                "            \"location\": \"Chicago ORD\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Stay at San Francisco Marriott Marquis\",\n" +
                "            \"start\": \"2015-11-05T00:00:00-00:00\",\n" +
                "            \"end\": \"2015-11-07T00:00:00-00:00\",\n" +
                "            \"location\": \"San Francisco Marriott Marquis\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to Houston\",\n" +
                "            \"start\": \"2015-11-05T16:18:00-06:00\",\n" +
                "            \"end\": \"2015-11-05T18:15:00-06:00\",\n" +
                "            \"location\": \"Birmingham BHM\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to San Francisco\",\n" +
                "            \"start\": \"2015-11-05T19:31:00-06:00\",\n" +
                "            \"end\": \"2015-11-05T23:55:00-06:00\",\n" +
                "            \"location\": \"Houston IAH\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to Atlanta\",\n" +
                "            \"start\": \"2015-11-07T00:55:00-06:00\",\n" +
                "            \"end\": \"2015-11-07T05:20:00-06:00\",\n" +
                "            \"location\": \"San Francisco SFO\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to Birmingham\",\n" +
                "            \"start\": \"2015-11-07T07:40:00-06:00\",\n" +
                "            \"end\": \"2015-11-07T08:30:00-06:00\",\n" +
                "            \"location\": \"Atlanta ATL\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to Chicago\",\n" +
                "            \"start\": \"2015-12-11T18:50:00-06:00\",\n" +
                "            \"end\": \"2015-12-11T20:40:00-06:00\",\n" +
                "            \"location\": \"Birmingham BHM\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to Detroit\",\n" +
                "            \"start\": \"2016-01-06T06:35:00-06:00\",\n" +
                "            \"end\": \"2016-01-06T08:07:00-06:00\",\n" +
                "            \"location\": \"Wausau CWA\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to New York\",\n" +
                "            \"start\": \"2016-01-06T09:00:00-06:00\",\n" +
                "            \"end\": \"2016-01-06T10:43:00-06:00\",\n" +
                "            \"location\": \"Detroit DTW\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to Birmingham\",\n" +
                "            \"start\": \"2016-01-10T20:25:00-06:00\",\n" +
                "            \"end\": \"2016-01-10T22:05:00-06:00\",\n" +
                "            \"location\": \"Chicago MDW\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"ACM Meeting\",\n" +
                "            \"start\": \"2016-04-05T17:15:00-05:00\",\n" +
                "            \"end\": \"2016-04-05T18:45:00-05:00\",\n" +
                "            \"location\": \"NERC 1012\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Officers Meeting\",\n" +
                "            \"start\": \"2016-01-17T14:00:00-06:00\",\n" +
                "            \"end\": \"2016-01-17T15:00:00-06:00\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Nath lunch\",\n" +
                "            \"start\": \"2016-01-22T14:00:00-06:00\",\n" +
                "            \"end\": \"2016-01-22T15:00:00-06:00\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"ACM Meeting\",\n" +
                "            \"start\": \"2016-01-26T17:15:00-06:00\",\n" +
                "            \"end\": \"2016-01-26T18:45:00-06:00\",\n" +
                "            \"location\": \"SERC 1013\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Study session\",\n" +
                "            \"start\": \"2016-01-26T19:00:00-06:00\",\n" +
                "            \"end\": \"2016-01-26T20:00:00-06:00\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Credit union in the ferg\",\n" +
                "            \"start\": \"2016-01-27T13:00:00-06:00\",\n" +
                "            \"end\": \"2016-01-27T14:00:00-06:00\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Amazon interview \",\n" +
                "            \"start\": \"2016-01-28T17:00:00-06:00\",\n" +
                "            \"end\": \"2016-01-28T18:00:00-06:00\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"PRAMP\",\n" +
                "            \"start\": \"2016-02-03T22:00:00-06:00\",\n" +
                "            \"end\": \"2016-02-03T23:00:00-06:00\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"PRAMP\",\n" +
                "            \"start\": \"2016-02-05T19:00:00-06:00\",\n" +
                "            \"end\": \"2016-02-05T20:00:00-06:00\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"PRAMP\",\n" +
                "            \"start\": \"2016-02-06T17:00:00-06:00\",\n" +
                "            \"end\": \"2016-02-06T18:00:00-06:00\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Amazon interview\",\n" +
                "            \"start\": \"2016-02-08T16:00:00-06:00\",\n" +
                "            \"end\": \"2016-02-08T17:00:00-06:00\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Source training\",\n" +
                "            \"start\": \"2016-02-22T18:00:00-06:00\",\n" +
                "            \"end\": \"2016-02-22T19:00:00-06:00\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Amazon interview\",\n" +
                "            \"start\": \"2016-02-10T16:00:00-06:00\",\n" +
                "            \"end\": \"2016-02-10T17:00:00-06:00\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Dr. Gray meeting\",\n" +
                "            \"start\": \"2016-02-17T13:30:00-06:00\",\n" +
                "            \"end\": \"2016-02-17T14:30:00-06:00\",\n" +
                "            \"color\": \"#ffb878\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Smash Bros\",\n" +
                "            \"start\": \"2016-02-20T14:00:00-06:00\",\n" +
                "            \"end\": \"2016-02-20T15:00:00-06:00\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Advising\",\n" +
                "            \"start\": \"2016-02-24T13:30:00-06:00\",\n" +
                "            \"end\": \"2016-02-24T15:30:00-06:00\",\n" +
                "            \"color\": \"#dbadff\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Advising\",\n" +
                "            \"start\": \"2016-03-02T10:30:00-06:00\",\n" +
                "            \"end\": \"2016-03-02T11:00:00-06:00\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Renaissance Learning call\",\n" +
                "            \"start\": \"2016-03-02T16:00:00-06:00\",\n" +
                "            \"end\": \"2016-03-02T17:00:00-06:00\",\n" +
                "            \"color\": \"#dbadff\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Dr. Lusth meeting\",\n" +
                "            \"start\": \"2016-03-03T14:15:00-06:00\",\n" +
                "            \"end\": \"2016-03-03T15:15:00-06:00\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Dr. Gray meeting\",\n" +
                "            \"start\": \"2016-03-11T08:30:00-06:00\",\n" +
                "            \"end\": \"2016-03-11T09:30:00-06:00\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to Atlanta\",\n" +
                "            \"start\": \"2016-03-11T16:21:00-06:00\",\n" +
                "            \"end\": \"2016-03-11T17:31:00-06:00\",\n" +
                "            \"location\": \"Birmingham BHM\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to Appleton\",\n" +
                "            \"start\": \"2016-03-11T18:40:00-06:00\",\n" +
                "            \"end\": \"2016-03-11T20:53:00-06:00\",\n" +
                "            \"location\": \"Atlanta ATL\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to Liberia\",\n" +
                "            \"start\": \"2016-03-11T16:58:00-06:00\",\n" +
                "            \"end\": \"2016-03-11T20:50:00-06:00\",\n" +
                "            \"location\": \"Atlanta ATL\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to Atlanta\",\n" +
                "            \"start\": \"2016-03-20T08:00:00-05:00\",\n" +
                "            \"end\": \"2016-03-20T11:57:00-05:00\",\n" +
                "            \"location\": \"Liberia LIR\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to Detroit\",\n" +
                "            \"start\": \"2016-03-20T10:55:00-05:00\",\n" +
                "            \"end\": \"2016-03-20T12:09:00-05:00\",\n" +
                "            \"location\": \"Appleton ATW\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Flight to Birmingham\",\n" +
                "            \"start\": \"2016-03-20T13:35:00-05:00\",\n" +
                "            \"end\": \"2016-03-20T15:35:00-05:00\",\n" +
                "            \"location\": \"Detroit DTW\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Award dinner\",\n" +
                "            \"start\": \"2016-04-04T18:00:00-05:00\",\n" +
                "            \"end\": \"2016-04-04T19:00:00-05:00\",\n" +
                "            \"location\": \"The University Club, 421 Queen City Ave, Tuscaloosa, AL 35401, United States\",\n" +
                "            \"color\": \"#ff887c\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Lwc mentorship\",\n" +
                "            \"start\": \"2016-03-24T11:00:00-05:00\",\n" +
                "            \"end\": \"2016-03-24T11:30:00-05:00\",\n" +
                "            \"color\": \"#ff887c\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Capstone\",\n" +
                "            \"start\": \"2016-03-25T17:00:00-05:00\",\n" +
                "            \"end\": \"2016-03-25T18:00:00-05:00\",\n" +
                "            \"color\": \"#ff887c\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Poster presentation\",\n" +
                "            \"start\": \"2016-03-30T08:00:00-05:00\",\n" +
                "            \"end\": \"2016-03-30T11:00:00-05:00\",\n" +
                "            \"color\": \"#ff887c\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Hackathon planning\",\n" +
                "            \"start\": \"2016-03-30T11:30:00-05:00\",\n" +
                "            \"end\": \"2016-03-30T13:30:00-05:00\",\n" +
                "            \"color\": \"#ffb878\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Nath meeting\",\n" +
                "            \"start\": \"2016-03-28T15:30:00-05:00\",\n" +
                "            \"end\": \"2016-03-28T16:30:00-05:00\",\n" +
                "            \"color\": \"#ff887c\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Dr. Gray meeting\",\n" +
                "            \"start\": \"2016-04-01T16:30:00-05:00\",\n" +
                "            \"end\": \"2016-04-01T17:30:00-05:00\",\n" +
                "            \"color\": \"#ffb878\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Jon Byom call\",\n" +
                "            \"start\": \"2016-03-30T15:30:00-05:00\",\n" +
                "            \"end\": \"2016-03-30T16:30:00-05:00\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Play\",\n" +
                "            \"start\": \"2016-04-20T19:30:00-05:00\",\n" +
                "            \"end\": \"2016-04-20T20:30:00-05:00\",\n" +
                "            \"location\": \"Marian Gallaway Theatre, Stadium Dr, Tuscaloosa, AL 35401, United States\",\n" +
                "            \"color\": \"#ff887c\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Grabeal\",\n" +
                "            \"start\": \"2016-04-04T11:00:00-05:00\",\n" +
                "            \"end\": \"2016-04-04T12:00:00-05:00\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Google talk\",\n" +
                "            \"start\": \"2016-04-08T14:00:00-05:00\",\n" +
                "            \"end\": \"2016-04-08T15:00:00-05:00\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Pick. Up stuff\",\n" +
                "            \"start\": \"2016-04-05T16:15:00-05:00\",\n" +
                "            \"end\": \"2016-04-05T17:15:00-05:00\",\n" +
                "            \"color\": \"#ffb878\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Startup Weekend Tuscaloosa\",\n" +
                "            \"start\": \"2016-04-22T18:30:00-05:00\",\n" +
                "            \"end\": \"2016-04-24T21:00:00-05:00\",\n" +
                "            \"location\": \"The University of Alabama - South Engineering Research Center Building (SERC), 245 7TH Ave., Tuscaloosa, AL, US, 35401\",\n" +
                "            \"color\": \"#a4bdfc\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Demo\",\n" +
                "            \"start\": \"2016-04-15T11:00:00-05:00\",\n" +
                "            \"end\": \"2016-04-15T12:00:00-05:00\",\n" +
                "            \"color\": \"#ffb878\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"privacyLevel\": \"HIDDEN\",\n" +
                "            \"rsvpable\": false,\n" +
                "            \"attendees\": [],\n" +
                "            \"tile\": \"Prog lang demo\",\n" +
                "            \"start\": \"2016-04-20T09:00:00-05:00\",\n" +
                "            \"end\": \"2016-04-20T10:00:00-05:00\",\n" +
                "            \"color\": \"#dc2127\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"calendar\": {\n" +
                "    \"id\": \"ef03dabb-f4c1-4323-ac8f-0082a81553be\",\n" +
                "    \"events\": [\n" +
                "      {\n" +
                "        \"id\": \"f2e57c69-d696-4dd1-a0a0-78a0e0cc11ea\",\n" +
                "        \"privacyLevel\": \"HIDDEN\",\n" +
                "        \"rsvpable\": false,\n" +
                "        \"attendees\": [],\n" +
                "        \"tile\": \"test\",\n" +
                "        \"start\": \"2016-04-16T02:00:00-05:00\",\n" +
                "        \"end\": \"2016-04-16T03:00:00-05:00\",\n" +
                "        \"color\": \"#a4bdfc\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
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
            e.privacyLevel = PrivacyLevel.FRIENDS;
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
