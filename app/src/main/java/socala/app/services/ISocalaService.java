package socala.app.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import socala.app.models.Event;
import socala.app.models.User;

public interface ISocalaService {

//    String ENDPOINT = "http://www.backend.com/";

    @GET("users/signin")
    Call<User> signIn(@Header("Authorization") String token);

    @GET("users/")
    Call<User> getUser(@Query("email") String email);

    @POST("events/")
    Call<Event> addEvent(@Body Event event);

    @DELETE("events/{id}")
    Call<Boolean> removeEvent(@Path("id") String eventId);

    @PUT("events/")
    Call<Boolean> updateEvent(@Body Event event);

    @GET("users/{id}/rsvp/{eventId}")
    Call<Boolean> rsvp(@Path("eventId") String eventId, @Path("id") String eventUserId);

    @GET("users/{id}/unrsvp/{eventId}")
    Call<Boolean> unrsvp(@Path("eventId") String eventId, @Path("id") String eventUserId);

    @GET("users/friends/add")
    Call<User> addFriend(@Query("email") String email);

    @GET("users/friends/remove")
    Call<Boolean> removeFriend(@Query("email") String friendId);
}
