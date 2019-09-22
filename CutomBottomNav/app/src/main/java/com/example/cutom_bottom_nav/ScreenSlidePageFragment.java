package com.example.cutom_bottom_nav;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class ScreenSlidePageFragment extends Fragment {
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static ScreenSlidePageFragment newInstance(String title, int page) {
        ScreenSlidePageFragment fragmentFirst = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = getLayoutInflater().inflate(R.layout.fragment_screen_slide_page, container, false);
        inflatedView.setBackgroundColor(getResources().getColor(page));
        TextView textView = inflatedView.findViewById(R.id.screen_slide_title);
        textView.setText(title);
        return inflatedView;
    }
}
