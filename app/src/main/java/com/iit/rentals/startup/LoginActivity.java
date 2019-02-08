package com.iit.rentals.startup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.iit.rentals.R;
import com.iit.rentals.home.HomeActivity;
import com.iit.rentals.utils.FirebaseHelper;
import com.iit.rentals.utils.TextDataUtils;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 1;
    private Context mContext = LoginActivity.this;

    //widgets
    private EditText mEmail, mPassword;
    private ProgressBar mProgressBar;

    //var
    private String email, password;
    private FirebaseHelper mFirebaseHelper;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupWidgets();
        setupFirebase();

    }

    private void setupFirebase() {
        mFirebaseHelper = new FirebaseHelper(mContext);
    }

    private void setupWidgets() {

        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);

        mProgressBar = findViewById(R.id.progressBar);

        findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RegisterActivity.class));
            }
        });
//normal login
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();

                if (validateForm()) {
                    mFirebaseHelper.checkLogin(email, password, LoginActivity.this);
                } else {
                    Toast.makeText(mContext, "Some fields are not set correctly", Toast.LENGTH_SHORT).show();
                    hideProgressBar();
                }
            }
        });


        hideProgressBar();
    }

    private boolean validateForm() {
        boolean valid = true;

        if (TextDataUtils.isEmpty(email)) {
//            mEmail.setError("Required.");
            valid = false;
        }

        if (!TextDataUtils.isValidPassword(password)) {
//            mPassword.setError("Password should be more than 6 characters.");
            valid = false;
        }

        return valid;
    }

    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
//        mPleaseWait.setVisibility(view.GONE);
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
//        mPleaseWait.setVisibility(view.VISIBLE);
    }


    private void updateUI(FirebaseUser user) {
        hideProgressBar();
        if (user != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        }
    }


}
