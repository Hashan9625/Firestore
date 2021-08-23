package com.example.firestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserListAdapter adapter;
    private List<UserVariable> userVariableList;  // that list used to store data and it send to RecyclerView
    private ProgressBar progressBar;


    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        

    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataBaseData();
    }

    private void getDataBaseData() {

        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userVariableList = new ArrayList<>();  // array list
        db = FirebaseFirestore.getInstance();
        adapter = new UserListAdapter(this, userVariableList);  // RecyclerView object



        db.collection("Users").get() // to get data from database
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        progressBar.setVisibility(View.GONE);

                        if(!queryDocumentSnapshots.isEmpty()) // document
                        {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();    //  for get all document

                            for (DocumentSnapshot d : list) // one by one
                            {
                                UserVariable userVariable = d.toObject(UserVariable.class);  // convert to object
                                userVariable.setId(d.getId());
                                userVariableList.add(userVariable);

                            }

                            adapter.notifyDataSetChanged();

                        }

                    }
                });



        recyclerView.setAdapter(adapter);


    }
}