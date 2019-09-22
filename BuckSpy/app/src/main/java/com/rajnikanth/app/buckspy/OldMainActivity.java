package com.rajnikanth.app.buckspy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rajnikanth.app.buckspy.database.DatabaseClient;
import com.rajnikanth.app.buckspy.entity.AccountDetails;
import com.rajnikanth.app.buckspy.entity.CategoryDetails;
import com.rajnikanth.app.buckspy.fragment.BottomSheetFragment;
import com.rajnikanth.app.buckspy.fragment.HomeFragment;
import com.rajnikanth.app.buckspy.fragment.PlacesFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class OldMainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "bottom_sheet";
    public static final int[] CustomisedColours = {
            Color.rgb(247, 203, 180), Color.rgb(234, 247, 180), Color.rgb(84, 109, 76),
            Color.rgb(181, 199, 201), Color.rgb(65, 79, 112), Color.rgb(118, 94, 124)
    };
    CircleImageView cim_profilephoto;
    TextView tv_add;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CurvedBottomNavigationView curvedBottomNavigationView = findViewById(R.id.customBottomBar);
        curvedBottomNavigationView.inflateMenu(R.menu.bottom_menu);
        CurvedBottomNavigationView.disableShiftMode(curvedBottomNavigationView);
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadFragment(new HomeFragment(this));

//        showPieChart();
        tv_add = findViewById(R.id.tv_add);
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetFragment fragment = new BottomSheetFragment(OldMainActivity.this);
                fragment.show(getSupportFragmentManager(), TAG);
            }
        });
//        cim_profilephoto = findViewById(R.id.cim_profilephoto);
//        cim_profilephoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectImage();
//            }
//        });
        new saveCategoryMaster().execute();
        new saveAccountMaster().execute();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showPieChart() {
        try {
            PieChart pieChart = findViewById(R.id.piechart);
            ArrayList<Entry> NoOfEmp = new ArrayList<>();

            NoOfEmp.add(new Entry(10, 0));
            NoOfEmp.add(new Entry(25, 1));
            NoOfEmp.add(new Entry(30, 2));
            NoOfEmp.add(new Entry(5, 3));
            NoOfEmp.add(new Entry(15, 4));
            NoOfEmp.add(new Entry(15, 5));
            PieDataSet dataSet = new PieDataSet(NoOfEmp, "Categories");

            ArrayList<String> year = new ArrayList<>();

            year.add("Food & Drinks");
            year.add("Shopping");
            year.add("Bills");
            year.add("Travel");
            year.add("Others");
            year.add("Unknown");
            PieData data = new PieData(year, dataSet);
            pieChart.setData(data);
            dataSet.setColors(CustomisedColours);
            pieChart.animateXY(5000, 5000);
            pieChart.setHoleRadius(0);
            pieChart.setTransparentCircleRadius(0);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_home:
                Toast.makeText(this, "Clicked 1", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_places:
                Toast.makeText(this, "Clicked 2", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_add:
                Toast.makeText(this, "Clicked 3", Toast.LENGTH_LONG).show();
                break;
//            case R.id.action_bio:
//                Toast.makeText(this, "Clicked 4", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.action_pro:
//                Toast.makeText(this, "Clicked 5", Toast.LENGTH_LONG).show();
//                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        try {
            switch (item.getItemId()) {
                case R.id.action_home:
                    fragment = new HomeFragment(this);
                    break;
                case R.id.action_places:
                    fragment = new PlacesFragment(this);
                    break;
                case R.id.action_add:
                    tv_add.performClick();
                    break;
//                case R.id.action_bio:
//                    fragment = new PlacesFragment();
//                    break;
//                case R.id.action_pro:
//                    fragment = new PlacesFragment();
//                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadFragment(fragment);
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(OldMainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    cim_profilephoto.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, System.currentTimeMillis() + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath + "");
                cim_profilephoto.setImageBitmap(thumbnail);
            }
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
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

    protected class saveCategoryMaster extends AsyncTask {

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
                Toast.makeText(OldMainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(OldMainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
