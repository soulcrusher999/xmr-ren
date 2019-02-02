package com.iit.rentals.categories;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.iit.rentals.R;
import com.iit.rentals.models.Category;
import com.iit.rentals.utils.FilePaths;
import com.iit.rentals.utils.FirebaseHelper;

public class CategoryAddActivity extends AppCompatActivity {
    private Context mContext = CategoryAddActivity.this;

    private TextInputEditText category_name, category_desc;
    private Button btn_save;
    private String name, desc;

    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        firebaseHelper = new FirebaseHelper(mContext);

        setupToolbar();
        setupWidgets();

        init();

    }

    private void init() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = category_name.getText().toString();
                desc = category_desc.getText().toString();

                if (validateForm()){

                    saveCategoryToDatabase();
                }
            }
        });
    }

    private void saveCategoryToDatabase() {
        //provide a unique primary key
        String keyId = firebaseHelper.getMyRef().child(FilePaths.CATEGORY).push().getKey();

        Category category = new Category(
                keyId,
                name,
                desc,
                "",
                false
        );

        assert keyId != null;
        firebaseHelper.getMyRef().child(FilePaths.CATEGORY)
                .child(keyId)
                .setValue(category).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;
        if (name.isEmpty()){
            category_name.setError(mContext.getString(R.string.empty_field));
            valid = false;
        }

        return valid;
    }

    private void setupWidgets() {
        category_name = findViewById(R.id.category_name);
        category_desc = findViewById(R.id.category_desc);
        btn_save = findViewById(R.id.btn_save);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
