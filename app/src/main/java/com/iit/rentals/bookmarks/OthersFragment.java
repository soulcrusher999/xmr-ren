package com.iit.rentals.bookmarks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iit.rentals.R;
import com.iit.rentals.models.OthersRent;
import com.iit.rentals.models.UserBookmark;
import com.iit.rentals.others.OthersRecyclerAdapter;
import com.iit.rentals.utils.FilePaths;
import com.iit.rentals.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class OthersFragment extends Fragment {

    private Context mContext;
    private View view;

    private List<OthersRent> mList;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private OthersRecyclerAdapter adapter;
    private FirebaseHelper mFirebaseHelper;
    private SwipeRefreshLayout refresh;

    private List<OthersRent> mBookmarkList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_others, container, false);
        mContext = getContext();

        mFirebaseHelper = new FirebaseHelper(mContext);

        setupAdapter();
        loadHousesData();


        return view;
    }

    private void loadHousesData() {
        refresh.setRefreshing(true);
        mFirebaseHelper.getMyRef().child(FilePaths.OTHERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mList.clear();
                        for (DataSnapshot ds :
                                dataSnapshot.getChildren()) {
                            OthersRent post = ds.getValue(OthersRent.class);
                            //we will use not use this list for adapter but store all the data of table others
                            mList.add(post);
                        }

                        mFirebaseHelper.getMyRef().child(FilePaths.BOOKMARK).child(mFirebaseHelper.getUser_id())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        mBookmarkList.clear();
                                        for (DataSnapshot ds :
                                                dataSnapshot.getChildren()) {
                                            UserBookmark userBookmark = ds.getValue(UserBookmark.class);

                                            for (OthersRent othersRent:
                                                    mList) {
                                                if (othersRent.getId().equals(userBookmark.getPost_id())){

                                                    //we will use this list for the adapter
                                                    mBookmarkList.add(othersRent);
                                                }
                                            }
                                            adapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
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

        mList = new ArrayList<>();
        mBookmarkList = new ArrayList<>();
        refresh = view.findViewById(R.id.refresh);
        recyclerView = view.findViewById(R.id.recyclerView);

        manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(manager);

        adapter = new OthersRecyclerAdapter(mContext, mBookmarkList);

        recyclerView.setAdapter(adapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadHousesData();
            }
        });
    }
}
