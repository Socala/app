package socala.app.dialogs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;


public class SocalaDatePickerDialog extends DatePickerDialog {

    public SocalaDatePickerDialog(Context context, final TextView textView, final Calendar calendar) {
        super(context, new OnDateSetListener() {
            final DateFormat formatter = DateFormat.getDateInstance();
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                textView.setText(formatter.format(calendar.getTime()));
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        final DateFormat formatter = DateFormat.getDateInstance();
        textView.setText(formatter.format(calendar.getTime()));
    }
}
