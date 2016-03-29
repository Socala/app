package socala.app.models;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String displayName;
    private String oauthToken;
    private String email;
    private Calendar calendar;
    private List<User> friends;

    public User() {
        friends = new ArrayList<>();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getOAuthToken() {
        return oauthToken;
    }

    public void setOAuthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public User getFriend(String id) {
        for (int i = 0; i < friends.size(); i++) {
            if (friends.get(i).getId().equals(id)) {
                return friends.get(i);
            }
        }

        throw new Resources.NotFoundException("Could not find friend with " + id + " in user");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
