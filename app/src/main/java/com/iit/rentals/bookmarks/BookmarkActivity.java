package com.iit.rentals.bookmarks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.iit.rentals.R;
import com.iit.rentals.home.HomeActivity;
import com.iit.rentals.rents.RentsActivity;
import com.iit.rentals.rents.ViewPagerAdapter;
import com.iit.rentals.utils.Navigation;
import com.iit.rentals.utils.SharedPreferenceHelper;
import com.iit.rentals.utils.UserTypes;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class BookmarkActivity extends AppCompatActivity {

    private SharedPreferenceHelper sharedPreferenceHelper;
    private Context mContext = BookmarkActivity.this;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(mContext);


        setupToolbar();
        initUI();
        setupBottomNavigation();


    }


    private void setupToolbar() {
        TextView toolbar = findViewById(R.id.toolbar);
        toolbar.setText("Find Bookmarks");

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
        navigationTabBar.setViewPager(viewPager, 0);

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
        bottomNavigation.setCurrentItem(2);

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
