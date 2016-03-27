package socala.app.models;

public class Calendar {
    private String mId;
    private Event[] mEvents;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public Event[] getEvents() {
        return mEvents;
    }

    public void setEvents(Event[] mEvents) {
        this.mEvents = mEvents;
    }
}
