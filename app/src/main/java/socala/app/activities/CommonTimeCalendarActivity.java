package socala.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

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

    public final int EVENT_DETAILS_INTENT = 1;

    private List<User> users;
    private final AppContext appContext = AppContext.getInstance();
    private Calendar start;
    private Calendar end;
    private Calendar startTimeOfDay;
    private Calendar endTimeOfDay;
    private long duration;

    @Override
    public void onEmptyViewClicked(Calendar time) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtra("startTime", time);
        Calendar endTime = (Calendar) time.clone();
        endTime.setTimeInMillis(endTime.getTimeInMillis() + duration);
        intent.putExtra("endTime", endTime);

        startActivityForResult(intent, EVENT_DETAILS_INTENT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == EVENT_DETAILS_INTENT) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
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

        setResult(RESULT_CANCELED);

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
        startTimeOfDay = (Calendar) intent.getSerializableExtra("startTimeOfDay");
        endTimeOfDay = (Calendar) intent.getSerializableExtra("endTimeOfDay");
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

        if (startOfRange.after(end)) {
            return generateFillerMonth(newYear, newMonth);
        }

        // If only I had Linq :(
        for (User user : users) {
            for (Event event : user.calendar.events) {
                if (isBeforeTimeOfDay(startTimeOfDay, event.end) && isAfterTimeOfDay(endTimeOfDay, event.start) &&
                        startOfRange.before(event.end) && endOfRange.after(event.start) &&
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
                Calendar blankEventStart = adjustStart(startOfConflict);
                Calendar blankEventEnd = adjustEnd(prevEvent.end);

                blankEvents.add(createBlankEvent(blankEventStart, blankEventEnd));
                startOfConflict = event.start;
            }

            prevEvent = event;
        }

        Calendar blankEventStart = adjustStart(startOfConflict);
        Calendar blankEventEnd = adjustEnd(prevEvent.end);

        blankEvents.add(createBlankEvent(blankEventStart, blankEventEnd));

        blankEvents.addAll(generateFillerBlankEvents(newYear, newMonth));

        return blankEvents;
    }

    private List<WeekViewEvent> generateFillerMonth(int year, int month) {

        Calendar startOfMonth = Calendar.getInstance();
        startOfMonth.set(year, month, 1, 0,0);

        Calendar endOfMonth = (Calendar) startOfMonth.clone();
        endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
        endOfMonth.set(Calendar.HOUR_OF_DAY, endOfMonth.getActualMaximum(Calendar.HOUR_OF_DAY));
        endOfMonth.set(Calendar.MINUTE, endOfMonth.getActualMaximum(Calendar.MINUTE));
        endOfMonth.set(Calendar.SECOND, endOfMonth.getActualMaximum(Calendar.SECOND));

        List<WeekViewEvent> blankEvents = new ArrayList<>();

        blankEvents.add(createBlankEvent(startOfMonth, endOfMonth));

        return blankEvents;
    }

    // THIS CODE IS REALLY BAD... probably fails to handle some edge cases
    private List<WeekViewEvent> generateFillerBlankEvents(int year, int month) {
        List<WeekViewEvent> blankEvents = new ArrayList<>();

        Calendar startOfMonth = Calendar.getInstance();
        startOfMonth.set(year, month, 1, 0, 0);

        Calendar endOfMonth = (Calendar) startOfMonth.clone();
        endOfMonth.set(Calendar.DAY_OF_MONTH, startOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
        endOfMonth.set(Calendar.HOUR_OF_DAY, startOfMonth.getActualMaximum(Calendar.HOUR_OF_DAY));
        endOfMonth.set(Calendar.MINUTE, startOfMonth.getActualMaximum(Calendar.MINUTE));
        endOfMonth.set(Calendar.SECOND, startOfMonth.getActualMaximum(Calendar.SECOND));

        Calendar startBlank = (Calendar) startOfMonth.clone();
        Calendar endBlank = Calendar.getInstance();


        boolean beforeMonth = false;

        // either start is before the start of the month or after
        if (start.before(startBlank)) {
            endBlank.set(year, month, 1, startTimeOfDay.get(Calendar.HOUR_OF_DAY) ,startTimeOfDay.get(Calendar.MINUTE), 0);
            createBlankEvent((Calendar) startBlank.clone(), (Calendar) endBlank.clone());
            beforeMonth = true;
        } else {
            Calendar startClone = (Calendar) start.clone();
            startClone.add(Calendar.MINUTE, -1);
            blankEvents.add(createBlankEvent((Calendar) startBlank.clone(), startClone));
        }

        // start was before month or end time of day is before the start
        if (beforeMonth || isBeforeTimeOfDay(endTimeOfDay, start)) {
            startBlank.set(year, month, 1, endTimeOfDay.get(Calendar.HOUR_OF_DAY), endTimeOfDay.get(Calendar.MINUTE), 0);
            endBlank.set(year, month, 2, startTimeOfDay.get(Calendar.HOUR_OF_DAY), startTimeOfDay.get(Calendar.MINUTE), 0);
        } else {
            startBlank = (Calendar) start.clone();
            startBlank.set(Calendar.HOUR_OF_DAY, endTimeOfDay.get(Calendar.HOUR_OF_DAY));
            startBlank.set(Calendar.MINUTE, endTimeOfDay.get(Calendar.MINUTE));
            endBlank.set(year, month, startBlank.get(Calendar.DAY_OF_MONTH) + 1, startTimeOfDay.get(Calendar.HOUR_OF_DAY), startTimeOfDay.get(Calendar.MINUTE), 0);
        }

        while (endBlank.get(Calendar.MONTH) == startBlank.get(Calendar.MONTH)) {

            if (startBlank.after(end)) {
                blankEvents.add(createBlankEvent((Calendar) end.clone(), (Calendar) endOfMonth.clone()));
                return blankEvents;
            }

            if (endBlank.after(end)) {
                blankEvents.add(createBlankEvent((Calendar) startBlank.clone(), (Calendar) endOfMonth.clone()));
                return blankEvents;
            }

            blankEvents.add(createBlankEvent((Calendar) startBlank.clone(), (Calendar) endBlank.clone()));
            endBlank.add(Calendar.DAY_OF_MONTH, 1);
            startBlank.add(Calendar.DAY_OF_MONTH, 1);
        }

        if (startBlank.after(end)) {
            blankEvents.add(createBlankEvent((Calendar) end.clone(), (Calendar) endOfMonth.clone()));
        } else {
            blankEvents.add(createBlankEvent((Calendar) startBlank.clone(), (Calendar) endOfMonth.clone()));
        }

        return blankEvents;
    }

    private Calendar adjustStart(Calendar calendar) {
        Calendar startTimeOfDayForToday = (Calendar) startTimeOfDay.clone();
        startTimeOfDayForToday.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        startTimeOfDayForToday.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        startTimeOfDayForToday.add(Calendar.MINUTE, 1);

        if (startTimeOfDayForToday.before(start)) {
            startTimeOfDayForToday.set(Calendar.HOUR_OF_DAY, start.get(Calendar.HOUR_OF_DAY));
            startTimeOfDayForToday.set(Calendar.MINUTE, start.get(Calendar.MINUTE) + 1);
        }

        if (isAfterTimeOfDay(startTimeOfDayForToday, calendar) || (calendar.getTimeInMillis() - startTimeOfDayForToday.getTimeInMillis()) < duration) {
            return startTimeOfDayForToday;
        }

        return calendar;
    }

    private Calendar adjustEnd(Calendar calendar) {
        Calendar endTimeOfDayForToday = (Calendar) endTimeOfDay.clone();
        endTimeOfDayForToday.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        endTimeOfDayForToday.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        endTimeOfDayForToday.add(Calendar.MINUTE, -1);

        if (endTimeOfDayForToday.after(end)) {
            endTimeOfDayForToday.set(Calendar.HOUR_OF_DAY, end.get(Calendar.HOUR_OF_DAY));
            endTimeOfDayForToday.set(Calendar.MINUTE, end.get(Calendar.MINUTE) - 1);
        }

        if (isBeforeTimeOfDay(endTimeOfDayForToday, calendar) || (endTimeOfDayForToday.getTimeInMillis() - calendar.getTimeInMillis()) < duration) {
            return endTimeOfDayForToday;
        }

        return calendar;
    }

    private boolean isBeforeTimeOfDay(Calendar before, Calendar after) {
        return before.get(Calendar.HOUR_OF_DAY) < after.get(Calendar.HOUR_OF_DAY) ||
                (before.get(Calendar.HOUR_OF_DAY) == after.get(Calendar.HOUR_OF_DAY) &&
                        before.get(Calendar.MINUTE) < after.get(Calendar.MINUTE));
    }

    private boolean isAfterTimeOfDay(Calendar after, Calendar before) {
        return after.get(Calendar.HOUR_OF_DAY) > before.get(Calendar.HOUR_OF_DAY) ||
                (after.get(Calendar.HOUR_OF_DAY) == before.get(Calendar.HOUR_OF_DAY) &&
                        after.get(Calendar.MINUTE) > before.get(Calendar.MINUTE));
    }

    private WeekViewEvent createBlankEvent(Calendar start, Calendar end) {
        WeekViewEvent event = new WeekViewEvent();
        event.setColor(Color.parseColor("#000000"));
        event.setStartTime(start);
        event.setEndTime(end);

        return event;
    }
}
