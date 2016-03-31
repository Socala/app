package socala.app.models;

import java.util.Date;

public class Event {
    private String id;
    private PrivacyLevel privacyLevel;
    private boolean rsvpable;
    private String[] attendees;
    private Date start;
    private Date end;
    private String[] recurrence;
    private String recurringEventId;
    private String location;
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String[] getAttendees() {
        return attendees;
    }

    public void setAttendees(String[] attendees) {
        this.attendees = attendees;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String[] getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String[] recurrence) {
        this.recurrence = recurrence;
    }

    public String getRecurringEventId() {
        return recurringEventId;
    }

    public void setRecurringEventId(String recurringEventId) {
        this.recurringEventId = recurringEventId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

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
