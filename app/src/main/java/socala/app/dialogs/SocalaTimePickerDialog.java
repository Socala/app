package socala.app.dialogs;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;


public class SocalaTimePickerDialog extends TimePickerDialog {

    public SocalaTimePickerDialog(Context context, final TextView textView, final Calendar calendar) {
        super(context, new TimePickerDialog.OnTimeSetListener() {
            final DateFormat formatter = DateFormat.getTimeInstance(DateFormat.SHORT);
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                textView.setText(formatter.format(calendar.getTime()));
            }
        },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false);

        final DateFormat formatter = DateFormat.getTimeInstance(DateFormat.SHORT);
        textView.setText(formatter.format(calendar.getTime()));
    }
}
