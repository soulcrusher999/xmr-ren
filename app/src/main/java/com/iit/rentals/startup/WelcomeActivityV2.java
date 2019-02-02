package com.iit.rentals.startup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iit.rentals.R;
import com.iit.rentals.home.HomeActivity;
import com.iit.rentals.utils.CookieThumperSample;

import su.levenetc.android.textsurface.TextSurface;

public class WelcomeActivityV2 extends AppCompatActivity {


    private TextSurface textSurface;
    private Button btn_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_v2);

        textSurface = (TextSurface) findViewById(R.id.text_surface);
        btn_continue = findViewById(R.id.btn_continue);

        btn_continue.postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_continue.setVisibility(View.VISIBLE);
            }
        },5000);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateUI(FirebaseAuth.getInstance().getCurrentUser());

            }
        });

        textSurface.postDelayed(new Runnable() {
            @Override
            public void run() {
                show();
            }
        }, 1000);


    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser !=null){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void show() {
        textSurface.reset();
        CookieThumperSample.play(textSurface,getAssets());
//        CookieThumperSample.play(textSurface, getAssets());
    }

}
