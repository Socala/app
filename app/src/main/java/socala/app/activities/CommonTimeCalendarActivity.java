package socala.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import socala.app.R;
import socala.app.contexts.AppContext;
import socala.app.models.Event;
import socala.app.models.EventDuration;
import socala.app.models.User;

public class CommonTimeCalendarActivity extends AppCompatActivity implements WeekView.EmptyViewClickListener,
        MonthLoader.MonthChangeListener {
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.weekView) WeekView weekView;

    private List<User> users;
    private final AppContext appContext = AppContext.getInstance();
    private Calendar start;
    private Calendar end;
    private long duration;

    @Override
    public void onEmptyViewClicked(Calendar time) {
        //Start a new event
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return createBlankEvents(newYear, newMonth);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_time_calendar);

        ButterKnife.bind(this);

        extractExtras();

        weekView.setEmptyViewClickListener(this);
        weekView.setMonthChangeListener(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void extractExtras() {
        Intent intent = getIntent();
        start = (Calendar) intent.getSerializableExtra("start");
        end = (Calendar) intent.getSerializableExtra("end");
        duration = getDuration(intent.getStringExtra("duration"));

        ArrayList<String> selectedIds = intent.getStringArrayListExtra("selectedIds");

        getUsers(selectedIds);
    }

    private long getDuration(String duration) {
        return EventDuration.getMilliseconds(duration);
    }

    private void getUsers(List<String> selectedIds) {
        List<User> allUsers = new ArrayList<>();
        allUsers.add(appContext.getUser());
        allUsers.addAll(appContext.getCachedUsers());
        allUsers.addAll(appContext.getUser().friends);

        users = new ArrayList<>();
        for (User user : allUsers) {
            if (selectedIds.contains(user.id)) {
                users.add(user);
            }
        }
    }

    private List<WeekViewEvent> createBlankEvents(int newYear, int newMonth) {

        List<Event> events = new ArrayList<>();

        Calendar startOfRange = Calendar.getInstance();
        startOfRange.set(newYear, newMonth, 1);
        Calendar endOfRange = (Calendar) startOfRange.clone();
        endOfRange.set(Calendar.DAY_OF_MONTH, endOfRange.getActualMaximum(Calendar.DAY_OF_MONTH));

        // If only I had Linq :(
        for (User user : users) {
            for (Event event : user.calendar.events) {
                if ( startOfRange.before(event.end) && endOfRange.after(event.start) &&
                        start.before(event.end) && end.after(event.start)
                        ) {
                    events.add(event);
                }
            }
        }

        Collections.sort(events);

        List<WeekViewEvent> blankEvents = new ArrayList<>();

        if (events.size() == 0) {
            return blankEvents;
        }

        Event prevEvent = events.get(0);
        Calendar startOfConflict = null;

        for (Event event : events) {
            if (startOfConflict == null) {
                startOfConflict = event.start;
            } else if (prevEvent.end.before(event.start) && (event.start.getTimeInMillis() - prevEvent.end.getTimeInMillis()) >= duration) {
                blankEvents.add(createBlankEvent(startOfConflict, prevEvent.end));
                startOfConflict = event.start;
            }

            prevEvent = event;
        }

        blankEvents.add(createBlankEvent(startOfConflict, prevEvent.end));

        return blankEvents;
    }

    private WeekViewEvent createBlankEvent(Calendar start, Calendar end) {
        WeekViewEvent event = new WeekViewEvent();
        event.setName("Unavailable");
        event.setColor(Color.parseColor("#000000"));
        event.setStartTime(start);
        event.setEndTime(end);

        return event;
    }
}
