package com.finalYearProject.votingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.finalYearProject.votingapp.R;
import com.finalYearProject.votingapp.activities.AllCandidateActivity;
import com.finalYearProject.votingapp.activities.Create_Candidate_Activity;
import com.finalYearProject.votingapp.activities.ViewUserRequestPage;
import com.finalYearProject.votingapp.adapters.RequestsAdapter;
import com.finalYearProject.votingapp.model.Candidate;
import com.finalYearProject.votingapp.model.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VoteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CircleImageView circleImg;
    private TextView nameTxt, nationalIdTxt;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerViewRequests;
    private RequestsAdapter requestsAdapter;
    private String uid;
    private Button createBtn, voteBtn, startBtn;

    public VoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VoteFragment newInstance(String param1, String param2) {
        VoteFragment fragment = new VoteFragment();
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

        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        firebaseFirestore.collection("Users").document(uid)
                .get().addOnCompleteListener
                        (new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    String name = task.getResult().getString("name");
                                    String nationalId = task.getResult().getString("nationalId");
                                    //assert name!=null;
                                    if (name != null) {
                                        if (name.equals("admin")) {
                                            createBtn.setVisibility(View.VISIBLE);
                                            startBtn.setVisibility(View.VISIBLE);
                                            voteBtn.setVisibility(View.GONE);
                                        } else {
                                            createBtn.setVisibility(View.GONE);
                                            startBtn.setVisibility(View.GONE);
                                            voteBtn.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "cannot retrieve data", Toast.LENGTH_SHORT).show();
                                    }
                                    nameTxt.setText(name);
                                    nationalIdTxt.setText(nationalId);
                                } else {
                                    Toast.makeText(getActivity(), "user not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vote, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTxt = view.findViewById(R.id.name);
        nationalIdTxt = view.findViewById(R.id.national_id);
        createBtn = view.findViewById(R.id.admin_btn);
        voteBtn = view.findViewById(R.id.give_vote);
        startBtn = view.findViewById(R.id.candidate_create_voting);

        recyclerViewRequests = view.findViewById(R.id.notificationRecycler);
        recyclerViewRequests.setLayoutManager(new LinearLayoutManager(getActivity()));
        //fetching the request(s) sent by candidates(user)
        fetchRequests();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Create_Candidate_Activity.class));

            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AllCandidateActivity.class));
            }
        });
        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AllCandidateActivity.class));
            }
        });
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
                        Toast.makeText(getActivity(), "Request approved", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void declineRequest(Request request) {
        firebaseFirestore.collection("Requests").document(request.getUserId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Request declined", Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(getActivity(), "Candidate added successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), "Error adding candidate", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Log.w("VoteFragment", "Error getting user details.", task.getException());
                        }
                    }
                });
    }

    public void OnRequestCardClick(String request){
        Intent toViewUserReq = new Intent(getActivity(), ViewUserRequestPage.class);
        toViewUserReq.putExtra("userName", request);
        toViewUserReq.putExtra("userId", request);
        toViewUserReq.putExtra("category", request);
        startActivity(toViewUserReq);
    }
}