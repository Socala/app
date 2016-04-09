package socala.app.models;

import android.graphics.Color;

import com.alamkanak.weekview.WeekViewEvent;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Parcel
public class Event implements Comparable {
    public String id;
    public PrivacyLevel privacyLevel;
    public boolean rsvpable;
    public String title;
    public List<String> attendees;
    public Calendar start;
    public Calendar end;
    public ArrayList recurrence;
    public String recurringEventId;
    public String location;
    public String color;

    public Event() {
        this.recurrence = new ArrayList<>();
        this.attendees = new ArrayList<>();
    }

    public WeekViewEvent toWeekViewEvent() {


        SocalaWeekViewEvent event = new SocalaWeekViewEvent();
        event.event = this;

        event.setName(this.title);
        event.setStartTime(start);
        event.setEndTime(end);
        event.setColor(Color.parseColor(this.color));

        return event;
    }

    public static Event getInstance() {

        Event event = new Event();

        Calendar calendar = Calendar.getInstance();

        event.rsvpable = false;
        event.privacyLevel = PrivacyLevel.FRIEND;
        event.color = "#111111";
        event.start = (Calendar) calendar.clone();
        calendar.add(Calendar.HOUR, 1);
        event.end = (Calendar) calendar.clone();
        event.id = "";
        event.title = "New Event";

        return event;
    }

    @Override
    public int compareTo(Object another) {
        if (!(another instanceof Event)) {
            return 1;
        }

        Event event = (Event) another;

        return this.start.compareTo(event.start);
    }
}
