package com.iit.rentals.hotels;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iit.rentals.R;
import com.iit.rentals.models.Hotel;
import com.iit.rentals.models.HotelService;
import com.iit.rentals.utils.FilePaths;
import com.iit.rentals.utils.FirebaseHelper;
import com.iit.rentals.utils.ImageManager;
import com.iit.rentals.utils.Permissions;
import com.iit.rentals.utils.SharedPreferenceHelper;
import com.iit.rentals.utils.UniversalImageLoader;
import com.iit.rentals.utils.VerifyPermissions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.iit.rentals.utils.SharedPreferenceHelper.getInstance;

public class HotelAddActivity extends AppCompatActivity {
    private static final String TAG = "HotelAddActivity";

    private static final int VERIFY_PERMISSION_REQUEST = 100;
    private Context mContext = HotelAddActivity.this;

    private TextInputEditText name, desc, price, discount, location , contact_no , owner_name;
    private ImageView image;
    private Button btn_save;

    private String s_name, s_desc, s_price, s_discount, s_location, image_link , s_contact_no , s_owner_name;
    private boolean valid;

    private FirebaseHelper mFirebaseHelper;
    private ProgressDialog dialog;
    private String keyId;
    private int mPhotoUploadProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_add);

        mFirebaseHelper = new FirebaseHelper(mContext);
        ImageView main_image = findViewById(R.id.main_image);
        UniversalImageLoader.setImage(FilePaths.IMAGE_URI, main_image, null, "");

        setupToolbar();
        setupWidgets();

        setupProgressDialog();

        initData();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register Hotel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupProgressDialog() {
        dialog = new ProgressDialog(mContext); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
    }


    private void initData() {
        image_link = "";
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerifyPermissions verifyPermissions = new VerifyPermissions(mContext, HotelAddActivity.this);
                if (verifyPermissions.checkPermissionsArray(Permissions.PERMISSIONS)) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, VERIFY_PERMISSION_REQUEST);
                } else {
                    verifyPermissions.verifyPermissionsArray(Permissions.PERMISSIONS);
                }
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_name = name.getText().toString();
                s_desc = desc.getText().toString();
                s_location = location.getText().toString();
                s_price = price.getText().toString();
                s_discount = discount.getText().toString();
                s_contact_no = contact_no.getText().toString();
                s_owner_name = owner_name.getText().toString();

                if (!validateForm()) {
                    Toast.makeText(mContext, "All fields are not set correctly", Toast.LENGTH_SHORT).show();
                } else {

                    savePictureToStorage();
                }
            }
        });
    }

    private boolean validateForm() {
        valid = true;

        if (s_name.isEmpty()) {
            name.setError(mContext.getString(R.string.empty_field));
            valid = false;
        }
        if (s_price.isEmpty()) {
            price.setError(mContext.getString(R.string.empty_field));
            valid = false;
        }
        if (image_link.isEmpty()) {
            Toast.makeText(mContext, "Set an image", Toast.LENGTH_SHORT).show();
            valid = false;
        }


        return valid;
    }

    private void addHotelToDatabase(Uri downloadUrl) {

        showProgressDialog();
//        List<String> stringList = Arrays.asList("asd", "sad");
        List<HotelService> hotelServices = new ArrayList<>();
        hotelServices.add(new HotelService("service1","dsd","sdlfkjsdlkf"));
        hotelServices.add(new HotelService("service2","dsd","sdlfkjsdlkf"));

//        Room post = new Room();
       // post.setCategory_name();
        Hotel post = new Hotel(
                keyId,
                s_name,
                s_desc,
                s_location,
                downloadUrl.toString(),
                FilePaths.HOTEL,
                s_price,
                s_discount,
                s_contact_no,
                s_owner_name,
                "This is owner rules part"
        );


        assert keyId != null;
        mFirebaseHelper.getMyRef().child(FilePaths.HOTEL)
                .child(keyId)
                .setValue(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideProgressDialog();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(mContext, mContext.getString(R.string.error_general), Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });
    }

    private void savePictureToStorage() {
        keyId = mFirebaseHelper.getMyRef().child(FilePaths.HOTEL).push().getKey();
        final StorageReference storageReference = mFirebaseHelper.getmStorageReference()
                .child(FilePaths.HOTEL + "/" + "hotel" + keyId);
        InputStream is = null;
        try {
            is = mContext.getContentResolver().openInputStream(Uri.parse(image_link));

            Bitmap bitmap = BitmapFactory.decodeStream(is);
            byte[] data = ImageManager.getBytesFromBitmap(bitmap, 100);
            UploadTask uploadTask = storageReference.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(mContext, "Image upload failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    if (progress - 30 > mPhotoUploadProgress) {
                        Toast.makeText(mContext, "Photo upload progress: " + String.format("%.0f", progress), Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "onProgress: progress: " + String.format("%.0f", progress) + " % done!");

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Task<Uri> firebaseUrl = storageReference.getDownloadUrl();
                    while (!firebaseUrl.isSuccessful()) ;

                    Uri downloadUrl = firebaseUrl.getResult();

                    SharedPreferenceHelper sharedPreferenceHelper = getInstance(mContext);
                    sharedPreferenceHelper.setAvatar(downloadUrl.toString());
                    Toast.makeText(mContext, "Upload success", Toast.LENGTH_SHORT).show();

                    addHotelToDatabase(downloadUrl);


                }
            });
        } catch (FileNotFoundException e) {
            //            e.printStackTrace();
            Toast.makeText(mContext, "File not found.", Toast.LENGTH_SHORT).show();
        } finally {

            try {
                assert is != null;
                is.close();
            } catch (IOException e) {
                //                e.printStackTrace();
                Toast.makeText(mContext, "Exception while closing file", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void updateHotelImageUrl(Uri downloadUrl) {
        mFirebaseHelper.getMyRef().child(FilePaths.HOTEL)
                .child(keyId)
                .child("image")
                .setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressDialog();
            }
        });
    }


    private void hideProgressDialog() {
        dialog.hide();
    }

    private void showProgressDialog() {
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            image_link = targetUri.toString();

            UniversalImageLoader.setImage(image_link, image, null, "");
//            Bitmap bitmap;
//            try {
//                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
//                image.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
        }
    }

    private void setupWidgets() {
        name = findViewById(R.id.name);
        desc = findViewById(R.id.desc);
        location = findViewById(R.id.location);
        image = findViewById(R.id.image);
        price = findViewById(R.id.price);
        discount = findViewById(R.id.discount);
        contact_no = findViewById(R.id.contact_no);
        owner_name = findViewById(R.id.owner_name);
        btn_save = findViewById(R.id.btn_save);

    }
}
