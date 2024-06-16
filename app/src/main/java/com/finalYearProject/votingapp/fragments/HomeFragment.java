package com.finalYearProject.votingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalYearProject.votingapp.CandidatesActivity;
import com.finalYearProject.votingapp.adapters.HomeCategoriesAdapter;
import com.finalYearProject.votingapp.manager.PreviousElectionManager;
import com.finalYearProject.votingapp.R;
import com.finalYearProject.votingapp.activities.PreviousElectionViewAllActivity;
import com.finalYearProject.votingapp.adapters.PreviousElectionAdapter;
import com.finalYearProject.votingapp.model.Candidate;
import com.finalYearProject.votingapp.model.OngoingElection;
import com.finalYearProject.votingapp.model.PreviousElection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements HomeCategoriesAdapter.OnCategoryClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String TAG = "HomeFragment";
    private FirebaseFirestore db;
    private RecyclerView recyclerView, ogRecyclerView;
    private PreviousElectionAdapter adapter;
    private List<PreviousElection> previousElectionList;
    private List<OngoingElection> ongoingElectionList;
    private PreviousElectionManager previousElectionManager;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        previousElectionManager = new PreviousElectionManager();
        // Populate the Firestore with initial data
        //previousElectionManager.populatePreviousElectionDetails();
        // Fetch previous elections from Firestore
        fetchPreviousElections();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //onGoing election category display
        ogRecyclerView = view.findViewById(R.id.onGoingRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        ogRecyclerView.setLayoutManager(layoutManager);

        List<String> categories = Arrays.asList("President", "Labour", "Financial Secretary", "Director of Sports");
        HomeCategoriesAdapter adapter2 = new HomeCategoriesAdapter(categories, this);
        ogRecyclerView.setAdapter(adapter2);


        recyclerView = view.findViewById(R.id.previousElectionRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        previousElectionList = new ArrayList<>();

        adapter = new PreviousElectionAdapter(previousElectionList, view1 -> {
            Intent intent = new Intent(getActivity(), PreviousElectionViewAllActivity.class);
            startActivity(intent);
        }, true);
        recyclerView.setAdapter(adapter);
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

    public void fetchCandidatesByPost(String post) {
        db.collection("Candidate")
                .whereEqualTo("post", post)
                .orderBy("votes", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Candidate> candidateList = new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Candidate candidate = documentSnapshot.toObject(Candidate.class);
                                candidateList.add(candidate);
                            }
                            //pass this candidateList to your RecyclerView adapter
                        } else {
                            // handle the error here
                        }
                    }
                });

    }

//    public void fetchCandidateByCategory(){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        db.collection("Candidate")
//                .whereEqualTo("category", selectedCategory)
//                .orderBy("votes", Query.Direction.DESCENDING)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            List<Candidate> candidates = new ArrayList<>();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Candidate candidate = document.toObject(Candidate.class);
//                                candidates.add(candidate);
//                            }
//                            // Pass the candidates list to the adapter
//                            candidatesAdapter.setCandidates(candidates);
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//
//    }

    @Override
    public void onCategoryClick(String category) {
        Intent intent = new Intent(getActivity(), CandidatesActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

}