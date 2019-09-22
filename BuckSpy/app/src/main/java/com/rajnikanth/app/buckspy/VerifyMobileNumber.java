package com.rajnikanth.app.buckspy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rajnikanth.app.buckspy.database.DatabaseClient;
import com.rajnikanth.app.buckspy.entity.UserProfile;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;


public class VerifyMobileNumber extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    ProgressBar progressBar;
    AppCompatEditText editText;
    AppCompatButton buttonSignIn, buttonResend, skip;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_mobile_number);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        editText = findViewById(R.id.editTextCode);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonResend = findViewById(R.id.buttonResend);
        skip = findViewById(R.id.skip);

        final String phoneNumber = getIntent().getStringExtra("phoneNumber");
        CommonUtils.mobileNumber = phoneNumber;

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("phoneNumber", phoneNumber);
        editor.apply();

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new saveUserProfile().execute();
            }
        });
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
        buttonResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSendVerificationCode(phoneNumber, mResendToken);
            }
        });
        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    editText.setText(code);
                    verifyCode(code);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(VerifyMobileNumber.this, e.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCodeSent(@NotNull String s, @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                mResendToken = forceResendingToken;
            }
        };
        sendVerificationCode(phoneNumber);
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

        progressBar.setVisibility(View.GONE);
    }

    private void reSendVerificationCode(String number, PhoneAuthProvider.ForceResendingToken token) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack, mResendToken
        );

        progressBar.setVisibility(View.GONE);
    }

//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
//            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//        @Override
//        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//            String code = phoneAuthCredential.getSmsCode();
//            if (code != null) {
//                editText.setText(code);
//                verifyCode(code);
//            }
//        }
//
//        @Override
//        public void onVerificationFailed(FirebaseException e) {
//            Toast.makeText(VerifyMobileNumber.this, e.getMessage(), Toast.LENGTH_LONG).show();
//            progressBar.setVisibility(View.GONE);
//        }
//
//        @Override
//        public void onCodeSent(@NotNull String s, @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            verificationId = s;
//            mResendToken = forceResendingToken;
//        }
//    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            new saveUserProfile().execute();
                        } else {
                            Toast.makeText(VerifyMobileNumber.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void startActivity() {
        try {
            Intent intent = new Intent(VerifyMobileNumber.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.getMessage();
        }
    }
    public class saveUserProfile extends AsyncTask {

        @Override
        protected String doInBackground(Object[] objects) {
            try {
                userProfile();
                return "S";
            } catch (Exception e) {
                return "Error : " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object str) {
            super.onPostExecute(str.toString());
            if (str.equals("S")) {
                startActivity();
            }
        }
    }
    private void userProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setCreated_timestamp(new Date().getTime());
        userProfile.setUser_id(CommonUtils.mobileNumber);
        userProfile.setMobile_number(CommonUtils.mobileNumber);
        DatabaseClient.getInstance(this).getAppDatabase().userProfileDao().insert(userProfile);
    }
}