package com.finalYearProject.votingapp.manager;

import android.util.Log;

import com.finalYearProject.votingapp.model.OngoingElection;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class OngoingElectionManager {
    private static final String COLLECTION_NAME = "Ongoing_Election";
    private static final String TAG = "OngoingElectionManager";
    private final FirebaseFirestore db;

    public OngoingElectionManager(FirebaseFirestore db) {
        this.db = db;
    }

    //saving the data in the OngoingElectionManager to the Database
    public void saveOngoingElectionToFirestore(Map<String, OngoingElection> ongoingElectionMap) {
        for (Map.Entry<String, OngoingElection> entry : ongoingElectionMap.entrySet()) {
            String runningForPost = entry.getKey();
            OngoingElection ongoing = entry.getValue();

            db.collection(COLLECTION_NAME).document(runningForPost)
                    .set(ongoing)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            //telling the you that the candidate running for each post has been saved
                            Log.d(TAG, "Candidate for " + runningForPost + " saved successfully");
                        } else {
                            //if it doesn't save, it should tell you
                            Log.w(TAG, "Error saving candidate running for this post: " + runningForPost, task.getException());
                        }
                    });
        }
    }

    public void retrieveOngoingElectionFromFireStore(String runningForPost, OngoingElectionCallback callback) {
        db.collection(COLLECTION_NAME).document(runningForPost)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            OngoingElection ongoingElection = document.toObject(OngoingElection.class);
                            if (ongoingElection != null) {
                                callback.onSuccess(ongoingElection);
                            } else {
                                callback.onFailure("Previous election not found for winner " + runningForPost);
                            }
                        } else {
                            callback.onFailure("No such document for winner " + runningForPost);
                        }
                    } else {
                        callback.onFailure("Error getting document for winner " + runningForPost + ": " + task.getException());
                    }
                });


    }

    public interface OngoingElectionCallback {
        void onSuccess(OngoingElection ongoingElection);

        void onFailure(String errorMessage);
    }
}