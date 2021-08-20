package com.example.firestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductsAdapter adapter;
    private List<Product> productList;
    private ProgressBar progressBar;


    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();

        adapter = new ProductsAdapter(this, productList);
        recyclerView.setAdapter(adapter);


        db = FirebaseFirestore.getInstance();


        db.collection("products").get() //
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        progressBar.setVisibility(View.GONE);

                        if(!queryDocumentSnapshots.isEmpty()) // document
                        {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();//  for get all document

                            for (DocumentSnapshot d : list) // one by one
                            {
                                Product product = d.toObject(Product.class);  // convert to object
                                product.setId(d.getId());
                                productList.add(product);

                            }

                            adapter.notifyDataSetChanged();

                        }

                    }
                });




    }
}