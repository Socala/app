package socala.app.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import socala.app.R;
import socala.app.adapters.UserAdapter;
import socala.app.contexts.AppContext;
import socala.app.dialogs.FriendAddDialog;
import socala.app.dialogs.FriendInfoDialog;
import socala.app.models.User;
import socala.app.services.ISocalaService;
import socala.app.services.SocalaClient;

public class FriendListFragment extends Fragment implements
        FriendInfoDialog.FriendInfoDialogListener,
        FriendAddDialog.FriendAddDialogListener {

    private final AppContext appContext = AppContext.getInstance();
    private final ISocalaService service = SocalaClient.getClient();
    private UserAdapter adapter;

    @Bind(R.id.list_view) ListView listView;

    public FriendListFragment() { }

    public static FriendListFragment newInstance() {
        return new FriendListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        ButterKnife.bind(this, view);

        List<User> friends = appContext.getUser().friends;

        adapter = new UserAdapter(getContext(), R.layout.user_item, friends);

        listView.setAdapter(adapter);

        return view;
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        showFriendAddDialog();
    }

    @OnItemClick(R.id.list_view)
    public void onFriendClick(AdapterView<?> parent, View view, int position, long id) {
        User user = (User) parent.getItemAtPosition(position);
        showFriendDetailsDialog(user);
    }

    @Override
    public void onRemoveFriendClicked(String email) {
        removeFriend(email);
    }

    @Override
    public void onAddFriendClicked(String email) {
        if (email.equals("")) {
            return;
        }

        addFriend(email);
    }

    private void showFriendAddDialog() {
        DialogFragment dialog = FriendAddDialog.newInstance();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "FriendAddDialog");

    }

    private void showFriendDetailsDialog(User user) {
        DialogFragment dialog = FriendInfoDialog.newInstance(user.displayName, user.email);
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "FriendInfoDialog");
    }

    private void addFriend(final String email) {

        service.addFriend(email).enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (!response.isSuccessful()) {
                    showFailedToAddToast(email);
                    return;
                }

                appContext.getUser().friends.add(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showFailedToAddToast(email);
            }
        });

    }

    private void removeFriend(final String email) {

        service.removeFriend(email).enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (!response.isSuccessful()) {
                    showFailedToRemoveToast(email);
                    return;
                }

                if (!response.body()) {
                    showFailedToRemoveToast(email);
                    return;
                }

                User friend = appContext.getUser().getFriend(email);

                appContext.getUser().friends.remove(friend);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                showFailedToRemoveToast(email);
            }
        });
    }

    private void showFailedToAddToast(String email) {
        Toast toast = Toast.makeText(getContext(), "Failed to add " + email, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void showFailedToRemoveToast(String id) {
        User user = appContext.getUser().getFriend(id);

        Toast toast = Toast.makeText(getContext(), "Failed to remove " + user.displayName, Toast.LENGTH_SHORT);
        toast.show();
    }
}