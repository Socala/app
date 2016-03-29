package socala.app.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

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

public class FriendListFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        FriendInfoDialog.FriendInfoDialogListener,
        FriendAddDialog.FriendAddDialogListener {

    private final AppContext appContext = AppContext.getInstance();
    private final ISocalaService service = SocalaClient.getClient();
    private UserAdapter adapter;
    private ListView listView;

    public FriendListFragment() {
    }

    public static FriendListFragment newInstance() {
        FriendListFragment fragment = new FriendListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_friend_list, container, false);

        listView = (ListView) fragmentView.findViewById(R.id.friend_list_view);

        List<User> friends = appContext.getUser().getFriends();

        adapter = new UserAdapter(getContext(), R.layout.user_item, friends);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) fragmentView.findViewById(R.id.friend_list_fab);

        fab.setOnClickListener(this);

        return fragmentView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.friend_list_fab) {
            showFriendAddDialog();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = (User) parent.getItemAtPosition(position);
        showFriendDetailsDialog(user);
    }

    @Override
    public void onRemoveFriendClicked(String id) {
        removeFriend(id);
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
        DialogFragment dialog = FriendInfoDialog.newInstance(user.getDisplayName(), user.getEmail(), user.getId());
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "FriendInfoDialog");
    }

    private void addFriend(final String email) {

        service.addFriend(appContext.getUser().getOAuthToken(), email).enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                appContext.getUser().getFriends().add(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showFailedToAddToast(email);
            }
        });

    }

    private void removeFriend(final String id) {

        service.removeFriend(appContext.getUser().getOAuthToken(), id).enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {


                if (!response.body()) {
                    showFailedToRemoveToast(id);
                    return;
                }

                User friend = appContext.getUser().getFriend(id);

                appContext.getUser().getFriends().remove(friend);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                showFailedToRemoveToast(id);
            }
        });
    }

    private void showFailedToAddToast(String email) {
        Toast toast = Toast.makeText(getContext(), "Failed to add " + email, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void showFailedToRemoveToast(String id) {
        User user = appContext.getUser().getFriend(id);

        Toast toast = Toast.makeText(getContext(), "Failed to remove " + user.getDisplayName(), Toast.LENGTH_SHORT);
        toast.show();
    }
}