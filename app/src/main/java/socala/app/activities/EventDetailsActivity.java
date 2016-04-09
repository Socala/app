package socala.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import socala.app.R;
import socala.app.contexts.AppContext;
import socala.app.dialogs.DateRangePickerDialogs;
import socala.app.models.Event;
import socala.app.services.ISocalaService;
import socala.app.services.SocalaClient;

public class EventDetailsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.summary) EditText summaryEditText;
    @Bind(R.id.rsvpableCheckBox) CheckBox rsvpableCheckBox;
    @Bind(R.id.startDate) TextView startDateTextView;
    @Bind(R.id.endDate) TextView endDateTextView;
    @Bind(R.id.startTime) TextView startTimeTextView;
    @Bind(R.id.endTime) TextView endTimeTextView;
    @Bind(R.id.saveButton) Button saveButton;

    private final AppContext appContext = AppContext.getInstance();
    private final ISocalaService service = SocalaClient.getClient();
    private Event event;
    private boolean editable;
    private DateRangePickerDialogs dateRangePickerDialogs;

    @OnClick({R.id.startDate, R.id.endDate, R.id.endTime, R.id.startTime, R.id.saveButton})
    public void onDateTimeClick(TextView view) {
        int viewId = view.getId();

        if (viewId == R.id.startDate) {
            dateRangePickerDialogs.showStartDatePicker();
        } else if (viewId == R.id.endDate) {
            dateRangePickerDialogs.showEndDatePicker();
        } else if (viewId == R.id.endTime) {
            dateRangePickerDialogs.showEndTimePicker();
        } else if (viewId == R.id.startTime) {
            dateRangePickerDialogs.showStartTimePicker();
        } else if (viewId == R.id.saveButton) {
            save();
        }
    }

    @OnCheckedChanged(R.id.rsvpableCheckBox)
    public void OnRsvpableClick(CheckBox view, boolean isChecked) {
        event.rsvpable = isChecked;
    }

    @OnTextChanged(R.id.summary)
    public void OnTitleChange(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        event.title = text.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        ButterKnife.bind(this);

        event = Parcels.unwrap(getIntent().getParcelableExtra("event"));

        if (event == null) {
            event = Event.getInstance();
            setEditability(true);
        } else {
            setEditability(appContext.getUser().calendar.hasEvent(event.id));
        }

        this.populateFields();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void populateFields() {

        summaryEditText.setText(event.title);
        rsvpableCheckBox.setChecked(event.rsvpable);

        dateRangePickerDialogs = new DateRangePickerDialogs(this,
                event.start, event.end,
                startTimeTextView, endTimeTextView,
                startDateTextView, endDateTextView);
    }

    private void setEditability(boolean value) {
        editable = value;
        setEditability(summaryEditText, value);
        setEditability(saveButton, value);
        setEditability(rsvpableCheckBox, value);
        setEditability(startDateTextView, value);
        setEditability(endDateTextView, value);
        setEditability(startTimeTextView, value);
        setEditability(endTimeTextView, value);

        saveButton.setEnabled(value);
    }

    private void setEditability(View v, boolean value) {
        v.setFocusable(value);
        v.setClickable(value);
    }

    private void save() {
        if(!validateFields() || !editable) {
            return;
        }

        Call<Event> call;

        if (event.id.equals("")) {
            call = service.addEvent(appContext.getUser().oauthToken, event);
        } else {
            call = service.updateEvent(appContext.getUser().oauthToken, event.id, event);
        }

        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {

                Event e = response.body();

                appContext.getUser().calendar.upsertEvent(e);
                finish();
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                makeToast("Failed to save event!");
            }
        });
    }

    private boolean validateFields() {
        if (event.start.getTime().after(event.end.getTime())) {
            makeToast("Cannot have start date after end date!");
            return false;
        } else if (event.title.equals("")) {
            makeToast("Cannot have empty event title");
            return false;
        }

        return true;
    }

    private void makeToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
