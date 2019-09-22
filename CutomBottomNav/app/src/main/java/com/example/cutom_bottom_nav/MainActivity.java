package com.example.cutom_bottom_nav;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<ScreenSlidePageFragment> fragList = new ArrayList<>();
        fragList.add(ScreenSlidePageFragment.newInstance("Home", R.color.red_inactive));
        fragList.add(ScreenSlidePageFragment.newInstance("Search", R.color.blue_inactive));
        fragList.add(ScreenSlidePageFragment.newInstance("Faves", R.color.blue_grey_inactive));
        fragList.add(ScreenSlidePageFragment.newInstance("Notify", R.color.green_inactive));
        fragList.add(ScreenSlidePageFragment.newInstance("Profile", R.color.purple_inactive));
        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(fragList, getSupportFragmentManager());

        final BubbleNavigationLinearView bubbleNavigationLinearView = findViewById(R.id.bottom_navigation_view_linear);
        bubbleNavigationLinearView.setTypeface(Typeface.createFromAsset(getAssets(), "rubik.ttf"));

        bubbleNavigationLinearView.setBadgeValue(0, "40");
        bubbleNavigationLinearView.setBadgeValue(1, null); //invisible badge
        bubbleNavigationLinearView.setBadgeValue(2, "7");
        bubbleNavigationLinearView.setBadgeValue(3, "2");
        bubbleNavigationLinearView.setBadgeValue(4, ""); //empty badge

        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                bubbleNavigationLinearView.setCurrentActiveItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                viewPager.setCurrentItem(position, true);
            }
        });
    }
}
