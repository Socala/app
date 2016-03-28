package socala.app.models;

public class Calendar {
    private String id;
    private Event[] events;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
