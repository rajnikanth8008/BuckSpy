package com.example.administrator.buckspy;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class LoginActivity extends Activity implements TabHost.OnTabChangeListener {
    TabHost tabhost;

//    EditText et_pin_one, et_pin_two, et_pin_three, et_pin_four;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tabhost = findViewById(R.id.tabhost);

//        et_pin_one = findViewById(R.id.et_pin_one);
//        et_pin_two = findViewById(R.id.et_pin_two);
//        et_pin_three = findViewById(R.id.et_pin_three);
//        et_pin_four = findViewById(R.id.et_pin_four);

//        CommonUtils.addTextWatcher(et_pin_one, et_pin_two, et_pin_three, et_pin_four);

        tabhost.setup();
        tabhost.setOnTabChangedListener(this);
        for (int i = 0; i < 2; i++) {
            String indicator = (i == 0) ? "SIGN UP" : "SIGN IN";
            int tabId = (i == 0) ? R.id.tab1 : R.id.tab2;
            TabHost.TabSpec tspec = tabhost.newTabSpec(indicator);
            tspec.setIndicator(indicator);
            tspec.setContent(tabId);
            tabhost.addTab(tspec);
        }
    }

    @Override
    public void onTabChanged(String s) {

    }
}
