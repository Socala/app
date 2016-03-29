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
import android.widget.EditText;

import socala.app.R;

public class FriendAddDialog extends DialogFragment {
    private FriendAddDialogListener callback;

    public static FriendAddDialog newInstance() {
        return new FriendAddDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View v = LayoutInflater.from(getContext()).inflate(R.layout.add_friend_dialog, null);

        builder
                .setTitle("Add Friend")
                .setView(v)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText emailEditText = (EditText) v.findViewById(R.id.add_friend_email_edit_text);

                        callback.onAddFriendClicked(emailEditText.getText().toString());
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        return builder.create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (FriendAddDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement FriendAddDialogListener interface");
        }
    }

    public interface FriendAddDialogListener {
        void onAddFriendClicked(String email);
    }
}
