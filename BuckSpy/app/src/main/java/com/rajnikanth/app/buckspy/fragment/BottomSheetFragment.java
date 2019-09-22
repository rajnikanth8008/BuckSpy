package com.rajnikanth.app.buckspy.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rajnikanth.app.buckspy.CommonUtils;
import com.rajnikanth.app.buckspy.R;
import com.rajnikanth.app.buckspy.adapter.AutoCompleteAdapter;
import com.rajnikanth.app.buckspy.adapter.CommonSpinnerAdapter;
import com.rajnikanth.app.buckspy.database.DatabaseClient;
import com.rajnikanth.app.buckspy.entity.AccountDetails;
import com.rajnikanth.app.buckspy.entity.CategoryDetails;
import com.rajnikanth.app.buckspy.entity.ExpenseDetails;
import com.rajnikanth.app.buckspy.entity.IncomeDetails;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@SuppressLint("ValidFragment")
public class BottomSheetFragment extends BottomSheetDialogFragment implements LocationListener {
    //    private View view;
    private Button bt_clear, bt_add;
    private EditText et_amount, et_description;
    private Spinner sp_choose_account, sp_choose_category;
    private Activity context;
    private RadioGroup rg_amounts, rg_categorytype;
    private List<AccountDetails> accountDetails;
    private List<CategoryDetails> categoryDetails;
    private String selectedCategoryType = "E";
    private AutoCompleteTextView actv_searchplace;
    private PlacesClient placesClient;
    private AutoCompleteAdapter adapter;
    private LinearLayout ll_location_view;
    ImageView imv_current_loaction;
    LocationManager locationManager;

    public BottomSheetFragment(Activity context) {
        this.context = context;
    }

    @SuppressLint({"RestrictedApi", "InflateParams"})
    @Override

    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        try {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_expence, null);
            dialog.setContentView(view);
            if (!Places.isInitialized()) {
                Places.initialize(context, getString(R.string.api_key));
            }
            placesClient = Places.createClient(context);
            initializeControls(view);
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
                        ll_location_view.setVisibility(selectedCategoryType.equals("E") ? View.VISIBLE : View.GONE);
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
                }
            });
            bt_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedCategoryType.equals("E")) {
                        actv_searchplace.setText("");
                        actv_searchplace.setTag("");
                    }
                    et_amount.setText("");
                    et_description.setText("");
                }
            });

            imv_current_loaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentLocation();
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void initializeControls(View view) {
        try {
            bt_clear = view.findViewById(R.id.bt_clear);
            bt_clear.setTypeface(ResourcesCompat.getFont(context, R.font.petrona), Typeface.BOLD);

            bt_add = view.findViewById(R.id.bt_add);
            bt_add.setTypeface(ResourcesCompat.getFont(context, R.font.petrona), Typeface.BOLD);

            et_amount = view.findViewById(R.id.et_amount);
            et_amount.setTypeface(ResourcesCompat.getFont(context, R.font.petrona), Typeface.BOLD);

            et_description = (view).findViewById(R.id.et_description);
            et_description.setTypeface(ResourcesCompat.getFont(context, R.font.petrona), Typeface.BOLD);

            rg_amounts = view.findViewById(R.id.rg_amounts);

            sp_choose_account = view.findViewById(R.id.sp_choose_account);
            sp_choose_category = view.findViewById(R.id.sp_choose_category);

            rg_categorytype = view.findViewById(R.id.rg_categorytype);

            actv_searchplace = view.findViewById(R.id.actv_searchplace);
            actv_searchplace.setTypeface(ResourcesCompat.getFont(context, R.font.petrona), Typeface.BOLD);
            actv_searchplace.setHint("Enter Place");
            actv_searchplace.setOnItemClickListener(autocompleteClickListener);
            adapter = new AutoCompleteAdapter(context, placesClient);
            actv_searchplace.setAdapter(adapter);

            ll_location_view = view.findViewById(R.id.ll_location_view);
            imv_current_loaction = view.findViewById(R.id.imv_current_loaction);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                final AutocompletePrediction item = adapter.getItem(position);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }
                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields).build();
                }
                if (request != null) {
                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {
                            actv_searchplace.setTag(Objects.requireNonNull(task.getPlace().getLatLng()).latitude + "," + task.getPlace().getLatLng().longitude);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            actv_searchplace.setTag("");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void loadCategoryData(String categoryType) {
        new loadCategories(categoryType).execute();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            actv_searchplace.setText(addresses.get(0).getAddressLine(0));
            actv_searchplace.setTag(location.getLatitude() + "," + location.getLongitude());
        } catch (Exception e) {

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(context, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

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
            actv_searchplace.setTag("");
            actv_searchplace.setText("");
        }
    }

    @SuppressLint("StaticFieldLeak")
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
        expenseDetails.setExpence_location(actv_searchplace.getTag().toString());
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

    private void currentLocation() {
        try {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

            }
            getLocation();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

}
