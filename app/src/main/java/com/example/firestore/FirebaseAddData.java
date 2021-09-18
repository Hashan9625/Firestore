package com.example.firestore;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseAddData extends AppCompatActivity implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText editText1;
    EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);

        findViewById(R.id.merge).setOnClickListener(this);
        findViewById(R.id.dataTypes).setOnClickListener(this);
        findViewById(R.id.updateFire).setOnClickListener(this);
        findViewById(R.id.UpdateElementsInAnArray).setOnClickListener(this);
        findViewById(R.id.IncrementANumericValue).setOnClickListener(this);
        findViewById(R.id.whereEqualTo).setOnClickListener(this);
        findViewById(R.id.multipleDocuments).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.merge:
                marge();
                break;
            case R.id.dataTypes:
                dataTypes();
                break;         
            case R.id.updateFire:
                updateFire();
                break;
            case R.id.UpdateElementsInAnArray:
                UpdateElementsInAnArray();
                break;
           case R.id.IncrementANumericValue:
               IncrementANumericValue();
                break;
           case R.id.whereEqualTo:
               whereEqualTo();
                break;
           case R.id.multipleDocuments:
               multipleDocuments();
                break;

        }

    }

    private void multipleDocuments() {
        db.collection("dataSet")
                .whereEqualTo("id", "100")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (value.isEmpty()){
                            Map<String, Object> docData = new HashMap<>();
                            docData.put("population",  "new");
                            docData.put("id",  "100");
                            db.collection("dataSet").add(docData);
                        }


                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("hii") != null) {
                                Map<String, Object> docData = new HashMap<>();
                                docData.put("population",  3);
                                db.collection("dataSet").document(doc.getId()).set(docData, SetOptions.merge());
                            }
                        }
                      //  Log.d(TAG, "Current cites in CA: " + cities);
                    }
                });
    }

    private void whereEqualTo() {
        Log.d(TAG,"okkkk");
        db.collection("dataSet")
                .whereEqualTo("id", "10")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"error  "+e.getMessage().toString());
            }
        });
    }

    private void IncrementANumericValue() {
        DocumentReference washingtonRef = db.collection("cities").document("DC");
//        Map<String, Object> docData = new HashMap<>();
//        docData.put("population",  3);
//        washingtonRef.set(docData, SetOptions.merge());

// Atomically increment the population of the city by 50.
        washingtonRef.update("population", FieldValue.increment(50)); //Increment operations are useful for implementing counters, but keep in mind that you can update a single document only once per second. If you need to update your counter above this rate, see the Distributed counters page.
    }

    private void UpdateElementsInAnArray() {
        DocumentReference washingtonRef = db.collection("cities").document("DC");

        Map<String, Object> docData = new HashMap<>();
        docData.put("regions", Arrays.asList(1, 2, 3));
        washingtonRef.set(docData, SetOptions.merge());
// Atomically add a new region to the "regions" array field.
        // array eke reater_virginia kiyala value ekak naththan meka add wenawa
        washingtonRef.update("regions", FieldValue.arrayUnion("greater_virginia"));

// Atomically remove a region from the "regions" array field.  3 hoyala ain karanawa
        washingtonRef.update("regions", FieldValue.arrayRemove(3));
    }

    private void updateFire() {
        DocumentReference washingtonRef = db.collection("cities").document("BJ");   // In some cases, it can be useful to create a document reference with an auto-generated ID, then use the reference later. For this use case, you can call doc():

        washingtonRef
                .update(
                        "age", 13,
                        "capital", false
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

    }


    private void dataTypes() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("stringExample", "Hello world!");
        docData.put("booleanExample", true);
        docData.put("numberExample", 3.14159265);
        docData.put("dateExample", new Timestamp(new Date()));
        docData.put("serverTimestamp", FieldValue.serverTimestamp());  // When updating multiple timestamp fields inside of a transaction, each field receives the same server timestamp value.
        docData.put("listExample", Arrays.asList(1, 2, 3));
        docData.put("nullExample", null);

        Map<String, Object> nestedData = new HashMap<>();
        nestedData.put("a", 5);
        nestedData.put("b", true);

        docData.put("objectExample", nestedData);

        db.collection("data")//.document("one")
                .add(docData)  //    Unlike "push IDs" in the Firebase Realtime Database, Cloud Firestore auto-generated IDs do not provide any automatic ordering. If you want to be able to order your documents by creation date, you should store a timestamp as a field in the documents.
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>()  {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void marge() {
        String id = editText1.getText().toString();
        String value = editText2.getText().toString();

        Map<String, Object> data = new HashMap<>();
//        data.put("capital", true);
        data.put(id, value);

        db.collection("dataSet")//.document("BJ")
                .add(data)//, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}