package com.example.administrator.buckspy.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.buckspy.R;
import com.example.administrator.buckspy.adapter.ViewPagerAdapter;
import com.example.administrator.buckspy.database.DatabaseClient;
import com.example.administrator.buckspy.entity.UtilizationDetails;

import java.util.Date;

@SuppressLint("ValidFragment")
public class BottomSheetFragment extends BottomSheetDialogFragment {
    View view = null;
    Button bt_cash, bt_card;
    TextView tv_ctgryType, tv_catagory_selection;
    RadioGroup rg_categories, rg_amounts;
    EditText et_amount, et_description;

    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    ExpenceFragment expenceFragment;
    IncomeFragment incomeFragment;

    String[] tabTitle = {"EXPENCE", "INCOME"};
    ViewPagerAdapter viewPagerAdapter = null;

    public BottomSheetFragment(ViewPagerAdapter adapter) {
        this.viewPagerAdapter = adapter;
    }

    @SuppressLint("RestrictedApi")
    @Override

    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        //Set the custom view

        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_bottom_sheet, null);
        dialog.setContentView(view);

        viewPager = view.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);

        tabLayout = view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

//        try {
//            setupTabIcons();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position, false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        /*bt_cash = view.findViewById(R.id.bt_cash);
        bt_card = view.findViewById(R.id.bt_card);
        tv_catagory_selection = (view).findViewById(R.id.tv_catagory_selection);
        tv_ctgryType = (view).findViewById(R.id.tv_ctgryType);
        rg_categories = (view).findViewById(R.id.rg_categories);
        et_amount = (view).findViewById(R.id.et_amount);
        et_description = (view).findViewById(R.id.et_description);
        rg_amounts = (view).findViewById(R.id.rg_amounts);
        initializeControls();
        bt_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                database.utilizationDetailsDao().insert(getUtilizationDetails());
                new saveUtilizationData("C").execute();
            }
        });
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();*/

//        if (behavior != null && behavior instanceof BottomSheetBehavior) {
//            ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//                @Override
//                public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                    String state = "";
//
//                    switch (newState) {
//                        case BottomSheetBehavior.STATE_DRAGGING: {
//                            state = "DRAGGING";
//                            break;
//                        }
//                        case BottomSheetBehavior.STATE_SETTLING: {
//                            state = "SETTLING";
//                            break;
//                        }
//                        case BottomSheetBehavior.STATE_EXPANDED: {
//                            state = "EXPANDED";
//                            break;
//                        }
//                        case BottomSheetBehavior.STATE_COLLAPSED: {
//                            state = "COLLAPSED";
//                            break;
//                        }
//                        case BottomSheetBehavior.STATE_HIDDEN: {
//                            dismiss();
//                            state = "HIDDEN";
//                            break;
//                        }
//                    }
//
//                    Toast.makeText(getContext(), "Bottom Sheet State Changed to: " + state, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                }
//            });
//        }
    }

    private void initializeControls() {
        try {
            rg_categories.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton checkedRadioButton = group.findViewById(checkedId);
                    if (checkedRadioButton.isChecked()) {
                        tv_ctgryType.setVisibility(View.VISIBLE);
                        tv_ctgryType.setText(checkedRadioButton.getText().toString());
                        tv_ctgryType.setTag(checkedRadioButton.getTag());
                    }
                }
            });
            rg_amounts.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    RadioButton checkedRadioButton = radioGroup.findViewById(checkedId);
                    if (checkedRadioButton != null) {
                        String str_amount = et_amount.getText().toString();
                        str_amount = str_amount.equals("") ? "0" : str_amount;
                        et_amount.setText(String.valueOf(Integer.valueOf(str_amount) + Integer.valueOf(checkedRadioButton.getText().toString())));
                        radioGroup.clearCheck();
                    }
                }
            });
//            RadioButton rb_fooddrinks, rb_shopping, rb_bill, rb_travel, rb_others, rb_unknown;
//            rb_fooddrinks = (view).findViewById(R.id.rb_fooddrinks);
//            rb_shopping = (view).findViewById(R.id.rb_shopping);
//            rb_bill = (view).findViewById(R.id.rb_bill);
//            rb_travel = (view).findViewById(R.id.rb_travel);
//            rb_others = (view).findViewById(R.id.rb_others);
//            rb_unknown = (view).findViewById(R.id.rb_unknown);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private UtilizationDetails getUtilizationDetails(String paymentMode) {
        UtilizationDetails utilizationDetails = new UtilizationDetails();
        utilizationDetails.setCategory_Id(tv_ctgryType.getTag().toString());
        utilizationDetails.setAmount(et_amount.getText().toString());
        utilizationDetails.setDescription(et_description.getText().toString());
        utilizationDetails.setMode_of_payment(paymentMode);
        utilizationDetails.setCreatedAt((new Date()).getTime());
        utilizationDetails.setModifiedAt((new Date()).getTime());
        return utilizationDetails;
    }

    private String isNull(String data) {
        return data == null ? "" : data;
    }

    private class saveUtilizationData extends AsyncTask<String, String, String> {

        String paymentMode = "";

        public saveUtilizationData(String mode) {
            this.paymentMode = mode;
        }

        @Override
        protected String doInBackground(String... strings) {
            DatabaseClient.getInstance(getContext()).getAppDatabase().utilizationDetailsDao().insert(getUtilizationDetails(paymentMode));
            return "S";
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            Toast.makeText(getContext(), "Data saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(new FragmentActivity().getSupportFragmentManager());
        expenceFragment = new ExpenceFragment();
        incomeFragment = new IncomeFragment();
        viewPagerAdapter.addFragment(expenceFragment, "EXPENCE");
        viewPagerAdapter.addFragment(incomeFragment, "INCOME");
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void setupTabIcons() {
        for (int i = 0; i < tabTitle.length; i++) {
            tabLayout.getTabAt(i).setCustomView(prepareTabView(i));
        }
    }

    private View prepareTabView(int pos) {
        View view = getLayoutInflater().inflate(R.layout.custom_tab, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(tabTitle[pos]);
        return view;
    }

}
