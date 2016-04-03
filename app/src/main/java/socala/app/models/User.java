package socala.app.models;

import android.content.res.Resources;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class User {
    public String id;
    public String displayName;
    public String oauthToken;
    public String email;
    public SocalaCalendar calendar;
    public List<User> friends;

    public User() {
        friends = new ArrayList<>();
    }

    public User getFriend(String id) {
        for (User friend : friends) {
            if (friend.id.equals(id)) {
                return friend;
            }
        }

        throw new Resources.NotFoundException("Could not find friend with " + id + " in user");
    }

}
