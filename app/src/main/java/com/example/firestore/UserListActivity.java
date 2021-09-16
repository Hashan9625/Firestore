package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserListAdapter adapter;
    private List<UserVariable> userVariableList;  // that list used to store data and it send to RecyclerView
    private ProgressBar progressBar;
    private DocumentSnapshot lastVisible;
    private LinearLayoutManager linearLayoutManager;
    private boolean isScrolling;
    private boolean isLastItemReached;


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
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userVariableList = new ArrayList<>();  // array list
        db = FirebaseFirestore.getInstance();

        adapter = new UserListAdapter(this, userVariableList);


//        db.collection("Users").get() // to get data from database
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @SuppressLint("NotifyDataSetChanged")
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                        //progressBar.setVisibility(View.GONE);
//
//                        if(!queryDocumentSnapshots.isEmpty()) // document
//                        {
//                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();    //  for get all document
//
//                            for (DocumentSnapshot d : list) // one by one
//                            {
//                                UserVariable userVariable = d.toObject(UserVariable.class);  // convert to object
//                                userVariable.setId(d.getId());
//                                userVariableList.add(userVariable);
//
//                            }
//
//                            adapter.notifyDataSetChanged();
//
//                        }
//
//                    }
//                });
//
//
//
//        recyclerView.setAdapter(adapter);




        // for pagination

        Query query = FirebaseFirestore.getInstance().collection("Users")
                .orderBy("password",Query.Direction.ASCENDING)
                .limit(12);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    progressBar.setVisibility(View.GONE);

                    for (DocumentSnapshot documentSnapshot : task.getResult())
                    {
                        UserVariable userVariable = documentSnapshot.toObject(UserVariable.class);
                        userVariable.setId(documentSnapshot.getId());
                        userVariableList.add(userVariable);
                    }


                    recyclerView.setAdapter(adapter);
                    lastVisible = task.getResult().getDocuments().get(task.getResult().size()-1);



                    Toast.makeText(getApplicationContext(),"First page loaded",Toast.LENGTH_SHORT).show();

                    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);

                            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                            {
                                isScrolling = true;
                            }
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            int firstVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                            int visibleItemCount = linearLayoutManager.getChildCount();
                            int totalItemCount = linearLayoutManager.getItemCount();

                            if(isScrolling && !isLastItemReached)
                            {
                                isScrolling = false;
                                Query nextQuery = FirebaseFirestore.getInstance().collection("Users")
                                        .orderBy("password",Query.Direction.ASCENDING)
                                        .startAfter(lastVisible)
                                        .limit(12);

                                nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                                        for (DocumentSnapshot documentSnapshot : task.getResult())
                                        {
                                            UserVariable userVariable = documentSnapshot.toObject(UserVariable.class);
                                            userVariable.setId(documentSnapshot.getId());
                                            userVariableList.add(userVariable);
                                        }

                                        adapter.notifyDataSetChanged();

                                        lastVisible = task.getResult().getDocuments().get(task.getResult().size()-1);
                                        Toast.makeText(getApplicationContext(),"Next page loaded",Toast.LENGTH_SHORT).show();

                                        if(task.getResult().size()<12)
                                        {
                                            isLastItemReached = true;
                                        }

                                    }
                                });
                            }
                        }
                    };

                    recyclerView.addOnScrollListener(onScrollListener);
                }

            }
        });


        

    }
}