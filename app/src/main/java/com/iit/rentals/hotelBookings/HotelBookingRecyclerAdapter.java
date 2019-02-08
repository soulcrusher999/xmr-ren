package com.iit.rentals.hotelBookings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iit.rentals.R;
import com.iit.rentals.models.BookNow;
import com.iit.rentals.utils.UniversalImageLoader;

import java.util.List;

public class HotelBookingRecyclerAdapter extends RecyclerView.Adapter<HotelBookingRecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private List<BookNow> mList;

    public HotelBookingRecyclerAdapter(Context context, List<BookNow> datas) {
        mContext = context;
        mList = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.snippet_layout_hotel_booking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HotelBookingRecyclerAdapter.MyViewHolder holder, int position) {

        BookNow post = mList.get(position);
        UniversalImageLoader.setImage(post.getHotelImage(), holder.image, null, "");
        holder.hotel_name.setText("Hotel Name: "+post.getHotelName());
        holder.owner_name.setText("Hotel Owner name: "+post.getOwnerName());
        holder.owner_contact.setText("Hotel Contact No.:"+post.getOwnerContact());
        holder.username.setText("User Name: "+post.getUserName());
        holder.user_contact.setText("User Contact No.: "+post.getUserContact());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView hotel_name,owner_name,owner_contact,username,user_contact;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);

            hotel_name = view.findViewById(R.id.hotel_name);
            owner_name = view.findViewById(R.id.owner_name);
            owner_contact = view.findViewById(R.id.owner_contact);
            username = view.findViewById(R.id.username);
            user_contact = view.findViewById(R.id.user_contact);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
//                        Intent intent = new Intent(mContext, HotelDetailActivity.class);
//                        intent.putExtra(mContext.getString(R.string.calling_hotel_detail),
//                                mList.get(getAdapterPosition()));
//
//                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
