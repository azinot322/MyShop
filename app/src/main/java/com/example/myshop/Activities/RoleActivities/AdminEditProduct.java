package com.example.myshop.Activities.RoleActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myshop.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class AdminEditProduct extends AppCompatActivity {
    private ImageView productImage;
    private static final int GALLERYPICK = 1;
    private String description, price, name, saveCurrentDate, saveCurrentTime, productKey;
    private String downloadImageUrl;
    private EditText productName, productDescription, productPrice;
    private Button addProduct;
    private Uri ImageUri;
    private StorageReference imgRef;
    private DatabaseReference ProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_product);

        
        initialization();

        productImage.setOnClickListener(v -> OpenGallery());

        addProduct.setOnClickListener(v -> CheckProduct());


    }

    private void CheckProduct() {
        description = productDescription.getText().toString();
        name = productName.getText().toString();
        price = productPrice.getText().toString();

        if(ImageUri == null){
            Toast.makeText(this,"Нет картинки",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description)){
            Toast.makeText(this,"Нет описания",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(price)){
            Toast.makeText(this,"Нет цены",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Нет названия",Toast.LENGTH_SHORT).show();
        }
        else {
            AddInfoInDb();
        }


    }

    private void AddInfoInDb() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("ddMMyyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HHmmss");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productKey = saveCurrentDate + saveCurrentTime;

        StorageReference filePath = imgRef.child(ImageUri.getLastPathSegment()+ productKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(e -> Toast.makeText(AdminEditProduct.this,"Не удалось загрузить товар",Toast.LENGTH_SHORT).show()).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(AdminEditProduct.this,"Успешно загружено",Toast.LENGTH_SHORT).show();

            Task<Uri> uriTask = uploadTask.continueWithTask(task -> {
                if (task.isCanceled()){
                    throw Objects.requireNonNull(task.getException());
                }
                downloadImageUrl= filePath.getDownloadUrl().toString();
                return filePath.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    downloadImageUrl = Objects.requireNonNull(task.getResult()).toString();

                    SaveProductInfoToDatabase();
                }
            });
        });
    }


    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERYPICK);
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("id", productKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", description);
        productMap.put("image", downloadImageUrl);
        productMap.put("price", price);
        productMap.put("name", name);
        ProductsRef.child(productKey).updateChildren(productMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminEditProduct.this, "Товар добавлен", Toast.LENGTH_SHORT).show();
                        Intent loginIntent = new Intent(AdminEditProduct.this, MainActivity.class);
                        startActivity(loginIntent);
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(AdminEditProduct.this, "Ошибка: " + message, Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERYPICK && resultCode== RESULT_OK && data != null){
            ImageUri =data.getData();
            productImage.setImageURI(ImageUri);

        }
    }

    private void initialization() {
        productImage = findViewById(R.id.select_product_image);
        productName = findViewById(R.id.product_name);
        productDescription = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        addProduct =findViewById(R.id.btn_add_new_product);
        imgRef = FirebaseStorage.getInstance().getReference().child("Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
    }
}

