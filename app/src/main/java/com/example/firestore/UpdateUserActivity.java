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

public class UpdateUserActivity extends AppCompatActivity {
    private EditText editName,editEmail,editPass;
    private FirebaseFirestore db;
    private UserVariable userVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        userVariable = (UserVariable) getIntent().getSerializableExtra("Users");
        db = FirebaseFirestore.getInstance();

        editName = (EditText) findViewById(R.id.editName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPass = (EditText) findViewById(R.id.editPassword);
        editName.setText(userVariable.getName());
        editEmail.setText(userVariable.getEmail());
        editPass.setText(userVariable.getPassword());


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
        db.collection("Users").document(userVariable.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(UpdateUserActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(UpdateUserActivity.this, UserListActivity.class));
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

            UserVariable userVariable1 = new UserVariable(name, email, password);

            db.collection("Users").document(userVariable.getId())
                    .set(userVariable1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(UpdateUserActivity.this, "Users Update", Toast.LENGTH_SHORT).show();
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