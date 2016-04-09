package socala.app.dialogs;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TextView;

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

        startDatePickerDialog = new SocalaDatePickerDialog(context, startDateTextView, start);
        endDatePickerDialog = new SocalaDatePickerDialog(context, endDateTextView, end);

        startTimePickerDialog = new SocalaTimePickerDialog(context, startTimeTextView, start);
        endTimePickerDialog = new SocalaTimePickerDialog(context, endTimeTextView, end);
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
