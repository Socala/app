package socala.app.fragments;


import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
import socala.app.models.Event;
import socala.app.models.SocalaWeekViewEvent;
import socala.app.models.User;

public class CalendarFragment extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener {

    protected static final int EVENT_DETAILS_INTENT = 1;

    @Bind(R.id.weekView) WeekView weekView;

    protected final AppContext appContext = AppContext.getInstance();

    public CalendarFragment() { }

    @OnClick(R.id.fab)
    public void onFabClick(FloatingActionButton fab) {
        startEventDetailsActivity(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        ButterKnife.bind(this, view);

        weekView.setOnEventClickListener(this);
        weekView.setMonthChangeListener(this);
        weekView.setEventLongPressListener(this);

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

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    public void refresh() {
        weekView.notifyDatasetChanged();
    }

    private List<? extends WeekViewEvent> getEventsForMonth(int newYear, int newMonth) {
        User u = appContext.getUser();

        if (u == null || u.calendar == null) {
           return new ArrayList<>();
        }

        List<Event> events = u.calendar.events;

        java.util.Calendar calendar = Calendar.getInstance();
        calendar.set(newYear, newMonth, 1);

        Calendar startOfRange = (Calendar) calendar.clone();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Calendar endOfRange = (Calendar) calendar.clone();

        List<WeekViewEvent> filteredEvents = new ArrayList<>();

        for (Event e : events) {
            if (startOfRange.getTime().before(e.start.getTime())
                    && endOfRange.getTime().after(e.start.getTime())) {
                filteredEvents.add(e.toWeekViewEvent());

            }
        }

        return filteredEvents;
    }

    private void startEventDetailsActivity(Event event) {
        Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
        intent.putExtra("event", Parcels.wrap(event));

        startActivityForResult(intent, CalendarFragment.EVENT_DETAILS_INTENT);
    }
}
