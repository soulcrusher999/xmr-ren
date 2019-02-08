package com.iit.rentals;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class OfflineActivity extends AppCompatActivity {
    private static final String TAG = "OfflineActivity";

    private void enablePersistence() {
        // [START rtdb_enable_persistence]
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // [END rtdb_enable_persistence]
    }
}
