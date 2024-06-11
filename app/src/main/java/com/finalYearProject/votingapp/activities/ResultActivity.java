package com.finalYearProject.votingapp.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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
import com.finalYearProject.votingapp.adapters.CandidateResultAdapter;
import com.finalYearProject.votingapp.model.Candidate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView resultRV;
    private List<Candidate> list;
    private CandidateResultAdapter adapter;
    private FirebaseFirestore firebaseFirestore;
    private TextView warningText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        resultRV=findViewById(R.id.result_rv);
        warningText=findViewById( R.id.warning_text);
        firebaseFirestore =FirebaseFirestore.getInstance();


        list = new ArrayList<>();
        adapter=new CandidateResultAdapter(ResultActivity.this,list);
        resultRV.setLayoutManager(new LinearLayoutManager(ResultActivity.this));
        resultRV.setAdapter(adapter);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            firebaseFirestore.collection("Candidate")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot snapshot:task.getResult()){
                                    list.add(new Candidate(
                                            snapshot.getString("name"),
                                            snapshot.getString("party"),
                                            snapshot.getString("post"),
                                            snapshot.getId()
                                    ));
                                }
                                adapter.notifyDataSetChanged();

                            }else{
                                Toast.makeText(ResultActivity.this, "Candidate not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("Users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String finish=task.getResult().getString("finish");
                        //assert finish!=null;
                        if(finish!=null){
                            if(!finish.equals("voted")){
                                resultRV.setVisibility(View.GONE);
                                warningText.setVisibility(View.VISIBLE);
                            }else {
                                resultRV.setVisibility(View.VISIBLE);
                                warningText.setVisibility(View.GONE);
                            }
                        }else{
                            warningText.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }
}