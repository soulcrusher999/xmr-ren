package com.iit.rentals.categories;

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
import com.iit.rentals.models.Category;
import com.iit.rentals.rents.RentsActivity;
import com.iit.rentals.utils.UniversalImageLoader;

import java.util.List;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private List<Category> mList;

    public CategoryRecyclerAdapter(Context context, List<Category> datas) {
        mContext = context;
        mList = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.snippet_layout_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerAdapter.MyViewHolder holder, int position) {

        Category post = mList.get(position);
        UniversalImageLoader.setImage(post.getPicture(), holder.category_image, null, "");
        holder.category_name.setText(post.getName());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView category_image;
        TextView category_name;

        public MyViewHolder(View view) {
            super(view);
            category_image = view.findViewById(R.id.category_image);
            category_name = view.findViewById(R.id.category_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

//                        if (mList.get(getAdapterPosition()).getName() == FilePaths.HOTEL) {

                            Intent intent = new Intent(mContext, RentsActivity.class);
                            intent.putExtra(mContext.getString(R.string.calling_category),
                                    mList.get(getAdapterPosition()));
                            mContext.startActivity(intent);
//                        }


                    }
                }
            });
        }
    }
}
