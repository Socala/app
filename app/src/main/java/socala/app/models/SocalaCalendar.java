package socala.app.models;

import android.content.res.Resources;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class SocalaCalendar {
    public String id;
    public List<Event> events;

    public SocalaCalendar() {
        this.events = new ArrayList<>();
    }

    public Event getEvent(String eventId) {
        for (Event event : events) {
            if (event.id.equals(eventId)) {
               return event;
            }
        }

        throw new Resources.NotFoundException("Could not find Event");
    }

    public boolean hasEvent(String id) {
        for (Event event : events) {
            if (event.id.equals(id)) {
                return true;
            }
        }

        return false;
    }

    public void upsertEvent(Event e) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).id.equals(e.id)) {
                events.set(i, e);
                return;
            }
        }

        events.add(e);
    }

    public void removeEvent(String id) {
        int index = -1;
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).id.equals(id)) {
                index = i;
            }
        }

        if (index != -1) {
            events.remove(index);
        }
    }
}
