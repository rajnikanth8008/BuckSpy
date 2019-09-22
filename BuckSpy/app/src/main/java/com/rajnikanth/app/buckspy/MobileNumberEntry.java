package com.rajnikanth.app.buckspy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MobileNumberEntry extends AppCompatActivity {

    AppCompatEditText editTextCountryCode, editTextPhone;
    AppCompatButton buttonContinue;
    private static final int REQUEST_PHONE_VERIFICATION = 1080;
    private static final int REQUEST_PERMISION = 1010;
    TelephonyManager tMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_number_entry);

        editTextCountryCode = findViewById(R.id.editTextCountryCode);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonContinue = findViewById(R.id.buttonContinue);
        tMgr = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        if ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            editTextCountryCode.setText("+" + tMgr.getLine1Number().substring(0, 2));
            editTextPhone.setText(tMgr.getLine1Number().substring(2, 12));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_SMS, android.Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE}, REQUEST_PERMISION);
        }
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCountryCode.getText().toString().trim();
                String number = editTextPhone.getText().toString().trim();
                if (number.isEmpty() || number.length() < 10) {
                    editTextPhone.setError("Valid number is required");
                    editTextPhone.requestFocus();
                    return;
                }
                String phoneNumber = code + number;
                Intent intent = new Intent(MobileNumberEntry.this, VerifyMobileNumber.class);
                intent.putExtra("phoneNumber", phoneNumber);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISION) {
            if ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
                editTextCountryCode.setText("+" + tMgr.getLine1Number().substring(0, 2));
                editTextPhone.setText(tMgr.getLine1Number().substring(2, 12));
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_SMS, android.Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE}, REQUEST_PERMISION);
            }
        }
    }
}