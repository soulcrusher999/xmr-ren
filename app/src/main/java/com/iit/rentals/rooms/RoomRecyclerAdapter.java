package com.iit.rentals.rooms;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iit.rentals.R;
import com.iit.rentals.hotels.HotelDetailActivity;
import com.iit.rentals.models.Room;
import com.iit.rentals.utils.UniversalImageLoader;

import java.util.List;

public class RoomRecyclerAdapter extends RecyclerView.Adapter<RoomRecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private List<Room> mList;

    public RoomRecyclerAdapter(Context context, List<Room> datas) {
        mContext = context;
        mList = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.snippet_layout_room, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoomRecyclerAdapter.MyViewHolder holder, int position) {
        Room post = mList.get(position);
        UniversalImageLoader.setImage(post.getImage(), holder.image, null, "");
        holder.title.setText(post.getName());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(mContext, HotelDetailActivity.class);
                        intent.putExtra(mContext.getString(R.string.calling_hotel_detail),
                                mList.get(getAdapterPosition()));

                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
