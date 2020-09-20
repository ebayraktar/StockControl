package com.bayraktar.stockcontrol.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bayraktar.stockcontrol.view.MainActivity;
import com.bayraktar.stockcontrol.R;
import com.bayraktar.stockcontrol.database.model.User;

public class ProfileFragment extends Fragment {

    TextView tvNameValue, tvUsernameValue;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        tvNameValue = root.findViewById(R.id.tvNameValue);
        tvUsernameValue = root.findViewById(R.id.tvUsernameValue);
        return  root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        User user = ((MainActivity) getActivity()).user;

        tvNameValue.setText(user.getName());
        tvUsernameValue.setText(user.getUserName());
        // TODO: Use the ViewModel
    }

}