package com.second.project.heysched.plan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.second.project.heysched.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailCalendarFragment extends Fragment {

    public DetailCalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.plan_detail, container, false);
    }
}
