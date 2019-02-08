package com.iit.rentals.hotelBookings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iit.rentals.R;
import com.iit.rentals.models.BookNow;
import com.iit.rentals.utils.FilePaths;
import com.iit.rentals.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class HotelsBookingActivity extends AppCompatActivity {

    private Context mContext = HotelsBookingActivity.this;

    private List<BookNow> mList;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private HotelBookingRecyclerAdapter adapter;
    private FirebaseHelper mFirebaseHelper;
    private SwipeRefreshLayout refresh;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_booking_activity);

        mFirebaseHelper = new FirebaseHelper(mContext);

        setupToolbar();
        setupAdapter();
        loadData();
    }

    private void setupToolbar() {
        TextView toolbar = findViewById(R.id.toolbar);
        toolbar.setText("Hotel Bookmarks");
    }


    private void loadData() {
        refresh.setRefreshing(true);

        mFirebaseHelper.getMyRef().child(FilePaths.BOOKNOW)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mList.clear();
                        for (DataSnapshot ds :
                                dataSnapshot.getChildren()) {
                            BookNow post = ds.getValue(BookNow.class);
                            mList.add(post);
                        }
                        adapter.notifyDataSetChanged();
                        refresh.setRefreshing(false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(mContext, mContext.getString(R.string.error_general), Toast.LENGTH_SHORT).show();
                        refresh.setRefreshing(false);
                    }
                });
    }

    private void setupAdapter() {

        refresh = findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        mList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);

        manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(manager);

        adapter = new HotelBookingRecyclerAdapter(mContext, mList);

        recyclerView.setAdapter(adapter);
    }
}
