package com.finalYearProject.votingapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.finalYearProject.votingapp.R;
import com.finalYearProject.votingapp.activities.LoginActivity;
import com.finalYearProject.votingapp.activities.ResultActivity;
import com.finalYearProject.votingapp.model.Request;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String uid;
    private FirebaseFirestore firebaseFirestore;
    public static final String PREFERENCES = "prefkey";
    SharedPreferences sharedPreferences;
    public static final String IsLogIn = "islogin";
    ImageView logOutBtn;
    RelativeLayout buttonApply;
    private FirebaseAuth auth = FirebaseAuth.getInstance();;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //apply button
        buttonApply = view.findViewById(R.id.cardButtonApply);

        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        sharedPreferences = getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);


        logOutBtn = view.findViewById(R.id.logOutBtn);
        onClicklisteners();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        SharedPreferences.Editor pref = sharedPreferences.edit();
        if (itemId == R.id.show_results) {
            startActivity(new Intent(getActivity(), ResultActivity.class));
            return true;
        } else if (itemId == R.id.log_out) {
            FirebaseAuth.getInstance().signOut();
            pref.putBoolean(IsLogIn, false);
            pref.commit();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            //finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void onClicklisteners() {
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences.Editor pref = sharedPreferences.edit();
                pref.putBoolean(IsLogIn, true);
                pref.apply();
                pref.putBoolean(IsLogIn, false);
                pref.commit();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                //finish();
            }
        });

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyForCategory();
            }
        });
    }

    private void applyForCategory() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String userName = user.getDisplayName(); // Or fetch from Firestore if stored separately
            String category = "President"; // Example, you may allow users to choose

            Request request = new Request(userId, userName, category, false);

            firebaseFirestore.collection("Requests").add(request)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getActivity(), "Request sent successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Error sending request", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}