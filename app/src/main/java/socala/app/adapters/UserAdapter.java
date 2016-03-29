package socala.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import socala.app.R;
import socala.app.models.User;

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public UserAdapter(Context context, int resource, List<User> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FriendHolder holder;

        if (convertView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            convertView = vi.inflate(R.layout.user_item, parent, false);

            holder = new FriendHolder();
            holder.userText = (TextView) convertView.findViewById(R.id.user_display_name);
            convertView.setTag(holder);
        } else {
            holder = (FriendHolder) convertView.getTag();
        }

        User user = this.getItem(position);
        holder.userText.setText(user.getDisplayName());

        return convertView;
    }

    static class FriendHolder {
        TextView userText;
    }
}
