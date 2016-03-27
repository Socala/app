package socala.app.models;

public class Event {
    private String mId;
    private PrivacyLevel mPrivacyLevel;
    private boolean mRsvpable;

    // TODO: Add more properties. Need to discuss with CJ about this

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public PrivacyLevel getPrivacyLevel() {
        return mPrivacyLevel;
    }

    public void setPrivacyLevel(PrivacyLevel mPrivacyLevel) {
        this.mPrivacyLevel = mPrivacyLevel;
    }

    public boolean isRsvpable() {
        return mRsvpable;
    }

    public void setRsvpable(boolean mRsvpable) {
        this.mRsvpable = mRsvpable;
    }
}
