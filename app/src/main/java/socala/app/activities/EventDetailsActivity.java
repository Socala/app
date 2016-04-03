package socala.app.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    private final AppContext appContext = AppContext.getInstance();
    private final ISocalaService service = SocalaClient.getClient();
    private Event event;
    private boolean editable;
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;
    private TimePickerDialog startTimePickerDialog;
    private TimePickerDialog endTimePickerDialog;
    private DateFormat dateFormatter;
    private DateFormat timeFormatter;

    @OnClick({R.id.startDate, R.id.endDate, R.id.endTime, R.id.startTime, R.id.saveButton})
    public void onDateTimeClick(TextView view) {
        int viewId = view.getId();

        if (viewId == R.id.startDate) {
            startDatePickerDialog.show();
        } else if (viewId == R.id.endDate) {
            endDatePickerDialog.show();
        } else if (viewId == R.id.endTime) {
            endTimePickerDialog.show();
        } else if (viewId == R.id.startTime) {
            startTimePickerDialog.show();
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

        dateFormatter = SimpleDateFormat.getDateInstance();
        timeFormatter = SimpleDateFormat.getTimeInstance();

        String startDateStr = dateFormatter.format(event.start.getTime());
        String endDateStr = dateFormatter.format(event.end.getTime());
        String startTimeStr = timeFormatter.format(event.start.getTime());
        String endTimeStr = timeFormatter.format(event.end.getTime());

        startDateTextView.setText(startDateStr);
        endDateTextView.setText(endDateStr);
        startTimeTextView.setText(startTimeStr);
        endTimeTextView.setText(endTimeStr);

        // The Android API Sucks

        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                event.start.set(year, monthOfYear, dayOfMonth);
                startDateTextView.setText(dateFormatter.format(event.start.getTime()));
            }
        },
                event.start.get(Calendar.YEAR),
                event.start.get(Calendar.MONTH),
                event.start.get(Calendar.DAY_OF_MONTH));

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                event.end.set(year, monthOfYear, dayOfMonth);
                endDateTextView.setText(dateFormatter.format(event.end.getTime()));
            }
        },
                event.end.get(Calendar.YEAR),
                event.end.get(Calendar.MONTH),
                event.end.get(Calendar.DAY_OF_MONTH));

        startTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                event.start.set(Calendar.HOUR_OF_DAY, hourOfDay);
                event.start.set(Calendar.MINUTE, minute);
                startTimeTextView.setText(timeFormatter.format(event.start.getTime()));
            }
        },
                event.start.get(Calendar.HOUR_OF_DAY),
                event.start.get(Calendar.MINUTE),
                false);

        endTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                event.end.set(Calendar.HOUR_OF_DAY, hourOfDay);
                event.end.set(Calendar.MINUTE, minute);
                endTimeTextView.setText(timeFormatter.format(event.end.getTime()));
            }
        },
                event.end.get(Calendar.HOUR_OF_DAY),
                event.end.get(Calendar.MINUTE),
                false);
    }

    private void setEditability(boolean value) {
        editable = value;
        setEditability(summaryEditText, value);
        setEditability(rsvpableCheckBox, value);
        setEditability(startDateTextView, value);
        setEditability(endDateTextView, value);
        setEditability(startTimeTextView, value);
        setEditability(endTimeTextView, value);
    }

    private void setEditability(View v, boolean value) {
        v.setFocusable(value);
        v.setClickable(value);
    }

    private void save() {
        if(!validateFields()) {
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
        } else if (event.title == "") {
            makeToast("Cannot have empty event title");
            return false;
        }

        return true;
    }

    private void makeToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
