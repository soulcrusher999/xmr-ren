package com.iit.rentals.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iit.rentals.R;
import com.iit.rentals.home.adapters.HotelRecyclerAdapter;
import com.iit.rentals.models.Category;
import com.iit.rentals.models.Hotel;
import com.iit.rentals.rents.RentsActivity;
import com.iit.rentals.utils.FilePaths;
import com.iit.rentals.utils.FirebaseHelper;
import com.iit.rentals.utils.Navigation;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Context mContext = HomeActivity.this;
    private List<Hotel> mHotelList;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private HotelRecyclerAdapter adapter;
    private FirebaseHelper mFirebaseHelper;
    private List<Category> mCategoryList;
    private RecyclerView categoryRecyclerView;
    private LinearLayoutManager categoryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFirebaseHelper = new FirebaseHelper(mContext);

        setupHotelAdapter();
        loadHotelData();

        setupCategoryAdapter();
        loadCategoryData();
        setupBottomNavigation();

    }

    private void loadHotelData() {
        mFirebaseHelper.getMyRef().child(FilePaths.HOTEL)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds :
                                dataSnapshot.getChildren()) {
                            Hotel hotel = ds.getValue(Hotel.class);

                            mHotelList.add(hotel);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(mContext, mContext.getString(R.string.error_general), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupHotelAdapter() {

        mHotelList = new ArrayList<>();
        recyclerView = findViewById(R.id.hotelsRecyclerView);

        manager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);


        recyclerView.setLayoutManager(manager);

        adapter = new HotelRecyclerAdapter(mContext, mHotelList);

        recyclerView.setAdapter(adapter);
    }

    private void loadCategoryData() {
        mFirebaseHelper.getMyRef().child(FilePaths.HOTEL)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds :
                                dataSnapshot.getChildren()) {
                            Hotel hotel = ds.getValue(Hotel.class);

                            mHotelList.add(hotel);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(mContext, mContext.getString(R.string.error_general), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupCategoryAdapter() {

        mCategoryList = new ArrayList<>();
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);

        categoryManager  = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

        categoryRecyclerView.setLayoutManager(categoryManager);

        adapter = new HotelRecyclerAdapter(mContext, mHotelList);

        recyclerView.setAdapter(adapter);
    }


    private AHBottomNavigation bottomNavigation;

    private void setupBottomNavigation() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }
        bottomNavigation = findViewById(R.id.bottom_navigation);

// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.ic_home, R.color.black);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_home, R.color.black);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.ic_home, R.color.black);

// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);


        // Set background color
        bottomNavigation.setDefaultBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
        // Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        // Use colored navigation with circle reveal effect
        bottomNavigation.setColored(false);
        bottomNavigation.setUseElevation(false);
        bottomNavigation.setBehaviorTranslationEnabled(false);

// Set current item programmatically
        bottomNavigation.setCurrentItem(0);

        // Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));


        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...

                if (wasSelected){
                    return true;
                }
                switch (position) {
                    case Navigation
                            .HOME:

                        Intent in = new Intent(mContext, HomeActivity.class);
                        startActivity(in);
                        break;
                    case Navigation.RENT:
                        Intent in1 = new Intent(mContext, RentsActivity.class);
                        startActivity(in1);
                        break;
                    case Navigation.WISHLIST:
                        Intent in2 = new Intent(mContext, HomeActivity.class);
                        startActivity(in2);
                        break;


                }
                return true;
            }
        });
    }

}
