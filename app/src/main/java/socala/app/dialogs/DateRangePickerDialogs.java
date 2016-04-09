package socala.app.dialogs;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateRangePickerDialogs {

    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;
    private TimePickerDialog startTimePickerDialog;
    private TimePickerDialog endTimePickerDialog;

    public DateRangePickerDialogs(Context context, Calendar start, Calendar end,
                                  TextView startTimeTextView,
                                  TextView endTimeTextView,
                                  TextView startDateTextView,
                                  TextView endDateTextView) {

        DateFormat dateFormatter = SimpleDateFormat.getDateInstance();
        DateFormat timeFormatter = SimpleDateFormat.getTimeInstance();

        startDatePickerDialog = new SocalaDatePickerDialog(context, startDateTextView, start, dateFormatter);
        endDatePickerDialog = new SocalaDatePickerDialog(context, endDateTextView, end, dateFormatter);

        startTimePickerDialog = new SocalaTimePickerDialog(context, startTimeTextView, start, timeFormatter);
        endTimePickerDialog = new SocalaTimePickerDialog(context, endTimeTextView, end, timeFormatter);
    }

    public void showStartDatePicker() {
        startDatePickerDialog.show();
    }

    public void showEndDatePicker() {
        endDatePickerDialog.show();
    }

    public void showStartTimePicker() {
        startTimePickerDialog.show();
    }

    public void showEndTimePicker() {
        endTimePickerDialog.show();
    }
}
