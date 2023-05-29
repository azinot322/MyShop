package com.example.myshop.Activities.RoleActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myshop.Activities.ItemsActivities.CatalogActivity;
import com.example.myshop.R;
import com.example.myshop.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText usernameInput, passwordInput;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private String parentDbName = "Users";
    private TextView adminLink, notAdminLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Button regButton = findViewById(R.id.register_btn);
        Button loginButton = findViewById(R.id.login_btn);
        usernameInput = findViewById(R.id.login_login_input);
        passwordInput = findViewById(R.id.login_password_input);
        adminLink = findViewById(R.id.admin_panel_link);
        notAdminLink = findViewById(R.id.not_admin_panel_link);
        regButton.setOnClickListener(v -> {
            Intent regIntent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(regIntent);
        });

        loginButton.setOnClickListener(v -> signIn());
        adminLink.setOnClickListener(v -> {
            adminLink.setVisibility(View.INVISIBLE);
            notAdminLink.setVisibility(View.VISIBLE);
            parentDbName="Admins";
        });
        notAdminLink.setOnClickListener(v -> {
            notAdminLink.setVisibility(View.INVISIBLE);
            adminLink.setVisibility(View.VISIBLE);
            parentDbName = "Users";
        });
    }

    private void signIn() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();
        }
        else
        {

            checkUsername(username, password);
        }
    }

    private void checkUsername(String username, String password) {
        final DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(username).exists())
                {
                    User userInfo = snapshot.child(parentDbName).child(username).getValue(User.class);

                    if (userInfo.getPassword().equals(password))
                    {
                        if (parentDbName.equals("Users")){

                            Intent catalogIntent = new Intent(MainActivity.this, CatalogActivity.class);
                            startActivity(catalogIntent);
                    }
                        if (parentDbName.equals("Admins")) {

                            Intent catalogIntent = new Intent(MainActivity.this, AdminEditProduct.class);
                            startActivity(catalogIntent);
                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Неизвестный логин", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}