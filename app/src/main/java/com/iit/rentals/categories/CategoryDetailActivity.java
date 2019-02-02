package com.iit.rentals.categories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.iit.rentals.R;
import com.iit.rentals.models.Hotel;

public class CategoryDetailActivity extends AppCompatActivity {

    private Intent in;
    private Context mContext = CategoryDetailActivity.this;
    private Hotel hotel;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        getIncomingIntent();

        setupWidgets();
    }

    private void setupWidgets() {
        title = findViewById(R.id.title);
        title.setText(hotel.getName());
    }

    private void getIncomingIntent() {
        in = getIntent();

        hotel = in.getParcelableExtra(mContext.getString(R.string.calling_hotel_detail));


    }
}
