package com.example.cutom_bottom_nav;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<ScreenSlidePageFragment> fragmentList;

    public ScreenSlidePagerAdapter(ArrayList<ScreenSlidePageFragment> fragmentList, FragmentManager fm) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

/*   public Fragment getItem(int position) {
        if (position >= 0 && position < this.fragmentList.size()) {
            Object var10000 = this.fragmentList.get(position);
            return (Fragment)var10000;
        } else {
            return (Fragment)(new ScreenSlidePageFragment());
        }
    }*/
}
