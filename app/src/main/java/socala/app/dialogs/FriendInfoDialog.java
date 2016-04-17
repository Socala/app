package socala.app.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class FriendInfoDialog extends DialogFragment {

    private FriendInfoDialogListener callback;
    private String displayName;
    private String email;

    public static FriendInfoDialog newInstance(String displayName, String email) {

        FriendInfoDialog dialog = new FriendInfoDialog();

        Bundle args = new Bundle();

        args.putString("displayName", displayName);
        args.putString("email", email);

        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder
                .setTitle(displayName)
                .setMessage(email)
                .setPositiveButton("Remove Friend", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onRemoveFriendClicked(email);
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

        displayName = getArguments().getString("displayName");
        email = getArguments().getString("email");

        try {
            callback = (FriendInfoDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement FriendInfoDialogListener interface");
        }
    }

    public interface FriendInfoDialogListener {
        void onRemoveFriendClicked(String id);
    }
}
