package com.example.administrator.buckspy;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.buckspy.database.DatabaseClient;
import com.example.administrator.buckspy.entity.UtilizationDetails;

import java.util.Date;

@SuppressLint("ValidFragment")
public class BottomSheetFragment extends BottomSheetDialogFragment {
    View view = null;
    Button bt_cash, bt_card;
    TextView tv_ctgryType;
    RadioGroup rg_categories, rg_amounts;
    EditText et_amount, et_description;

    @SuppressLint("RestrictedApi")
    @Override

    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        //Set the custom view

        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_bottom_sheet, null);
        dialog.setContentView(view);
        bt_cash = view.findViewById(R.id.bt_cash);
        bt_card = view.findViewById(R.id.bt_card);
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
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    String state = "";

                    switch (newState) {
                        case BottomSheetBehavior.STATE_DRAGGING: {
                            state = "DRAGGING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_SETTLING: {
                            state = "SETTLING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_EXPANDED: {
                            state = "EXPANDED";
                            break;
                        }
                        case BottomSheetBehavior.STATE_COLLAPSED: {
                            state = "COLLAPSED";
                            break;
                        }
                        case BottomSheetBehavior.STATE_HIDDEN: {
                            dismiss();
                            state = "HIDDEN";
                            break;
                        }
                    }

                    Toast.makeText(getContext(), "Bottom Sheet State Changed to: " + state, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });
        }
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
}
