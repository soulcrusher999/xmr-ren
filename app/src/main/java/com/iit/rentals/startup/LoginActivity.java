package com.iit.rentals.startup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iit.rentals.R;
import com.iit.rentals.home.HomeActivity;
import com.iit.rentals.models.User;
import com.iit.rentals.utils.FirebaseHelper;
import com.iit.rentals.utils.TextDataUtils;
import com.iit.rentals.utils.UserTypes;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 1;
    private Context mContext = LoginActivity.this;

//

    //widgets
    private EditText mEmail, mPassword;
    private ProgressBar mProgressBar;
    private TextView mPleaseWait;

    //var
    private String email, password;
    private FirebaseHelper mFirebaseHelper;
    private View view;
    private GoogleSignInClient mGoogleSignInClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupWidgets();
        setupFirebase();

    }

    private void setupFirebase() {
        mFirebaseHelper = new FirebaseHelper(mContext);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setupWidgets() {

        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);

        mProgressBar = findViewById(R.id.progressBar);




        findViewById(R.id.btn_google_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

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
        } else {
//            mPassword.setError(null);
        }

        return valid;
    }

    public void hideProgressBar() {
        mProgressBar.setVisibility(view.GONE);
//        mPleaseWait.setVisibility(view.GONE);
    }

    public void showProgressBar() {
        mProgressBar.setVisibility(view.VISIBLE);
//        mPleaseWait.setVisibility(view.VISIBLE);
    }




    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressBar();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseHelper.getAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            final FirebaseUser user = mFirebaseHelper.getAuth().getCurrentUser();
                            Log.e(TAG , "onComplete: firebase user:" + user.getUid());
                            //store the information from google sign-in in firebase database
                            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(mContext);

                            if (acct != null) {
                                final String personName = acct.getDisplayName();
                                final String personEmail = acct.getEmail();
                                final Uri personPhoto = acct.getPhotoUrl();
                                mFirebaseHelper.getMyRef().child("users")
                                        .child(user.getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    Log.e(TAG, "onDataChange: datasnap" + dataSnapshot);
//                                                    for (DataSnapshot ds :
//                                                            dataSnapshot.getChildren()) {
//                                                        Log.e(TAG, "onDataChange: children"+ds.toString() );
                                                    User user = dataSnapshot.getValue(User.class);
//                                                        User user1 = new User(user.getUid(),
//                                                                personName,
//                                                                personPhoto.toString(),
//                                                                dataSnapshot.getValue(User.class).getUser_type(),
//                                                                personEmail
//                                                        );
                                                    mFirebaseHelper.getSharedPreference().saveUserInfo(user);

//                                                    }
                                                } else {
                                                    User user1 = new User(user.getUid(),
                                                            personName,
                                                            personPhoto.toString(),
                                                            UserTypes.NORMAL,
                                                            personEmail
                                                    );
                                                    mFirebaseHelper.getSharedPreference().saveUserInfo(user1);
                                                    mFirebaseHelper.getMyRef().child("users").child(user.getUid())
                                                            .setValue(user1);

                                                }
                                                updateUI(user);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    private void updateUI(FirebaseUser user) {
        hideProgressBar();
        if (user != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
//            dsklfjdsklfj

        }
    }


}
