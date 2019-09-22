package com.rajnikanth.app.buckspy.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.rajnikanth.app.buckspy.CommonUtils;
import com.rajnikanth.app.buckspy.MainActivity;
import com.rajnikanth.app.buckspy.OldMainActivity;
import com.rajnikanth.app.buckspy.R;
import com.rajnikanth.app.buckspy.database.DatabaseClient;
import com.rajnikanth.app.buckspy.entity.DashBoardDetails;
import com.rajnikanth.app.buckspy.entity.UserProfile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {

    PieChart pieChart;
    CircleImageView cim_profilephoto;
    TextView tv_total_month, tv_total_income, tv_total_amount;
    Activity context = null;
    EditText et_username;
    ImageView imv_user_edit;
    boolean isUserEdit = false;

    public HomeFragment(OldMainActivity oldMainActivity) {
        this.context = oldMainActivity;
    }

    public HomeFragment(MainActivity mainActivity) {
        this.context = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        View view = getLayoutInflater().inflate(R.layout.content_main, container, false);
        initializeControls(view);
        String currentMonth = new SimpleDateFormat("MM").format(new Date());
        tv_total_month.setText(CommonUtils.getMonth(Integer.parseInt(currentMonth)));
        new loadCurrentMonthData(currentMonth).execute();
        new loadUserProfile().execute();
        imv_user_edit.setBackground(getResources().getDrawable(R.drawable.ic_pencil));
        et_username.setEnabled(isUserEdit);
        et_username.setCursorVisible(isUserEdit);
        imv_user_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserEdit) {
                    if (et_username.getText().toString().trim().length() < 4) {
                        Toast.makeText(context, "User name length should be greater than four char", Toast.LENGTH_LONG).show();
                    } else {
                        new updateUserName().execute();
                    }
                } else {
                    imv_user_edit.setBackground(getResources().getDrawable(R.drawable.ic_check));
                    isUserEdit = true;
                    et_username.setEnabled(isUserEdit);
                    et_username.setCursorVisible(isUserEdit);
                }
            }
        });
        cim_profilephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        showPieChart();
        return view;
    }

    public class updateUserName extends AsyncTask {

        @Override
        protected String doInBackground(Object[] objects) {
            try {
                DatabaseClient.getInstance(context).getAppDatabase().userProfileDao().updateUserName(CommonUtils.mobileNumber, et_username.getText().toString().trim());
                return "S";
            } catch (Exception e) {
                return "Error : " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object str) {
            super.onPostExecute(str.toString());
            if (str.equals("S")) {
                imv_user_edit.setBackground(getResources().getDrawable(R.drawable.ic_pencil));
                isUserEdit = false;
                et_username.setEnabled(isUserEdit);
                et_username.setCursorVisible(isUserEdit);
            }
        }
    }

    public class updateUserPhoto extends AsyncTask {
        String bitMapToString = "";

        public updateUserPhoto(String bitMapToString) {
            this.bitMapToString = bitMapToString;
        }

        @Override
        protected String doInBackground(Object[] objects) {
            try {
                DatabaseClient.getInstance(context).getAppDatabase().userProfileDao().updateImagePath(CommonUtils.mobileNumber, bitMapToString);
                return "S";
            } catch (Exception e) {
                return "Error : " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object str) {
            super.onPostExecute(str.toString());
            if (str.equals("S")) {
                imv_user_edit.setBackground(getResources().getDrawable(R.drawable.ic_pencil));
                isUserEdit = false;
                et_username.setEnabled(isUserEdit);
                et_username.setCursorVisible(isUserEdit);
            }
        }
    }


    private void initializeControls(View view) {
        try {
            TextView tv_greeting = view.findViewById(R.id.tv_greeting);
            tv_greeting.setTypeface(ResourcesCompat.getFont(context, R.font.baloo));

            et_username = view.findViewById(R.id.et_username);
            et_username.setTypeface(ResourcesCompat.getFont(context, R.font.baloo));

            imv_user_edit = view.findViewById(R.id.imv_user_edit);

            tv_total_month = view.findViewById(R.id.tv_total_month);
            tv_total_month.setTypeface(ResourcesCompat.getFont(context, R.font.baloo));

            tv_total_income = view.findViewById(R.id.tv_total_income);
            tv_total_income.setTypeface(ResourcesCompat.getFont(context, R.font.petrona), Typeface.BOLD);

            tv_total_amount = view.findViewById(R.id.tv_total_amount);
            tv_total_amount.setTypeface(ResourcesCompat.getFont(context, R.font.petrona), Typeface.BOLD);

            TextView tv_modeofspent = view.findViewById(R.id.tv_modeofspent);
            tv_modeofspent.setTypeface(ResourcesCompat.getFont(context, R.font.baloo));

            TextView tv_modeofspent_card = view.findViewById(R.id.tv_modeofspent_card);
            tv_modeofspent_card.setTypeface(ResourcesCompat.getFont(context, R.font.petrona), Typeface.BOLD);

            TextView tv_modeofspent_cash = view.findViewById(R.id.tv_modeofspent_cash);
            tv_modeofspent_cash.setTypeface(ResourcesCompat.getFont(context, R.font.petrona), Typeface.BOLD);

            pieChart = view.findViewById(R.id.piechart);
            cim_profilephoto = view.findViewById(R.id.cim_profilephoto);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void showPieChart() {
        try {
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

    public static final int[] CustomisedColours = {
            Color.rgb(247, 203, 180), Color.rgb(234, 247, 180), Color.rgb(84, 109, 76),
            Color.rgb(181, 199, 201), Color.rgb(65, 79, 112), Color.rgb(118, 94, 124)
    };

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                try {
                    if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    } else {
                        if ((ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                                && (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                                && (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {

                            if (options[item].equals("Take Photo")) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                                startActivityForResult(intent, 1);
                            } else if (options[item].equals("Choose from Gallery")) {
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 2);
                            }
                        } else {
                            ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                        }
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
        builder.show();
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
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
//                        new updateUserPhoto(CommonUtils.BitMapToString(bitmap)).execute();
//                        String path = android.os.Environment
//                                .getExternalStorageDirectory()
//                                + File.separator
//                                + "Phoenix" + File.separator + "default";
//                        f.delete();
//                        OutputStream outFile = null;
//                        File file = new File(path, System.currentTimeMillis() + ".jpg");
//                        try {
//                            outFile = new FileOutputStream(file);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
//                            outFile.flush();
//                            outFile.close();
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == 2) {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContext().getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    Log.w("path of image from gallery......******************.........", picturePath + "");
                    cim_profilephoto.setImageBitmap(thumbnail);
//                    new updateUserPhoto(CommonUtils.BitMapToString(thumbnail)).execute();
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public class loadCurrentMonthData extends AsyncTask {
        DashBoardDetails dashBoardDetails = new DashBoardDetails();
        String currentMonth = "";

        public loadCurrentMonthData(String currentMonth) {
            this.currentMonth = currentMonth;
        }

        @Override
        protected String doInBackground(Object[] objects) {
            try {
                dashBoardDetails = DatabaseClient.getInstance(context).getAppDatabase().incomeDetailsDao().getCurrentMonthData(currentMonth);
                return "S";
            } catch (Exception e) {
                return "Error : " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object str) {
            super.onPostExecute(str.toString());
            if (str.equals("S")) {
                tv_total_income.setText(dashBoardDetails.getIncome_amount());
                tv_total_amount.setText(dashBoardDetails.getExpense_amount());
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class loadUserProfile extends AsyncTask {
        UserProfile userProfile;

        @Override
        protected String doInBackground(Object[] objects) {
            try {
                userProfile = DatabaseClient.getInstance(context).getAppDatabase().userProfileDao().getUserProfileDetails();
                return "S";
            } catch (Exception e) {
                return "Error : " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object str) {
            super.onPostExecute(str.toString());
            if (str.equals("S")) {
                CommonUtils.mobileNumber = CommonUtils.isNull(userProfile.getMobile_number());
                et_username.setText(CommonUtils.isNull(userProfile.getUser_name()).equals("") ? "User" : userProfile.getUser_name());
//                Bitmap bitmap = CommonUtils.StringToBitMap(userProfile.getImage_path());
//                if (bitmap != null) {
//                    cim_profilephoto.setImageBitmap(bitmap);
//                }
            }
        }
    }
}
