package com.finalYearProject.votingapp.manager;

import android.util.Log;

import com.finalYearProject.votingapp.model.PreviousElection;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PreviousElectionManager {
    private static final String COLLECTION_NAME = "previous_election";
    private static final String TAG = "PreviousElectionManager";
    private final FirebaseFirestore db;

    public PreviousElectionManager() {
        db = FirebaseFirestore.getInstance();
    }

    public void savePreviousElectionToFirestore(Map<String, PreviousElection> previousElectionMap) {
        for (Map.Entry<String, PreviousElection> entry : previousElectionMap.entrySet()) {
            String winnerOfPost = entry.getKey();
            PreviousElection previous = entry.getValue();

            db.collection(COLLECTION_NAME).document(winnerOfPost)
                    .set(previous)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Winner for " + winnerOfPost + " saved successfully");
                        } else {
                            Log.w(TAG, "Error saving winner of this post: " + winnerOfPost, task.getException());
                        }
                    });
        }
    }

    public void retrievePreviousElectionFromFirestore(String winnerOfPost, PreviousElectionCallback callback) {
        db.collection(COLLECTION_NAME).document(winnerOfPost)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            PreviousElection previousElection = document.toObject(PreviousElection.class);
                            if (previousElection != null) {
                                callback.onSuccess(previousElection);
                            } else {
                                callback.onFailure("Previous election not found for winner " + winnerOfPost);
                            }
                        } else {
                            callback.onFailure("No such document for winner " + winnerOfPost);
                        }
                    } else {
                        callback.onFailure("Error getting document for winner " + winnerOfPost + ": " + task.getException());
                    }
                });
    }

    public void populatePreviousElectionDetails() {
        Map<String, PreviousElection> previousElectionMap = new HashMap<>();
        previousElectionMap.put("winner_president", new PreviousElection("500", "President", "2023", "Amadi, Finbar", "400", "Micheal, B Jordan", "Favour, Grace", "Okoro, Victor"));
        previousElectionMap.put("winner_labour", new PreviousElection("250", "Labour", "2023", "Eze, Bethel", "400", "Dwayne, the rock Johson", "Omaka, Grace", "Okoro, Henry"));
        previousElectionMap.put("winner_utility", new PreviousElection("301", "Utility", "2023", "Megbulem, Susan", "400", "Kevin Hart", "Nnamani, Loveth", "Osimehn, Victor"));
        previousElectionMap.put("winner_dos", new PreviousElection("50", "Director of Socials", "2023", "David", "400", "Batista, Monster", "Obiekwe, Mmesoma", "Okoronkwo, Victory"));
        previousElectionMap.put("winner_doSport", new PreviousElection("150", "Director of Sports", "2023", "Akuba, Ignatus", "400", "Flora, Shaw", "Wonder, Chinweze", "Ezechi, Divine"));
        previousElectionMap.put("winner_treasurer", new PreviousElection("20", "Treasurer", "2023", " Unije, Kingsley", "400", "Omaka Sam", "Awesome, Chinweze", "Treasure, Nwandu"));
        previousElectionMap.put("winner_fs", new PreviousElection("109", "Financial Secretary", "2023", "Okpara, Gabriel", "400", "Chinweze, Chimamanda", "Favour, NNamani", "Ikechukwu, Chinweze"));

        savePreviousElectionToFirestore(previousElectionMap);
    }

    public interface PreviousElectionCallback {
        void onSuccess(PreviousElection previousElection);
        void onFailure(String errorMessage);
    }
}
