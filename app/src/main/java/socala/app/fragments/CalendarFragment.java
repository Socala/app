package socala.app.fragments;


import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import socala.app.R;
import socala.app.activities.EventDetailsActivity;
import socala.app.contexts.AppContext;
import socala.app.dialogs.CalendarOptionsDialog;
import socala.app.models.Event;
import socala.app.models.SocalaWeekViewEvent;
import socala.app.models.User;

public class CalendarFragment extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, CalendarOptionsDialog.CalendarsChangedListener, WeekView.EmptyViewClickListener {

    protected static final int EVENT_DETAILS_INTENT = 1;

    @Bind(R.id.weekView) WeekView weekView;

    protected final AppContext appContext = AppContext.getInstance();
    private List<String> selectedIds;

    public CalendarFragment() { }

    @OnClick(R.id.fab)
    public void onFabClick(FloatingActionButton fab) {
        startEventDetailsActivity((Event) null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.calendar_options, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.calendar_options) {
            createCalendarOptionsDialog();
            return true;
        } else if (item.getItemId() == R.id.to_today) {
            weekView.goToToday();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            selectedIds = Parcels.unwrap(savedInstanceState.getParcelable("selectedIds"));
        } else {
            selectedIds = new ArrayList<>();
            selectedIds.add(appContext.getUser().id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        ButterKnife.bind(this, view);

        weekView.setOnEventClickListener(this);
        weekView.setMonthChangeListener(this);
        weekView.setEmptyViewClickListener(this);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        SocalaWeekViewEvent socalaEvent = (SocalaWeekViewEvent) event;

        startEventDetailsActivity(socalaEvent.event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CalendarFragment.EVENT_DETAILS_INTENT) {
            refresh();
        }
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return this.getEventsForMonth(newYear, newMonth);
    }

    public void refresh() {
        weekView.notifyDatasetChanged();
    }

    @Override
    public void onCalendarsChanged(List<String> ids) {
        selectedIds = ids;
        weekView.notifyDatasetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("selectedIds", Parcels.wrap(selectedIds));
    }

    @Override
    public void onEmptyViewClicked(Calendar time) {
        startEventDetailsActivity(time);
    }

    private List<? extends WeekViewEvent> getEventsForMonth(int newYear, int newMonth) {
        User u = appContext.getUser();

        if (u == null || u.calendar == null) {
           return new ArrayList<>();
        }

        List<Event> events = getSelectedCalendarEvents();

        java.util.Calendar calendar = Calendar.getInstance();
        calendar.set(newYear, newMonth, 1);

        Calendar startOfRange = (Calendar) calendar.clone();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Calendar endOfRange = (Calendar) calendar.clone();

        List<WeekViewEvent> filteredEvents = new ArrayList<>();

        for (Event e : events) {
            if (startOfRange.before(e.start)
                    && endOfRange.after(e.start)) {
                filteredEvents.add(e.toWeekViewEvent());

            }
        }

        return filteredEvents;
    }

    private List<Event> getSelectedCalendarEvents() {
        List<User> users = calendarOptions();

        List<Event> events = new ArrayList<>();

        for (User user : users) {
            if (selectedIds.contains(user.id)) {
                events.addAll(user.calendar.events);
            }
        }

        return events;
    }

    private void startEventDetailsActivity(Calendar startTime) {
        Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
        intent.putExtra("startTime", startTime);
        Calendar endTime = (Calendar) startTime.clone();

        endTime.add(Calendar.HOUR_OF_DAY, 1);
        intent.putExtra("endTime", endTime);

        startActivityForResult(intent, CalendarFragment.EVENT_DETAILS_INTENT);
    }

    private void startEventDetailsActivity(Event event) {
        Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
        intent.putExtra("event", Parcels.wrap(event));

        startActivityForResult(intent, CalendarFragment.EVENT_DETAILS_INTENT);
    }

    private List<User> calendarOptions() {
        List<User> users = new ArrayList<>();
        users.addAll(appContext.getUser().friends);
        users.addAll(appContext.getCachedUsers());
        users.add(appContext.getUser());

        return users;
    }

    private void createCalendarOptionsDialog() {
        DialogFragment dialog = CalendarOptionsDialog.newInstance(calendarOptions(), selectedIds);
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "CalendarOptionsDialog");
    }

}
