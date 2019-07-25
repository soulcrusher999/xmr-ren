package com.iit.rentals.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iit.rentals.R;
import com.iit.rentals.bookmarks.BookmarkActivity;
import com.iit.rentals.categories.CategoryRecyclerAdapter;
import com.iit.rentals.home.adapters.HotelRecyclerAdapter;
import com.iit.rentals.hotelBookings.HotelsBookingActivity;
import com.iit.rentals.models.Category;
import com.iit.rentals.models.Hotel;
import com.iit.rentals.models.Room;
import com.iit.rentals.rents.RentsActivity;
import com.iit.rentals.rooms.RoomRecyclerAdapter;
import com.iit.rentals.startup.LoginActivity;
import com.iit.rentals.utils.FilePaths;
import com.iit.rentals.utils.FirebaseHelper;
import com.iit.rentals.utils.Navigation;
import com.iit.rentals.utils.SharedPreferenceHelper;
import com.iit.rentals.utils.UniversalImageLoader;
import com.iit.rentals.utils.UserTypes;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Context mContext = HomeActivity.this;
    private List<Hotel> mHotelList;

    private RecyclerView hotelRecyclerView;
    private LinearLayoutManager hotelManager;
    private HotelRecyclerAdapter hotelAdapter;

    private RecyclerView categoryRecyclerView;
    private LinearLayoutManager categoryManager;
    private CategoryRecyclerAdapter categoryAdapter;
    private List<Category> mCategoryList;

    private RecyclerView roomRecyclerView;
    private LinearLayoutManager roomManager;
    private RoomRecyclerAdapter roomAdapter;
    private List<Room> mRoomList;

    private FirebaseHelper mFirebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initImageLoader();
        //set main image from universal image loader to load image properly
        ImageView main_image = findViewById(R.id.main_image);
        UniversalImageLoader.setImage(FilePaths.IMAGE_URI, main_image, null, "");
        mFirebaseHelper = new FirebaseHelper(mContext);

        checkAdmin();

        setupCategoryAdapter();
        loadCategoryData();

        setupHotelAdapter();
        loadHotelData();


        setupRoomAdapter();
        loadRoomData();

        setupBottomNavigation();

        logout();

    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }


    private void logout() {
        findViewById(R.id.log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmLogOut();
            }
        });
    }

    private void checkAdmin() {
        if (SharedPreferenceHelper.getInstance(mContext).getUserInfo().getUser_type() == UserTypes.ADMIN) {
            mFirebaseHelper.getMyRef().child(FilePaths.CATEGORY)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<Category> mTempList = new ArrayList<>();
                            for (DataSnapshot ds :
                                    dataSnapshot.getChildren()) {
                                Category category = ds.getValue(Category.class);
                                mTempList.add(category);

                            }

                            if (mTempList.isEmpty()){
                                String keyId = mFirebaseHelper.getMyRef().child(FilePaths.CATEGORY).push().getKey();
                                mFirebaseHelper.getMyRef().child(FilePaths.CATEGORY)
                                        .child(keyId)
                                        .setValue(new Category(
                                                keyId,
                                                FilePaths.ROOM,
                                                "Room",
                                                FilePaths.DEFAULT_IMAGE,
                                                true
                                        ));
                                String keyId1 = mFirebaseHelper.getMyRef().child(FilePaths.CATEGORY).push().getKey();
                                mFirebaseHelper.getMyRef().child(FilePaths.CATEGORY)
                                        .child(keyId1)
                                        .setValue(new Category(
                                                keyId,
                                                FilePaths.HOTEL,
                                                "Hotel",
                                                FilePaths.DEFAULT_IMAGE,
                                                true
                                        ));
                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

    }

    private void loadRoomData() {
        mFirebaseHelper.getMyRef().child(FilePaths.ROOM)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds :
                                dataSnapshot.getChildren()) {
                            Room post = ds.getValue(Room.class);

                            mRoomList.add(post);
                        }
                        roomAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(mContext, mContext.getString(R.string.error_general), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupRoomAdapter() {

        mRoomList = new ArrayList<>();
        roomRecyclerView = findViewById(R.id.roomRecyclerView);

        roomManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);


        roomRecyclerView.setLayoutManager(roomManager);

        roomAdapter = new RoomRecyclerAdapter(mContext, mRoomList);

        roomRecyclerView.setAdapter(roomAdapter);
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
                        hotelAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(mContext, mContext.getString(R.string.error_general), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupHotelAdapter() {

        mHotelList = new ArrayList<>();
        hotelRecyclerView = findViewById(R.id.hotelsRecyclerView);

        hotelManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);


        hotelRecyclerView.setLayoutManager(hotelManager);

        hotelAdapter = new HotelRecyclerAdapter(mContext, mHotelList);

        hotelRecyclerView.setAdapter(hotelAdapter);
    }

    private void loadCategoryData() {
        mFirebaseHelper.getMyRef().child(FilePaths.CATEGORY)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds :
                                dataSnapshot.getChildren()) {
                            Category hotel = ds.getValue(Category.class);

                            mCategoryList.add(hotel);
                        }
                        categoryAdapter.notifyDataSetChanged();
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

        categoryAdapter = new CategoryRecyclerAdapter(mContext, mCategoryList);

        categoryRecyclerView.setAdapter(categoryAdapter);
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
        AHBottomNavigationItem item3;
        if (SharedPreferenceHelper.getInstance(mContext).getUserInfo().getUser_type() == UserTypes.ADMIN){
            item3 = new AHBottomNavigationItem("Hotel Bookings", R.drawable.ic_home, R.color.black);
        }else {
            item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.ic_home, R.color.black);
        }

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

                        if (SharedPreferenceHelper.getInstance(mContext).getUserInfo().getUser_type() == UserTypes.ADMIN){

                            Intent in2 = new Intent(mContext, HotelsBookingActivity.class);
                            startActivity(in2);
                        }else {

                            Intent in2 = new Intent(mContext, BookmarkActivity.class);
                            startActivity(in2);
                        }
                        break;


                }
                return true;
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log_out_menu, menu);
//        menu.findItem(R.id.edit_post_action).setVisible(false);
//        menu.findItem(R.id.delete_post_action).setVisible(false);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.log_out:

                confirmLogOut();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void confirmLogOut() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        builder.setTitle("Confirm  Log out?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mFirebaseHelper.signOut();
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialog.cancel();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
