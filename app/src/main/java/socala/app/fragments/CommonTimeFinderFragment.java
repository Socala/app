package socala.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import socala.app.R;
import socala.app.activities.CommonTimeCalendarActivity;
import socala.app.contexts.AppContext;
import socala.app.dialogs.CalendarOptionsDialog;
import socala.app.dialogs.DateRangePickerDialogs;
import socala.app.models.EventDuration;
import socala.app.models.User;

public class CommonTimeFinderFragment extends Fragment implements CalendarOptionsDialog.CalendarsChangedListener {

    @Bind(R.id.startDate) TextView startDateTextView;
    @Bind(R.id.endDate) TextView endDateTextView;
    @Bind(R.id.startTime) TextView startTimeTextView;
    @Bind(R.id.endTime) TextView endTimeTextView;
    @Bind(R.id.durationSpinner) Spinner durationSpinner;

    private DateRangePickerDialogs dateRangePickerDialogs;
    private Calendar start;
    private Calendar end;
    private List<String> selectedIds;
    private final AppContext appContext = AppContext.getInstance();

    public CommonTimeFinderFragment() {
        // Required empty public constructor
    }

    public static CommonTimeFinderFragment newInstance() {
        CommonTimeFinderFragment fragment = new CommonTimeFinderFragment();
        return fragment;
    }

    @OnClick({R.id.startDate, R.id.endDate, R.id.endTime, R.id.startTime, R.id.computeButton, R.id.selectButton})
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.startTime) {
            dateRangePickerDialogs.showStartTimePicker();
        } else if (viewId == R.id.endTime) {
            dateRangePickerDialogs.showEndTimePicker();
        } else if (viewId == R.id.startDate) {
            dateRangePickerDialogs.showStartDatePicker();
        } else if (viewId == R.id.endDate) {
            dateRangePickerDialogs.showEndDatePicker();
        } else if (viewId == R.id.computeButton) {
            computeCommonTimes();
        } else if (viewId == R.id.selectButton) {
            createCalendarOptionsDialog();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_time_finder, container, false);

        ButterKnife.bind(this, view);

        selectedIds = new ArrayList<>();
        selectedIds.add(appContext.getUser().id);
        populateFields();

        return view;
    }

    @Override
    public void onCalendarsChanged(List<String> ids) {
        selectedIds = ids;
    }

    private void populateFields() {

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        end.add(Calendar.DATE, 5);

        dateRangePickerDialogs = new DateRangePickerDialogs(getContext(),
                start, end,
                startTimeTextView, endTimeTextView,
                startDateTextView, endDateTextView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, durationOptions());

        durationSpinner.setAdapter(adapter);
    }

    private void computeCommonTimes() {
        // TODO: Validate before computing

        Intent intent = new Intent(getActivity(), CommonTimeCalendarActivity.class);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(selectedIds);

        intent.putStringArrayListExtra("selectedIds", arrayList);
        intent.putExtra("start", start);
        intent.putExtra("end", end);
        intent.putExtra("duration", durationSpinner.getSelectedItem().toString());

        startActivity(intent);
    }

    private String[] durationOptions() {
        return new String[]{
                EventDuration.FIFTEEN_MINUTES,
                EventDuration.THIRTY_MINUTES,
                EventDuration.FORTY_FIVE_MINUTES,
                EventDuration.ONE_HOUR,
                EventDuration.TWO_HOURS
        };
    }

    private void createCalendarOptionsDialog() {
        DialogFragment dialog = CalendarOptionsDialog.newInstance(calendarOptions(), selectedIds);
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "CalendarOptionsDialog");
    }

    private List<User> calendarOptions() {
        List<User> users = new ArrayList<>();
        users.addAll(appContext.getUser().friends);
        users.addAll(appContext.getCachedUsers());

        return users;
    }
}
