package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText Name;
    private EditText Email;
    private EditText Password;


    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        Name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);


        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.textView).setOnClickListener(this);
    }

    //button method
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button:
                addUser();
                break;
            case R.id.textView:
                startActivity(new Intent(this, ProductActivity.class));
                break;
        }



    }



    private boolean validateInputs(String name, String brand, String desc) {
        if (name.isEmpty()) {
            Name.setError("Name required");
            Name.requestFocus();
            return true;
        }

        if (brand.isEmpty()) {
            Email.setError("Email required");
            Email.requestFocus();
            return true;
        }

        if (desc.isEmpty()) {
            Password.setError("Password required");
            Password.requestFocus();
            return true;
        }


        return false;
    }


    private void addUser() {

        String name = Name.getText().toString().trim();
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

//  check is it correct
        if (!validateInputs(name, email, password)) {

            //connect with dataBase
            CollectionReference dbProducts = db.collection("Users");

            // set value to Object
            Product product = new Product(name,  email, password );

            dbProducts.add(product)
                    // code for save data
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(MainActivity.this, "User Added", Toast.LENGTH_LONG).show();
                        }
                    })
                    // for some error
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        }

    }
}