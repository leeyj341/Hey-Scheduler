package com.second.project.heysched.plan;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.second.project.heysched.R;
import com.second.project.heysched.plan.adapter.PlaceAutocompleteAdapter;
import com.second.project.heysched.plan.adapter.SectionsPagerAdapter;

import java.util.ArrayList;

public class SearchLocationActivity extends AppCompatActivity {
    FragmentPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        ViewPager viewPager = findViewById(R.id.view_pager);
        adapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


    }

}