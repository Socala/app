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

    private int resource;

    public UserAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public UserAdapter(Context context, int resource, List<User> items) {
        super(context, resource, items);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        UserHolder holder;

        if (convertView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            convertView = vi.inflate(resource, parent, false);

            holder = new UserHolder();
            holder.userText = (TextView) convertView.findViewById(R.id.user_display_name);
            convertView.setTag(holder);
        } else {
            holder = (UserHolder) convertView.getTag();
        }

        User user = this.getItem(position);
        holder.userText.setText(user.displayName);

        return convertView;
    }

    static class UserHolder {
        TextView userText;
    }
}
