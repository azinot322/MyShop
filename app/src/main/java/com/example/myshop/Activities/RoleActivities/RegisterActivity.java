package com.example.myshop.Activities.RoleActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button registerBtn, loginButton;
    private EditText usernameInput,  passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerBtn = findViewById(R.id.register_btn);
        loginButton = findViewById(R.id.button2);
        usernameInput = findViewById(R.id.reg_login_input);
        passwordInput = findViewById(R.id.reg_password_input);
        registerBtn.setOnClickListener(v -> createAccount());
        loginButton.setOnClickListener(v -> {
            Intent regIntent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(regIntent);
        });
    }




    private void createAccount() {
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

    private void checkUsername(final String username, final String password) {
        final DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(username).exists()))
                {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("name", username );
                    userDataMap.put("password", password );

                    reference.child("Users").child(username).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(mainIntent);
                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this, "Ошибка.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {

                    Toast.makeText(RegisterActivity.this, "Никнейм " + username + " уже зарегистрирован", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}