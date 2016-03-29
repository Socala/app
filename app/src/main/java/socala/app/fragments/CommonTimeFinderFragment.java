package socala.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import socala.app.R;

public class CommonTimeFinderFragment extends Fragment {

    public CommonTimeFinderFragment() {
        // Required empty public constructor
    }

    public static CommonTimeFinderFragment newInstance() {
        CommonTimeFinderFragment fragment = new CommonTimeFinderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_time_finder, container, false);

        ButterKnife.bind(this, view);

        return view;
    }
}
