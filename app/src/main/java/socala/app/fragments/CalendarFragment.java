package socala.app.fragments;


import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import socala.app.R;

public class CalendarFragment extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener {

    @Bind(R.id.weekView) WeekView weekView;

    public CalendarFragment() {
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

    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return new ArrayList<WeekViewEvent>();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }
}
