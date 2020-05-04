package com.second.project.heysched.plan.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.second.project.heysched.R;
import com.second.project.heysched.plan.fragment.RecommandFragment;
import com.second.project.heysched.plan.fragment.SearchFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private  static int NUM_TABS = 2;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return RecommandFragment.newInstance(position);
            case 1:
                return SearchFragment.newInstance(position);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title="";
        if(position==0){
            title = "recommand";
        }else {
            title = "search";
        }
        return title;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return NUM_TABS;
    }
}