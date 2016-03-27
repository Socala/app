package socala.app.services;

import socala.app.models.*;

public class DataService {
    private final String mUrl = "Some string that needs to be in a constants file";
    private String mToken;

    public DataService(String token) {
        this.mToken = token;
    }

    public DataService() {
        this.mToken = null;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }

    public User addUser(User user) {
        throw new UnsupportedOperationException();
    }

    public boolean removeUser(User user) {
        throw new UnsupportedOperationException();
    }

    public User getUser() {
        throw new UnsupportedOperationException();
    }

    public User updateUser(User user) {
        throw new UnsupportedOperationException();
    }

    public Event addEvent(Event event) {
        throw new UnsupportedOperationException();
    }

    public Event removeEvent(Event event) {
        throw new UnsupportedOperationException();
    }

    public Event updateEvent(Event event) {
        throw new UnsupportedOperationException();
    }

    public Calendar getCalendar(String userId) {
        throw new UnsupportedOperationException();
    }

    public Calendar[] getCalendars(String userIds) {
        throw new UnsupportedOperationException();
    }

    public boolean rsvp(String eventId, String eventUserId) {
        throw new UnsupportedOperationException();
    }

    public boolean unrsvp(String eventId, String eventUserId) {
        throw new UnsupportedOperationException();
    }

    public Calendar getCommonTimes(String userId, String[] friendIds, CommonTimeOptions options) {
        throw new UnsupportedOperationException();
    }

    public User addFriend(String email) {
        throw new UnsupportedOperationException();
    }

    public User removeFriend(String friendId) {
        throw new UnsupportedOperationException();
    }
}
