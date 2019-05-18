package com.example.administrator.buckspy.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.buckspy.R;
import com.example.administrator.buckspy.adapter.CommonSpinnerAdapter;
import com.example.administrator.buckspy.dao.SpinnerDataDO;
import com.example.administrator.buckspy.database.DatabaseClient;
import com.example.administrator.buckspy.entity.UtilizationDetails;

import java.util.ArrayList;
import java.util.Date;

public class ExpenceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    View view = null;
    Button bt_cash, bt_card;
    TextView tv_ctgryType, tv_catagory_img, tv_catagory_selection;
    RadioGroup rg_categories, rg_amounts;
    EditText et_amount, et_description;
    LinearLayout ll_categoryselection;
    Spinner sp_choose_category;
    ArrayList<SpinnerDataDO> spinnerDataDOS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_expence, container, false);
        bt_cash = view.findViewById(R.id.bt_cash);
        bt_card = view.findViewById(R.id.bt_card);
        tv_catagory_img = (view).findViewById(R.id.tv_catagory_img);
        tv_catagory_selection = (view).findViewById(R.id.tv_catagory_selection);
        tv_ctgryType = (view).findViewById(R.id.tv_ctgryType);
        rg_categories = (view).findViewById(R.id.rg_categories);
        et_amount = (view).findViewById(R.id.et_amount);
        et_description = (view).findViewById(R.id.et_description);
        rg_amounts = (view).findViewById(R.id.rg_amounts);
        ll_categoryselection = (view).findViewById(R.id.ll_categoryselection);
        sp_choose_category = (view).findViewById(R.id.sp_choose_category);
        CommonSpinnerAdapter commonSpinnerAdapter = new CommonSpinnerAdapter((Activity) getContext(), loadCategoryData());
        sp_choose_category.setAdapter(commonSpinnerAdapter);
        spinnerDataDOS = new ArrayList<>();
        initializeControls();

        bt_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                database.utilizationDetailsDao().insert(getUtilizationDetails());
                new saveUtilizationData("C").execute();
            }
        });
        ll_categoryselection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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

    private ArrayList<SpinnerDataDO> loadCategoryData() {
        ArrayList<SpinnerDataDO> spinnerDataDOS = new ArrayList<>();
        spinnerDataDOS.add(new SpinnerDataDO(R.drawable.ic_info, "Choose Category"));
        spinnerDataDOS.add(new SpinnerDataDO(R.drawable.ic_bill, "Bill"));
        spinnerDataDOS.add(new SpinnerDataDO(R.drawable.ic_clothing, "Clothing"));
        spinnerDataDOS.add(new SpinnerDataDO(R.drawable.ic_drinks, "Drinks"));
        spinnerDataDOS.add(new SpinnerDataDO(R.drawable.ic_education, "Education"));
        spinnerDataDOS.add(new SpinnerDataDO(R.drawable.ic_food, "Food"));
        spinnerDataDOS.add(new SpinnerDataDO(R.drawable.ic_fuel, "Fuel"));
        spinnerDataDOS.add(new SpinnerDataDO(R.drawable.ic_health, "Health"));
        spinnerDataDOS.add(new SpinnerDataDO(R.drawable.ic_home, "Home"));
        spinnerDataDOS.add(new SpinnerDataDO(R.drawable.ic_other, "Other"));
        spinnerDataDOS.add(new SpinnerDataDO(R.drawable.ic_personal, "Personal"));
        spinnerDataDOS.add(new SpinnerDataDO(R.drawable.ic_restaurant, "Restaurant"));
        spinnerDataDOS.add(new SpinnerDataDO(R.drawable.ic_transport, "Transport"));
        return spinnerDataDOS;
    }
}
