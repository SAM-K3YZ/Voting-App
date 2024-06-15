package com.finalYearProject.votingapp.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalYearProject.votingapp.R;
import com.finalYearProject.votingapp.adapters.PreviousElectionAdapter;
import com.finalYearProject.votingapp.model.PreviousElection;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PreviousElectionViewAllActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PreviousElectionAdapter adapter;
    private List<PreviousElection> previousElectionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_previous_election_view_all);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(PreviousElectionViewAllActivity.this, 2));
        previousElectionList = new ArrayList<>();
        adapter = new PreviousElectionAdapter(previousElectionList, null, false);
        recyclerView.setAdapter(adapter);

        fetchPreviousElections();
    }

    private void fetchPreviousElections() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("previous_election")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                        if (error != null) {
                            System.err.println("Listen failed: " + error);
                            return;
                        }

                        previousElectionList.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            if (doc.exists()) {
                                PreviousElection election = doc.toObject(PreviousElection.class);
                                previousElectionList.add(election);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}