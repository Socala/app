package socala.app.contexts;

import socala.app.models.User;
import socala.app.services.DataService;

public class AppContext {

    private static AppContext sAppContext = null;

    private DataService mDataService;
    private User mUser;

    private AppContext(User user) {
        this.mDataService = new DataService(user.getOAuthToken());
        this.mUser = null;
    }

    public static AppContext getInstance(User user) {
        if (sAppContext == null) {
            sAppContext = new AppContext(user);
        }

        return sAppContext;
    }

    public User getUser() {
        return mUser;
    }

    public DataService getDataService() {
        return mDataService;
    }
}
