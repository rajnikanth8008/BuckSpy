package com.rajnikanth.app.buckspy;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.rajnikanth.app.buckspy.fragment.BottomSheetFragment;
import com.rajnikanth.app.buckspy.fragment.HomeFragment;
import com.rajnikanth.app.buckspy.fragment.PlacesFragment;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    ViewPager viewPager;
    BubbleNavigationLinearView bubbleNavigationLinearView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        viewPager = findViewById(R.id.view_pager);
        final ImageView imageView = findViewById(R.id.imageView);
        bubbleNavigationLinearView = findViewById(R.id.bottom_navigation_view_linear);
        bubbleNavigationLinearView.setTypeface(ResourcesCompat.getFont(this, R.font.baloo));
        bubbleNavigationLinearView.setBadgeValue(0, null);
        bubbleNavigationLinearView.setBadgeValue(1, null);

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bubbleNavigationLinearView.setBadgeValue(0, null);
                bubbleNavigationLinearView.setBadgeValue(1, null);
                bubbleNavigationLinearView.setCurrentActiveItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
                viewPager.setCurrentItem(position, true);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFragment fragment = new BottomSheetFragment(MainActivity.this);
                fragment.show(getSupportFragmentManager(), TAG);
            }
        });
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new PlacesFragment(MainActivity.this);
                default:
                    return new HomeFragment(MainActivity.this);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
