package com.iit.rentals.rents;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.iit.rentals.R;
import com.iit.rentals.bookmarks.BookmarkActivity;
import com.iit.rentals.categories.CategoryAddActivity;
import com.iit.rentals.home.HomeActivity;
import com.iit.rentals.hotels.HotelAddActivity;
import com.iit.rentals.others.OthersAddActivity;
import com.iit.rentals.rooms.RoomAddActivity;
import com.iit.rentals.utils.Navigation;
import com.iit.rentals.utils.SharedPreferenceHelper;
import com.iit.rentals.utils.UserTypes;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class RentsActivity extends AppCompatActivity {

    private SharedPreferenceHelper sharedPreferenceHelper;
    private Context mContext = RentsActivity.this;

    private int position = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rents);

        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(mContext);

        checkIncomingIntent();

        setupToolbar();
        initUI();
        setupBottomNavigation();

        checkIfAdmin();
    }

    private void checkIncomingIntent() {
        Intent in = getIntent();
        if (in.hasExtra(mContext.getString(R.string.calling_category))){
            position = in.getIntExtra(mContext.getString(R.string.calling_category),0);
        }
    }

    private void checkIfAdmin() {
        FloatingActionMenu menu = findViewById(R.id.menu);
        if (sharedPreferenceHelper.getUserType() == UserTypes.ADMIN) {
            menu.setVisibility(View.VISIBLE);

            setupAdminMenu();
        } else if (sharedPreferenceHelper.getUserType() == UserTypes.NORMAL) {
            menu.setVisibility(View.GONE);
        }
    }

    private void setupAdminMenu() {
        FloatingActionButton menu_add_category, menu_add_rent_room_hotel, menu_add_rent_room, menu_add_rent_room_others;

        menu_add_rent_room_hotel = findViewById(R.id.menu_add_rent_hotel);
        menu_add_rent_room = findViewById(R.id.menu_add_rent_room);
        menu_add_rent_room_others = findViewById(R.id.menu_add_rent_others);
        menu_add_category = findViewById(R.id.menu_add_category);

        menu_add_rent_room_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, HotelAddActivity.class));
            }
        });
        menu_add_rent_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, RoomAddActivity.class));
            }
        });
        menu_add_rent_room_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, OthersAddActivity.class));
            }
        });
        menu_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, CategoryAddActivity.class));
            }
        });


    }

    private void setupToolbar() {
        TextView toolbar = findViewById(R.id.toolbar);
        toolbar.setText("Find Rents");
//        setSupportActionBar(toolbar);
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Find Rents");
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

    }

    private void initUI() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new HotelsFragment()); //index 0
        adapter.addFragment(new RoomFragment()); //index 1
        adapter.addFragment(new OthersFragment()); //index 1

        viewPager.setAdapter(adapter);

        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_rent_restaurant),
                        Color.parseColor(colors[0]))
                        .title("Hotels")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_rent_home),
                        Color.parseColor(colors[1]))
                        .title("Rooms")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_rent_extra),
                        Color.parseColor(colors[2]))
                        .title("Others")
                        .build()
        );


        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, position);

        //IMPORTANT: ENABLE SCROLL BEHAVIOUR IN COORDINATOR LAYOUT
        navigationTabBar.setBehaviorEnabled(true);

        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {
            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                model.hideBadge();
            }
        });
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

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
        bottomNavigation.setCurrentItem(1);

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
                        Intent in2 = new Intent(mContext, BookmarkActivity.class);
                        startActivity(in2);
                        break;


                }
                return true;
            }
        });
    }
}
