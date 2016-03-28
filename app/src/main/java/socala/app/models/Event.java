package socala.app.models;

public class Event {
    private String id;
    private PrivacyLevel privacyLevel;
    private boolean rsvpable;

    // TODO: Add more properties. Need to discuss with CJ about this

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PrivacyLevel getPrivacyLevel() {
        return privacyLevel;
    }

    public void setPrivacyLevel(PrivacyLevel privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    public boolean isRsvpable() {
        return rsvpable;
    }

    public void setRsvpable(boolean rsvpable) {
        this.rsvpable = rsvpable;
    }
}
