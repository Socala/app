package socala.app.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import socala.app.R;
import socala.app.adapters.UserAdapter;
import socala.app.layouts.CheckableLayout;
import socala.app.models.User;

public class CalendarOptionsDialog extends DialogFragment {

    @Bind(R.id.calendars_list_view) ListView listView;

    private UserAdapter adapter;
    private List<User> users;
    private List<String> selectedIds;
    private CalendarsChangedListener callback;

    public static CalendarOptionsDialog newInstance(List<User> users, List<String> selected) {

        CalendarOptionsDialog dialog = new CalendarOptionsDialog();

        Bundle args = new Bundle();
        args.putParcelable("users", Parcels.wrap(users));
        args.putParcelable("selected", Parcels.wrap(selected));

        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final View v = LayoutInflater.from(getContext()).inflate(R.layout.calendar_options_dialog, null);
        ButterKnife.bind(this, v);

        users = Parcels.unwrap(getArguments().getParcelable("users"));
        selectedIds = Parcels.unwrap(getArguments().getParcelable("selected"));

        setupListView();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder
                .setTitle("Calendars")
                .setView(v)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onCalendarsChanged(selectedIds);
                    }
                }).setNegativeButton("Cancel", null);

        return builder.create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (CalendarsChangedListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement CalendarsChangedListener interface");
        }
    }

    public void setupListView() {
        adapter = new UserAdapter(getContext(), R.layout.checkable_user_item, users);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckableLayout layout = (CheckableLayout) view;

                User user = adapter.getItem(position);

                if (layout.isChecked()) {
                    selectedIds.add(user.id);
                } else {
                    for (int i = 0; i < selectedIds.size(); i++) {
                        if (selectedIds.get(i).equals(user.id)) {
                            selectedIds.remove(i);
                            return;
                        }
                    }
                }
            }
        });

        // Initialize selections
        for (int i = 0; i < users.size(); i++) {
            if (selectedIds.contains(users.get(i).id)) {
                listView.setItemChecked(i, true);
            } else {
                listView.setItemChecked(i, false);
            }
        }
    }

    public interface CalendarsChangedListener {
        void onCalendarsChanged(List<String> ids);
    }
}
