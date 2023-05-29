package com.example.myshop.Activities.ItemsActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myshop.R;
import com.example.myshop.model.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    DatabaseReference ProductsRef;
    public String realId;
    public String pName;
    public String pDescription;
    public String pPrice;
    String idFromRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Button btn = findViewById(R.id.button321);
        Button btn2 = findViewById(R.id.buttonBought);
        TextView textView = findViewById(R.id.testView);
        TextView testView2 = findViewById(R.id.testView2);
        TextView testView3 = findViewById(R.id.testView3);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent catalogIntent = new Intent(OrderActivity.this, CatalogActivity.class);
                startActivity(catalogIntent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent compIntent = new Intent(OrderActivity.this, CompleteOrderActivity.class);
                compIntent.putExtra("name",pName);
                compIntent.putExtra("description",pDescription);
                compIntent.putExtra("price",pPrice);
                startActivity(compIntent);
            }
        });


        idFromRecycler = getIntent().getStringExtra("uid");
        Query citiesQuery = ProductsRef;
        citiesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> products = new ArrayList<String>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    products.add(postSnapshot.child("id").getValue().toString());
                }
                realId = products.get(Integer.parseInt(idFromRecycler));
                Products buyProduct = dataSnapshot.child(realId).getValue(Products.class);
                pName = buyProduct.getName();
                pDescription = buyProduct.getDescription();
                pPrice = buyProduct.getPrice();
                textView.setText(pName);
                testView2.setText(pDescription);
                testView3.setText(pPrice+" \nРуб.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}

