package com.rajnikanth.app.buckspy;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.rajnikanth.app.buckspy.database.DatabaseClient;
import com.rajnikanth.app.buckspy.entity.AccountDetails;
import com.rajnikanth.app.buckspy.entity.CategoryDetails;

import java.util.Date;

public class BaseActivity extends FragmentActivity {
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            new saveCategoryMaster().execute();
            new saveAccountMaster().execute();
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class saveCategoryMaster extends AsyncTask {

        @Override
        protected String doInBackground(Object[] objects) {
            try {
                generateCategoryMaster();
                return "S";
            } catch (Exception e) {
                return "Error : " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object str) {
            super.onPostExecute(str.toString());
            if (str.equals("S")) {
                Log.e(TAG, "saveCategoryMaster Saved");
            }
        }
    }

    public class saveAccountMaster extends AsyncTask {

        @Override
        protected String doInBackground(Object[] objects) {
            try {
                generateAccountMaster();
                return "S";
            } catch (Exception e) {
                return "Error : " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object str) {
            super.onPostExecute(str.toString());
            if (str.equals("S")) {
                Log.e(TAG, "saveAccountMaster Saved");
            }
        }
    }

    private CategoryDetails categoryDetails(String category_Id, String category_type, String category_name, int resource) {
        CategoryDetails categoryDetails = new CategoryDetails();
        categoryDetails.setCreated_timestamp(new Date().getTime());
//        categoryDetails.setModified_timestamp(0);
        categoryDetails.setCategory_id(category_Id);
        categoryDetails.setCategory_type(category_type);
        categoryDetails.setCategory_name(category_name);
        categoryDetails.setCategory_description("");
        categoryDetails.setImage_path(resource + "");
        return categoryDetails;
    }

    public void generateCategoryMaster() throws Exception {
        CategoryDetails[] categoryDetails = new CategoryDetails[15];
        try {
            categoryDetails[0] = categoryDetails("CE001", "E", "Bill", R.drawable.ic_bill);
            categoryDetails[1] = categoryDetails("CE002", "E", "Clothing", R.drawable.ic_clothing);
            categoryDetails[2] = categoryDetails("CE003", "E", "Drinks", R.drawable.ic_drinks);
            categoryDetails[3] = categoryDetails("CE004", "E", "Education", R.drawable.ic_education);
            categoryDetails[4] = categoryDetails("CE005", "E", "Food", R.drawable.ic_food);
            categoryDetails[5] = categoryDetails("CE006", "E", "Fuel", R.drawable.ic_fuel);
            categoryDetails[6] = categoryDetails("CE007", "E", "Health", R.drawable.ic_health);
            categoryDetails[7] = categoryDetails("CE008", "E", "Home", R.drawable.ic_home);
            categoryDetails[8] = categoryDetails("CE009", "E", "Other", R.drawable.ic_other);
            categoryDetails[9] = categoryDetails("CE010", "E", "Personal", R.drawable.ic_personal);
            categoryDetails[10] = categoryDetails("CE011", "E", "Restaurant", R.drawable.ic_restaurant);
            categoryDetails[11] = categoryDetails("CE012", "E", "Transport", R.drawable.ic_transport);
            categoryDetails[12] = categoryDetails("CI001", "I", "Loan", R.drawable.ic_loan);
            categoryDetails[13] = categoryDetails("CI002", "I", "Salary", R.drawable.ic_salary);
            categoryDetails[14] = categoryDetails("CI003", "I", "Sales", R.drawable.ic_sales);
            DatabaseClient.getInstance(this).getAppDatabase().categoryDetailsDao().insert(categoryDetails);
        } catch (Exception e) {
            e.getMessage();
            throw e;
        }
    }

    private AccountDetails accountDetails(String account_id, String account_name, int resource) {
        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setCreated_timestamp(new Date().getTime());
//        accountDetails.setModified_timestamp(0);
        accountDetails.setAccount_id(account_id);
        accountDetails.setAccount_name(account_name);
        accountDetails.setAccount_description("");
//        accountDetails.setExpire_timestamp(0);
        accountDetails.setImage_path(resource + "");
        return accountDetails;
    }

    public void generateAccountMaster() throws Exception {
        AccountDetails[] accountDetails = new AccountDetails[3];
        try {
            accountDetails[0] = accountDetails("AC001", "Card", R.drawable.ic_card);
            accountDetails[1] = accountDetails("AC002", "Cash", R.drawable.ic_cash);
            accountDetails[2] = accountDetails("AC003", "Savings", R.drawable.ic_savings);
            DatabaseClient.getInstance(this).getAppDatabase().accountDetailsDao().insert(accountDetails);
        } catch (Exception e) {
            e.getMessage();
            throw e;
        }
    }
}
