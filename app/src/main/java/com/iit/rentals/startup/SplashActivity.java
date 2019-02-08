package com.iit.rentals.startup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.iit.rentals.R;
import com.iit.rentals.home.HomeActivity;
import com.iit.rentals.utils.SharedPreferenceHelper;
import com.iit.rentals.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private Context mContext = SplashActivity.this;
    private SharedPreferenceHelper sharedPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(mContext);
        loadScreen();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        initImageLoader();
    }


    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void loadScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (sharedPreferenceHelper.getIsFirstLaunch()) {
                    sharedPreferenceHelper.setIsfirstlaunch(false);
                    Intent in = new Intent(mContext, WelcomeActivityV2.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                } else {
                    if (FirebaseAuth.getInstance().getCurrentUser() == null){

                        Intent in = new Intent(mContext, LoginActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                    }else {
                        Intent in = new Intent(mContext, HomeActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                    }
                }

            }
        }, 2000);
    }


}
