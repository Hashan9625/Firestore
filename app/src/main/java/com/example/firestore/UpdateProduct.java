package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateProduct extends AppCompatActivity {
    private EditText editName,editEmail,editPass;
    private FirebaseFirestore db;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        product = (Product) getIntent().getSerializableExtra("Users");
        db = FirebaseFirestore.getInstance();

        editName = (EditText) findViewById(R.id.editName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPass = (EditText) findViewById(R.id.editPassword);
        editName.setText(product.getName());
        editEmail.setText(product.getEmail());
        editPass.setText(product.getPassword());


        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });

    }

    private void deleteProduct() {
        db.collection("Users").document(product.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(UpdateProduct.this, "deleted", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(UpdateProduct.this,ProductActivity.class));
                        }
                    }
                });
    }

    private void updateProduct() {

        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPass.getText().toString().trim();

//  check is it correct
        if (!validateInputs(name, email, password))
        {

            Product product1 = new Product(name, email, password);

            db.collection("Users").document(product.getId())
                    .set(product1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(UpdateProduct.this, "Users Update", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

        }
    }

    private boolean validateInputs(String name, String email, String password) {
        if (name.isEmpty()) {
            editName.setError("Name required");
            editName.requestFocus();
            return true;
        }

        if (email.isEmpty()) {
            editEmail.setError("Email required");
            editEmail.requestFocus();
            return true;
        }

        if (password.isEmpty()) {
            editPass.setError("Password required");
            editPass.requestFocus();
            return true;
        }


        return false;
    }


}