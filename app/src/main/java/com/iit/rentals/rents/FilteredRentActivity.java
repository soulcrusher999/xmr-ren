package com.iit.rentals.rents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iit.rentals.R;
import com.iit.rentals.models.Category;
import com.iit.rentals.models.OthersRent;
import com.iit.rentals.others.RecyclerAdapter;
import com.iit.rentals.utils.FilePaths;
import com.iit.rentals.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class FilteredRentActivity extends AppCompatActivity {

    private Context mContext = FilteredRentActivity.this;

    private FirebaseHelper firebaseHelper;
    private Intent in;
    private Category category;
    private FragmentManager fragmentManager;
    private FrameLayout main_frame;
    private RelativeLayout relRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        main_frame = findViewById(R.id.main_frame);
        relRecycler = findViewById(R.id.relRecycler);

        firebaseHelper = new FirebaseHelper(mContext);

        getIncomingIntent();


        setupAdapter();
        loadData();
    }

    private RecyclerAdapter adapter;

    private List<OthersRent> mList;
    private SwipeRefreshLayout refresh;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private void setupAdapter() {
        mList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);

        manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(manager);

        adapter = new RecyclerAdapter(mContext, mList);

        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        firebaseHelper.getMyRef().child(FilePaths.OTHERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds :
                                dataSnapshot.getChildren()) {
                            OthersRent post = ds.getValue(OthersRent.class);
                            mList.add(post);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(mContext, mContext.getString(R.string.error_general), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getIncomingIntent() {
        in = getIntent();

        fragmentManager = getSupportFragmentManager();
        category = in.getParcelableExtra(mContext.getString(R.string.calling_category));

        if (category.getName().equals(FilePaths.HOTEL)) {
            relRecycler.setVisibility(View.VISIBLE);
            main_frame.setVisibility(View.GONE);
            loadHotelFragment();
        } else if (category.getName().equals(FilePaths.ROOM)) {
            relRecycler.setVisibility(View.VISIBLE);
            main_frame.setVisibility(View.GONE);
            loadRoomFragment();
        } else {
            relRecycler.setVisibility(View.GONE);
            main_frame.setVisibility(View.VISIBLE);
            setupAdapter();
        }
    }

    private void loadRoomFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame, new RoomFragment())
                .commit();
    }

    private void loadHotelFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame, new HotelsFragment())
                .commit();
    }
}
