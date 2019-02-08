package com.iit.rentals.hotels;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.iit.rentals.R;
import com.iit.rentals.models.BookNow;
import com.iit.rentals.models.Hotel;
import com.iit.rentals.models.UserBookmark;
import com.iit.rentals.utils.FilePaths;
import com.iit.rentals.utils.FirebaseHelper;
import com.iit.rentals.utils.SharedPreferenceHelper;
import com.iit.rentals.utils.UniversalImageLoader;
import com.iit.rentals.utils.UserTypes;

public class HotelDetailActivity extends AppCompatActivity {

    private Intent in;
    private Context mContext = HotelDetailActivity.this;
    private Hotel post;
    private TextView roomName, location, price, contact_no, owner_name, discount, total_price, bookmark;
    private ImageView house_pic;
    private Button call_now;
    private FirebaseHelper firebaseHelper;
    private Button book_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_houses_detail);

        firebaseHelper = new FirebaseHelper(mContext);

        setupWidgets();
        getIncomingIntent();
        setupToolbar();

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupWidgets() {
//        title = findViewById(R.id.title);
//        title.setText(post.getName());

        house_pic = findViewById(R.id.house_pic);
        roomName = findViewById(R.id.roomName);
        location = findViewById(R.id.location);
        price = findViewById(R.id.price);
        discount = findViewById(R.id.discount);
        total_price = findViewById(R.id.total_price);
        call_now = findViewById(R.id.call_now);
        book_now = findViewById(R.id.book_now);
        contact_no = findViewById(R.id.contact_no);
        owner_name = findViewById(R.id.owner_name);
        bookmark = findViewById(R.id.bookmark);

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String keyId = firebaseHelper.getMyRef().child(FilePaths.BOOKMARK)
                        .child(userId)
                        .push().getKey();
                UserBookmark userBookmark = new UserBookmark();
                userBookmark.setId(keyId);
                userBookmark.setCategoryName(FilePaths.HOTEL);
                userBookmark.setPost_id(post.getId());
                userBookmark.setUser_id(userId);


                firebaseHelper.getMyRef().child(FilePaths.BOOKMARK)
                        .child(userId)
                        .child(keyId)
                        .setValue(userBookmark).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void getIncomingIntent() {
        in = getIntent();

        post = in.getParcelableExtra(mContext.getString(R.string.calling_hotel_detail));

        int idiscount = ((Integer.parseInt(post.getDiscount()) * Integer.parseInt(post.getPrice()) / 100));
        int result = Integer.parseInt(post.getPrice()) - idiscount;

        UniversalImageLoader.setImage(post.getImage(), house_pic, null, "");
        post = in.getParcelableExtra(mContext.getString(R.string.calling_hotel_detail));
        roomName.setText(post.getName());
        location.setText(post.getLocation());
        price.setText(post.getPrice());
        discount.setText(post.getDiscount());
        total_price.setText(result + "");
        contact_no.setText(post.getContact_no() + "");
        owner_name.setText(post.getOwner_name());

        call_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + post.getContact_no()));
                startActivity(intent);
            }


        });

        book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveBookingToDatabase();
            }


        });
    }

    private void saveBookingToDatabase() {

        String keyId = firebaseHelper.getMyRef().child(FilePaths.BOOKNOW).push().getKey();
        BookNow bookPost = new BookNow(
          keyId,
          FirebaseAuth.getInstance().getCurrentUser().getUid(),
          post.getId(),
          post.getName(),
          post.getImage(),
          post.getOwner_name(),
          post.getContact_no(),
          SharedPreferenceHelper.getInstance(mContext).getUserInfo().getUsername(),
          SharedPreferenceHelper.getInstance(mContext).getUserInfo().getContact()
        );

        firebaseHelper.getMyRef().child(FilePaths.BOOKNOW)
                .child(keyId)
                .setValue(bookPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                book_now.setText("Booked");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete_menu, menu);
//        menu.findItem(R.id.edit_post_action).setVisible(false);
//        menu.findItem(R.id.delete_post_action).setVisible(false);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //TODO instead of directly deleting post by admin eiher flag the content or send msg of deletion
        if (SharedPreferenceHelper.getInstance(mContext).getUserType() == UserTypes.ADMIN) {
            menu.findItem(R.id.delete).setVisible(true);
        } else {
            menu.findItem(R.id.delete).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete:

                confirmDelDialog();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void confirmDelDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        builder.setTitle("Delete post " + post.getName() + " ?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                firebaseHelper.getMyRef().child(FilePaths.HOTEL).child(post.getId())
                        .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.cancel();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
