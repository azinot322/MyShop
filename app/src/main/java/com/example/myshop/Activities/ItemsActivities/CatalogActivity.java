package com.example.myshop.Activities.ItemsActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myshop.R;
import com.example.myshop.model.ProductViewHolder;
import com.example.myshop.model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

public class CatalogActivity extends AppCompatActivity {
    DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String  id;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

            recyclerView = findViewById(R.id.recycler_menu);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

        }

    private void nextTern(String i) {
        Toast.makeText(CatalogActivity.this, "id " + i +  " ", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef, Products.class).build();


        System.out.println(options);

        
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int i, @NonNull @NotNull Products model) {

                holder.txtProductName.setText(model.getName());


                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Стоимость = " + model.getPrice() + " рублей");

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent catalogIntent = new Intent(CatalogActivity.this, OrderActivity.class);
                        id = ""+holder.getAdapterPosition();
                        catalogIntent.putExtra("uid",id);

                        startActivity(catalogIntent);
                    }
                });

                Picasso.get().load(model.getImage()).into(holder.imageView);
            }

            @NonNull
            @NotNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                //y =+ 1;
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
