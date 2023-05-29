package com.example.myshop.Activities.ItemsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myshop.Activities.RoleActivities.MainActivity;
import com.example.myshop.R;

public class SadActivity extends AppCompatActivity {
    private Button buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sad);
        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        TextView textView = findViewById(R.id.text_to_add);
        textView.setText(info);
        buttonBack = findViewById(R.id.button3);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SadActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}