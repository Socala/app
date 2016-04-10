package socala.app.contexts;

import java.util.ArrayList;
import java.util.List;

import socala.app.models.User;

public class AppContext {

    private static AppContext appContext = null;
    private User user;
    private List<User> cachedUsers;

    private AppContext() {
        this.user = null;
        this.cachedUsers = new ArrayList<>();
    }

    public static AppContext getInstance() {
        if (appContext == null) {
            appContext = new AppContext();
        }

        return appContext;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getCachedUsers() {
        return cachedUsers;
    }
}
