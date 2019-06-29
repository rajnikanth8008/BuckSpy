package com.example.administrator.buckspy.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.buckspy.CommonUtils;
import com.example.administrator.buckspy.R;
import com.example.administrator.buckspy.adapter.CommonSpinnerAdapter;
import com.example.administrator.buckspy.database.DatabaseClient;
import com.example.administrator.buckspy.entity.AccountDetails;
import com.example.administrator.buckspy.entity.CategoryDetails;
import com.example.administrator.buckspy.entity.ExpenseDetails;
import com.example.administrator.buckspy.entity.IncomeDetails;

import java.util.Date;
import java.util.List;

@SuppressLint("ValidFragment")
public class BottomSheetFragment extends BottomSheetDialogFragment {
    View view = null;
    Button bt_clear, bt_add;
    TextView tv_catagory_img, tv_catagory_selection;
    EditText et_amount, et_description;
    Spinner sp_choose_account, sp_choose_category;
    Activity context = null;
    RadioGroup rg_amounts, rg_categorytype;
    String[] tabTitle = {"EXPENCE", "INCOME"};
    List<AccountDetails> accountDetails;
    List<CategoryDetails> categoryDetails;
    String selectedCategoryType = "E";

    public BottomSheetFragment(Activity context) {
        this.context = context;
    }

    @SuppressLint("RestrictedApi")
    @Override

    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_expence, null);
        dialog.setContentView(view);
        initializeControls();
        new loadAccounts().execute();
        loadCategoryData(selectedCategoryType);
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
        rg_categorytype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                if (checkedRadioButton.isChecked()) {
                    selectedCategoryType = checkedRadioButton.getText().toString().equalsIgnoreCase("Expence") ? "E" : "I";
                    loadCategoryData(selectedCategoryType);
                }
            }
        });
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.stringToIntDefault(et_amount.getText().toString().trim(), 0) <= 0) {
                    Toast.makeText(context, "Enter amount", Toast.LENGTH_LONG).show();
                } else {
                    new saveUtilizationData(selectedCategoryType).execute();
                }
//                database.utilizationDetailsDao().insert(getUtilizationDetails());
            }
        });
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
//        CoordinatorLayout.Behavior behavior = params.getBehavior();

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
            bt_clear = view.findViewById(R.id.bt_clear);
            bt_add = view.findViewById(R.id.bt_add);
            tv_catagory_img = (view).findViewById(R.id.tv_catagory_img);
            tv_catagory_selection = (view).findViewById(R.id.tv_catagory_selection);
            et_amount = (view).findViewById(R.id.et_amount);
            et_description = (view).findViewById(R.id.et_description);
            rg_amounts = (view).findViewById(R.id.rg_amounts);
            sp_choose_account = (view).findViewById(R.id.sp_choose_account);
            sp_choose_category = (view).findViewById(R.id.sp_choose_category);
            rg_categorytype = (view).findViewById(R.id.rg_categorytype);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private String isNull(String data) {
        return data == null ? "" : data;
    }

    private void loadCategoryData(String categoryType) {
        new loadCategories(categoryType).execute();
    }

    private class saveUtilizationData extends AsyncTask<String, String, String> {

        String addType = "";

        public saveUtilizationData(String mode) {
            this.addType = mode;
        }

        @Override
        protected String doInBackground(String... strings) {
            if (addType.equals("E")) {
                DatabaseClient.getInstance(getContext()).getAppDatabase().expenseDetailsDao().insert(saveExpenseDetails());
            } else {
                DatabaseClient.getInstance(getContext()).getAppDatabase().incomeDetailsDao().insert(saveIncomeDetails());
            }
            return "S";
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            Toast.makeText(getContext(), "Data saved", Toast.LENGTH_SHORT).show();
            et_amount.setText("");
            et_description.setText("");
        }
    }

    public class loadAccounts extends AsyncTask {

        @Override
        protected String doInBackground(Object[] objects) {
            try {
                accountDetails = DatabaseClient.getInstance(context).getAppDatabase().accountDetailsDao().loadAll();
                return "S";
            } catch (Exception e) {
                return "Error : " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object str) {
            super.onPostExecute(str.toString());
            if (str.equals("S")) {
                CommonSpinnerAdapter commonSpinnerAdapter = new CommonSpinnerAdapter(context, accountDetails);
                sp_choose_account.setAdapter(commonSpinnerAdapter);
            }
        }
    }

    public class loadCategories extends AsyncTask {
        String categoryType = "";

        public loadCategories(String categoryType) {
            this.categoryType = categoryType;
        }

        @Override
        protected String doInBackground(Object[] objects) {
            try {
                categoryDetails = DatabaseClient.getInstance(context).getAppDatabase().categoryDetailsDao().loadAll(categoryType);
                return "S";
            } catch (Exception e) {
                return "Error : " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object str) {
            super.onPostExecute(str.toString());
            if (str.equals("S")) {
                CommonSpinnerAdapter commonSpinnerAdapter = new CommonSpinnerAdapter(context, categoryDetails);
                sp_choose_category.setAdapter(commonSpinnerAdapter);
            }
        }
    }

    private ExpenseDetails saveExpenseDetails() {
        ExpenseDetails expenseDetails = new ExpenseDetails();
        expenseDetails.setCreated_timestamp(new Date().getTime());
        expenseDetails.setExpense_id(CommonUtils.getRowGUID());
        expenseDetails.setAccount_id(((AccountDetails) sp_choose_account.getSelectedItem()).getAccount_id());
        expenseDetails.setCategory_id(((CategoryDetails) sp_choose_category.getSelectedItem()).getCategory_id());
        expenseDetails.setExpense_amount(et_amount.getText().toString());
        expenseDetails.setExpense_description(et_description.getText().toString());
        return expenseDetails;
    }

    private IncomeDetails saveIncomeDetails() {
        IncomeDetails incomeDetails = new IncomeDetails();
        incomeDetails.setCreated_timestamp(new Date().getTime());
        incomeDetails.setIncome_id(CommonUtils.getRowGUID());
        incomeDetails.setAccount_id(((AccountDetails) sp_choose_account.getSelectedItem()).getAccount_id());
        incomeDetails.setCategory_id(((CategoryDetails) sp_choose_category.getSelectedItem()).getCategory_id());
        incomeDetails.setIncome_amount(et_amount.getText().toString());
        incomeDetails.setIncome_description(et_description.getText().toString());
        return incomeDetails;
    }
}
