package com.example.myshop.Activities.ItemsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myshop.R;

public class CompleteOrderActivity extends AppCompatActivity {
    public String pName;
    public String pDescription;
    public String pPrice;
    private EditText editEmail;
    private EditText editPhone;
    private EditText editZip;
    private Button buttonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);

        editEmail = findViewById(R.id.edit_email);
        editPhone = findViewById(R.id.edit_phone);
        editZip = findViewById(R.id.edit_zip);
        buttonConfirm = findViewById(R.id.button_confirm);

        pName = getIntent().getStringExtra("name");
        pDescription = getIntent().getStringExtra("description");
        pPrice = getIntent().getStringExtra("price");
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    String email = editEmail.getText().toString().trim();
                    String phone = editPhone.getText().toString().trim();
                    String zip = editZip.getText().toString().trim();

                    if (isValidEmail(email)) {
                        Intent intent = new Intent(CompleteOrderActivity.this, BoughtActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("phone", phone);
                        intent.putExtra("zip", zip);
                        intent.putExtra("name", pName);
                        intent.putExtra("description", pDescription);
                        intent.putExtra("price", pPrice);

                        startActivity(intent);
                    } else {
                        Toast.makeText(CompleteOrderActivity.this, "Некорректный Email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CompleteOrderActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateFields() {
        String email = editEmail.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String zip = editZip.getText().toString().trim();

        return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(zip);
    }

    private boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}