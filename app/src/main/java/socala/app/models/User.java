package socala.app.models;

public class User {
    private String mUsername;
    private String mOAuthToken;
    private String mEmail;
    private Calendar mCalendar;
    private User[] mFriends;

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getOAuthToken() {
        return mOAuthToken;
    }

    public void setOAuthToken(String mOAuthToken) {
        this.mOAuthToken = mOAuthToken;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public Calendar getCalendar() {
        return mCalendar;
    }

    public void setCalendar(Calendar mCalendar) {
        this.mCalendar = mCalendar;
    }

    public User[] getFriends() {
        return mFriends;
    }

    public void setFriends(User[] mFriends) {
        this.mFriends = mFriends;
    }
}
