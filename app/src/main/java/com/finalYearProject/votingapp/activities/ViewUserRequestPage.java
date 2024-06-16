package com.finalYearProject.votingapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalYearProject.votingapp.R;
import com.finalYearProject.votingapp.adapters.RequestsAdapter;
import com.finalYearProject.votingapp.model.Candidate;
import com.finalYearProject.votingapp.model.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewUserRequestPage extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    RequestsAdapter requestsAdapter;
    RecyclerView recyclerViewRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_user_request_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewRequests = findViewById(R.id.notificationRecycler);
        recyclerViewRequests.setLayoutManager(new LinearLayoutManager(ViewUserRequestPage.this));
        //fetching the request(s) sent by candidates(user)
        fetchRequests();
    }

    private void fetchRequests() {
        firebaseFirestore.collection("Requests")
                .whereEqualTo("isApproved", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Request> requests = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Request request = document.toObject(Request.class);
                                requests.add(request);
                            }
                            requestsAdapter = new RequestsAdapter(requests, new RequestsAdapter.OnRequestActionListener() {
                                @Override
                                public void onApprove(Request request) {
                                    approveRequest(request);
                                }

                                @Override
                                public void onDecline(Request request) {
                                    declineRequest(request);
                                }
                            });
                            recyclerViewRequests.setAdapter(requestsAdapter);
                        } else {
                            Log.w("AdminRequestsActivity", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void approveRequest(Request request) {
        firebaseFirestore.collection("Requests").document(request.getUserId())
                .update("isApproved", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        addToCandidates(request);
                        Toast.makeText(ViewUserRequestPage.this, "Request approved", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void declineRequest(Request request) {
        firebaseFirestore.collection("Requests").document(request.getUserId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ViewUserRequestPage.this, "Request declined", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addToCandidates(final Request request) {
        firebaseFirestore.collection("Users").document(request.getUserId()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Fetch user details
                                String name = document.getString("name");
                                String department = document.getString("department");
                                String level = document.getString("level");
                                String manifesto = document.getString("manifesto");
                                String gender = document.getString("gender");
                                String id = document.getId();

                                // Create a Candidate object
                                Candidate candidate = new Candidate(name, department, request.getCategory(), level, manifesto, gender, id);

                                // Add to the Candidates collection
                                firebaseFirestore.collection("Candidates").document(id).set(candidate)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(ViewUserRequestPage.this, "Candidate added successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ViewUserRequestPage.this, "Error adding candidate", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Log.w("VoteFragment", "Error getting user details.", task.getException());
                        }
                    }
                });
    }
}