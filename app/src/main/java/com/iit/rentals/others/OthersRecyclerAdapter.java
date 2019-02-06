package com.iit.rentals.others;

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
import com.iit.rentals.models.OthersRent;
import com.iit.rentals.rents.RentDetailActivity;
import com.iit.rentals.utils.UniversalImageLoader;

import java.util.List;

public class OthersRecyclerAdapter extends RecyclerView.Adapter<OthersRecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private List<OthersRent> mList;

    public OthersRecyclerAdapter(Context context, List<OthersRent> datas) {
        mContext = context;
        mList = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.snippet_layout_hotel, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OthersRecyclerAdapter.MyViewHolder holder, int position) {

        OthersRent post = mList.get(position);
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
                        Intent intent = new Intent(mContext, RentDetailActivity.class);
                        intent.putExtra(mContext.getString(R.string.calling_hotel_detail),
                                mList.get(getAdapterPosition()));

                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
