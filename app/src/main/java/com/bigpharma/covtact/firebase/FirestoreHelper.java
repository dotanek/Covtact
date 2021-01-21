package com.bigpharma.covtact.firebase;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bigpharma.covtact.LoginActivity;
import com.bigpharma.covtact.RegisterActivity;
import com.bigpharma.covtact.model.PathModel;
import com.bigpharma.covtact.model.PathPointModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class FirestoreHelper {
    private FirebaseFirestore fs = FirebaseFirestore.getInstance();
    private List<PathPointModel> tmp = new ArrayList<PathPointModel>();
    List<List<PathPointModel>> allPaths = new ArrayList<List<PathPointModel>>();

    public FirestoreHelper() {
        intializeSnapshot();
    }

    public void intializeSnapshot() {


    }




    public List<List<PathPointModel>> getVirusPaths(){
        getThings();
        return allPaths;
    }





    public void getThings() {


        fs.collection("VirusPaths")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                List<PathPointModel> path = new ArrayList<PathPointModel>();
                                if ((!document.getData().toString().equals(FirebaseAuth.getInstance().getUid()))) {
                                    document.getReference().collection("pathPoints").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (DocumentSnapshot doc : task.getResult()
                                                ) {
                                                    tmp.add(doc.toObject(PathPointModel.class));
                                                    Log.d(TAG, doc.getId() + " => " + doc.toObject(PathPointModel.class).toString());
                                                }
                                            }
                                        }
                                    });
                                }
                                allPaths.add(tmp);
                                tmp.clear();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }






    /*
        fs.collection("VirusPaths")
                .whereNotEqualTo("AuthorID", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.toObject(PathPointModel.class));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

     */

/*
        fs.collection("VirusPaths").whereNotEqualTo("AuthorID", FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()
                    ) {
                        Log.d(TAG, "Document successfully ridden!" + doc.getData().toString());
                        if (!doc.getData().toString().equals(FirebaseAuth.getInstance().getUid())) {
                            Log.d(TAG, "Document successfully ridden!" + doc.getData().toString());
                            tmp.add(doc.getData().toString());
                        }
                    }
                } else Log.d(TAG, "Donnnnkkan!");
            }
        });

*/


    private void getPath(String uuid) {

        Log.d(TAG, "Im here" + uuid);
        fs.collection("VirusPaths").document(uuid).collection("pathPoints").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()
                    ) {
                        Log.d(TAG, "Document successfully!" + doc.toObject(PathPointModel.class).toString());
                    }

                }
            }
        });
    }

    public void createUser(String name, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("lastChecked", null);
        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }


    private void PublishPathPoint(PathPointModel point) {
        FirebaseFirestore.getInstance().collection("VirusPaths")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("pathPoints")
                .add(point.toMap())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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

    public List<PathPointModel> genaratePath() {
        List<PathPointModel> path = new ArrayList<PathPointModel>();

        for (int i = 0; i < 10; i++) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);
            calendar.set(year, month, day, 20, i * 5, 00);
            path.add(new PathPointModel(calendar.getTime(), 0.0 + i * 0.0001, 0.0 + i * 0.0001));
        }
        return path;
    }

    public void PublishPath(List<PathPointModel> path) {


        Map<String, Object> creator = new HashMap<>();
        creator.put("CreatorID", FirebaseAuth.getInstance().getUid());
        FirebaseFirestore.getInstance().collection("VirusPaths")
                .document(FirebaseAuth.getInstance().getUid())
                .set(creator)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        for (PathPointModel point : path
        ) {
            PublishPathPoint(point);
        }

    }

    public PathModel getNextVirusPath() {


        return null;
    }
}
