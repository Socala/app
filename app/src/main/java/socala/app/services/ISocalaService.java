package socala.app.services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import socala.app.models.SocalaCalendar;
import socala.app.models.CommonTimeOptions;
import socala.app.models.Event;
import socala.app.models.User;

public interface ISocalaService {

//    String ENDPOINT = "http://www.backend.com/";

    @POST("user/")
    Call<User> addUser(@Body User user);

    @DELETE("user/{id}")
    Call<Boolean> removeUser(@Header("Authorization") String token, @Path("id") String userId);

    @GET("user/")
    Call<User> getUser(@Header("Authorization") String token);

    @GET("user/")
    Call<User> getUser(@Header("Authorization") String token, @Query("email") String email);

    @PUT("user/{id}")
    Call<User> updateUser(@Header("Authorization") String token, @Path("id") String userId, @Body User user);

    @POST("event/")
    Call<Event> addEvent(@Header("Authorization") String token, @Body Event event);

    @DELETE("event/{id}")
    Call<Event> removeEvent(@Header("Authorization") String token, @Path("id") String eventId);

    @PUT("event/{id}")
    Call<Event> updateEvent(@Header("Authorization") String token, @Path("id") String eventId, @Body Event event);

    @GET("event/{id}")
    Call<SocalaCalendar> getCalendar(@Header("Authorization") String token, @Path("id") String userId);

    @POST("events/")
    Call<SocalaCalendar[]> getCalendars(@Header("Authorization") String token, @Body String[] userIds);

    @GET("user/{id}/rsvp/{eventId}")
    Call<Boolean> rsvp(@Header("Authorization") String token, @Path("eventId") String eventId, @Path("id") String eventUserId);

    @GET("user/{id}/unrsvp/{eventId}")
    Call<Boolean> unrsvp(@Header("Authorization") String token, @Path("eventId") String eventId, @Path("id") String eventUserId);

    @GET("events/commonTimes")
    Call<SocalaCalendar> getCommonTimes(@Header("Authorization") String token, @Part("userId") String userId, @Part("friendIds") String[] friendIds, @Part("options") CommonTimeOptions options);

    @GET("user/friend/add")
    Call<User> addFriend(@Header("Authorizaiton") String token, @Query("email") String email);

    @GET("user/friend/remove")
    Call<Boolean> removeFriend(@Header("Authorization") String token, @Query("email") String friendId);
}
