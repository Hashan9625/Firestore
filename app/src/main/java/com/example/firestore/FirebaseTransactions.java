package com.example.firestore;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

//    Read operations must come before write operations.
//A function calling a transaction (transaction function) might run more than once if a concurrent edit affects a document that the transaction reads.
//Transaction functions should not directly modify application state.
//Transactions will fail when the client is offline.
public class FirebaseTransactions extends AppCompatActivity implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_transactions);

        findViewById(R.id.runTransaction).setOnClickListener(this);
        findViewById(R.id.outOfTransactions).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.runTransaction:
                runTransaction();
                break;
            case R.id.outOfTransactions:
                outOfTransactions();
                break;
        }
    }

    private void outOfTransactions() {
//The transaction contains read operations after write operations. Read operations must always come before any write operations.
//The transaction read a document that was modified outside of the transaction. In this case, the transaction automatically runs again. The transaction is retried a finite number of times.
//The transaction exceeded the maximum request size of 10 MiB.
//Transaction size depends on the sizes of documents and index entries modified by the transaction. For a delete operation, this includes the size of the target document and the sizes of the index entries deleted in response to the operation.

        final DocumentReference sfDocRef = db.collection("cities").document("DC");

        db.runTransaction(new Transaction.Function<Double>() {
            @Override
            public Double apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                double newPopulation = snapshot.getDouble("population") + 1;
                if (newPopulation <= 4550) {
                    transaction.update(sfDocRef, "population", newPopulation);
                    return newPopulation;
                } else {
                    throw new FirebaseFirestoreException("Population too high",
                            FirebaseFirestoreException.Code.ABORTED);
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<Double>() {
            @Override
            public void onSuccess(Double result) {
                Log.d(TAG, "Transaction success: " + result);
            }
        })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Transaction failure.", e);
                }
            });
    }

    private void runTransaction() {
        final DocumentReference sfDocRef = db.collection("dataSet").document("DC");
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);


                // Note: this could be done without a transaction
                //       by updating the population using FieldValue.increment()
                double newPopulation = snapshot.getDouble("population") + 1;
                if(newPopulation>3522){
                    Map<String, Object> docData = new HashMap<>();
                    docData.put("population2",  100);
                    transaction.set(sfDocRef,docData, SetOptions.merge());
                }
                transaction.update(sfDocRef, "population", newPopulation);

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);
                    }
                });
    }
}