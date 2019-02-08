package com.iit.rentals.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iit.rentals.R;
import com.iit.rentals.home.HomeActivity;
import com.iit.rentals.models.User;
import com.iit.rentals.startup.LoginActivity;
import com.iit.rentals.startup.RegisterActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static com.iit.rentals.utils.SharedPreferenceHelper.getInstance;


public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    private static FirebaseHelper instance;

    private final SharedPreferenceHelper sharedPreference;
    private final StorageReference mStorageReference;
    private String user_id;
    private Context mContext;
    private FirebaseAuth mAuth;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private int mPhotoUploadProgress = 0;

    public static FirebaseHelper getFirebaseInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseHelper(context);
        }

        return instance;
    }

    public FirebaseHelper(Context mContext) {
        this.mContext = mContext;
        mAuth = FirebaseAuth.getInstance();
        //inititalize storage

        mStorageReference = FirebaseStorage.getInstance().getReference();
//        initialize the firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        sharedPreference = getInstance(mContext);

        if (mAuth.getCurrentUser() != null) {
            this.user_id = mAuth.getCurrentUser().getUid();
            Log.e(TAG, "FirebaseHelper: " + this.user_id);
        }

        Log.e(TAG, "FirebaseHelper: " + myRef.toString());
    }

    public StorageReference getmStorageReference() {
        return mStorageReference;
    }


    public DatabaseReference getMyRef() {
        return myRef;
    }

    public void setUserID(String userID) {
        this.user_id = userID;
    }


    public void signOut() {
        mAuth.signOut();

        sharedPreference.saveUserInfo(new User("", "", "", UserTypes.NORMAL, "",""));
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(intent);
    }

    public void checkLogin(String email, String password, Activity activity) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            user_id = mAuth.getCurrentUser().getUid();
                            myRef.child("users").child(user_id)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            User user = dataSnapshot.getValue(User.class);
                                            SharedPreferenceHelper preferenceHelper = getInstance(mContext);
                                            preferenceHelper.saveUserInfo(user);
                                            Intent intent = new Intent(mContext, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            mContext.startActivity(intent);
//                                            ((HomeActivity) mContext).finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            ((LoginActivity) mContext).hideProgressBar();
                                        }
                                    });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            String[] exception = task.getException().toString().split(":");
                            Toast.makeText(mContext, exception[1],
                                    Toast.LENGTH_SHORT).show();
                            ((LoginActivity) mContext).hideProgressBar();


                        }


                    }
                });
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    /**
     * sets the selected user by admin to staff
     *
     * @param selected_user_id - user_id of the user selected by the admin in profile activity
     */
    public void setUserAsNormal(String selected_user_id) {
        myRef.child("users")
                .child(selected_user_id)
                .child("user_type")
                .setValue(UserTypes.NORMAL);
    }


    public SharedPreferenceHelper getSharedPreference() {
        return sharedPreference;
    }


    /**
     * Register a new email and password to Firebase Authentication
     *  @param email
     * @param password
     * @param contact
     */
    public void registerNewEmail(final String email, String password, final String username,
                                 final String avatar_img, final String contact) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                            ((RegisterActivity) mContext).hideProgressBar();

                        } else if (task.isSuccessful()) {

                            Toast.makeText(mContext, "Sucessfully registered user. Welcome " + username, Toast.LENGTH_SHORT).show();
                            ((RegisterActivity) mContext).finish();
                            mContext.startActivity(new Intent(mContext, HomeActivity.class));
                            User user = new User(mAuth.getCurrentUser().getUid(),
                                    username,
                                    avatar_img,
                                    UserTypes.NORMAL,
                                    email,
                                    contact
                            );
                            getInstance(mContext).saveUserInfo(user);

                            addUserDetails(getInstance(mContext).getUserInfo());
                            ((RegisterActivity) mContext).hideProgressBar();

                        }

                    }
                });
    }

    private void addUserDetails(User user) {
        myRef.child("users")
                .child(mAuth.getCurrentUser().getUid())
                .setValue(user);

        if (user.getAvatar_img_link().isEmpty()){

            Intent in = new Intent(mContext, HomeActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ((RegisterActivity) mContext).finish();
            mContext.startActivity(in);
        }else {
            FilePaths filePaths = new FilePaths();
            final StorageReference storageReference = mStorageReference
                    .child("users" + "/" + SharedPreferenceHelper.getInstance(mContext).getUID() + "/avatar");
            InputStream is = null;
            try {
                is = mContext.getContentResolver().openInputStream(Uri.parse(user.getAvatar_img_link()));

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
                        myRef.child("users").child(mAuth.getCurrentUser().getUid())
                                .child(mContext.getString(R.string.avatar_link))
                                .setValue(downloadUrl.toString());


                        Intent in = new Intent(mContext, HomeActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ((RegisterActivity) mContext).finish();
                        mContext.startActivity(in);

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


    }

    public String getUser_id() {
        return user_id;
    }
}
