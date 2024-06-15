package com.finalYearProject.votingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.finalYearProject.votingapp.R;
import com.finalYearProject.votingapp.activities.AllCandidateActivity;
import com.finalYearProject.votingapp.activities.Create_Candidate_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
}