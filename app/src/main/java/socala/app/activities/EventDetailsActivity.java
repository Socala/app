package socala.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import socala.app.R;
import socala.app.contexts.AppContext;
import socala.app.dialogs.DateRangePickerDialogs;
import socala.app.models.Event;
import socala.app.models.PrivacyLevel;
import socala.app.services.ISocalaService;
import socala.app.services.SocalaClient;

public class EventDetailsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.summary) EditText summaryEditText;
    @Bind(R.id.startDate) TextView startDateTextView;
    @Bind(R.id.endDate) TextView endDateTextView;
    @Bind(R.id.startTime) TextView startTimeTextView;
    @Bind(R.id.endTime) TextView endTimeTextView;
    @Bind(R.id.saveButton) Button saveButton;
    @Bind(R.id.deleteButton) Button deleteButton;
    @Bind(R.id.privacyLevelSpinner) Spinner privacyLevelSpinner;

    private final AppContext appContext = AppContext.getInstance();
    private final ISocalaService service = SocalaClient.getClient();
    private Event event;
    private boolean editable;
    private DateRangePickerDialogs dateRangePickerDialogs;

    @OnClick({R.id.startDate, R.id.endDate, R.id.endTime, R.id.startTime, R.id.saveButton, R.id.deleteButton })
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
        } else if (viewId == R.id.deleteButton) {
            delete();
        }
    }

    @OnItemSelected(R.id.privacyLevelSpinner)
    public void OnPrivacyLevelSelected() {
        event.privacyLevel = (PrivacyLevel) privacyLevelSpinner.getSelectedItem();
    }

    @OnTextChanged(R.id.summary)
    public void OnTitleChange(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        event.title = text.toString();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        ButterKnife.bind(this);


        setResult(RESULT_CANCELED);

        event = Parcels.unwrap(getIntent().getParcelableExtra("event"));

        if (event == null) {
            event = Event.getInstance();

            Calendar startTime = (Calendar) getIntent().getSerializableExtra("startTime");
            Calendar endTime = (Calendar) getIntent().getSerializableExtra("endTime");

            if (startTime != null) {
                event.start = startTime;
                event.end = endTime;
            }

            deleteButton.setVisibility(View.INVISIBLE);
            setEditability(true);
        } else {

            boolean isUsersEvent = appContext.getUser().calendar.hasEvent(event.id);

            setEditability(isUsersEvent);

            if (!isUsersEvent) {
                deleteButton.setVisibility(View.INVISIBLE);
                saveButton.setVisibility(View.INVISIBLE);
            }
        }

        this.populateFields();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void populateFields() {

        summaryEditText.setText(event.title);

        ArrayAdapter<PrivacyLevel> privacyLevelAdapter = new ArrayAdapter<PrivacyLevel>(this, android.R.layout.simple_list_item_1, PrivacyLevel.values());
        privacyLevelSpinner.setAdapter(privacyLevelAdapter);

        privacyLevelSpinner.setSelection(privacyLevelAdapter.getPosition(event.privacyLevel));

        dateRangePickerDialogs = new DateRangePickerDialogs(this,
                event.start, event.end,
                startTimeTextView, endTimeTextView,
                startDateTextView, endDateTextView);
    }

    private void setEditability(boolean value) {
        editable = value;
        setEditability(summaryEditText, value);
        setEditability(saveButton, value);
        setEditability(deleteButton, value);
        setEditability(privacyLevelSpinner, value);
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


    private void delete() {
        if (!editable) {
            return;
        }

        Call<Boolean> call = service.removeEvent(event.id);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful() || !response.body()) {
                    makeToast("Failed to remove event!");
                    return;
                }

                appContext.getUser().calendar.removeEvent(event.id);

                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                makeToast("Failed to remove event!");
            }
        });


    }

    private void save() {
        if(!validateFields() || !editable) {
            return;
        }

        if (event.id.equals("")) {
            Call<Event> call = service.addEvent(event);

            call.enqueue(new Callback<Event>() {
                @Override
                public void onResponse(Call<Event> call, Response<Event> response) {

                    if (!response.isSuccessful()) {
                        makeToast("Failed to create event!");
                        return;
                    }

                    Event e = response.body();

                    appContext.getUser().calendar.upsertEvent(e);
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(Call<Event> call, Throwable t) {
                    makeToast("Failed to create event!");
                }
            });

            return;
        }

        Call<Boolean> call = service.updateEvent(event);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (!response.isSuccessful()) {
                    makeToast("Failed to update event!");
                    return;
                }

                appContext.getUser().calendar.upsertEvent(event);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                makeToast("Failed to update event!");
            }
        });
    }

    private boolean validateFields() {
        if (event.start.after(event.end)) {
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
