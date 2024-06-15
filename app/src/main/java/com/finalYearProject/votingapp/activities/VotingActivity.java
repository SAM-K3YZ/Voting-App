package com.finalYearProject.votingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.finalYearProject.votingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class VotingActivity extends AppCompatActivity {

    private CircleImageView image;
    private TextView name, position, department, level, manifesto, gender;
    private Button voteBtn;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setContentView(R.layout.activity_voting);

        firebaseFirestore = FirebaseFirestore.getInstance();

        //image= itemView.findViewById(R.id.image);
        name = findViewById(R.id.votingName);
        position = findViewById(R.id.votingPost);
        department = findViewById(R.id.votingDepartment);
        gender = findViewById(R.id.votingGender);
        level = findViewById(R.id.votingLevel);
        manifesto = findViewById(R.id.votingManifesto);

        voteBtn = findViewById(R.id.vote_btn);

        //String url=getIntent().getStringExtra("image");
        String nm = getIntent().getStringExtra("name");
        String pos = getIntent().getStringExtra("post");
        String dept = getIntent().getStringExtra("department");
        String id = getIntent().getStringExtra("id");
        String mani = getIntent().getStringExtra("manifesto");
        String lev = getIntent().getStringExtra("level");
        String gen = getIntent().getStringExtra("gender");
        //Glide.with(this).load(url).into(image);
        name.setText(nm);
        position.setText(pos);
        department.setText(dept);
        manifesto.setText(mani);
        level.setText(lev);
        gender.setText(gen);

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String finish = "voted";
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("finish", finish);
                userMap.put("deviceIp", getDeviceIp());
                userMap.put(pos, id);

                firebaseFirestore.collection("Users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(userMap);

                Map<String, Object> candidateMap = new HashMap<>();
                candidateMap.put("deviceIp", getDeviceIp());
                candidateMap.put("candidatePost", pos);
                candidateMap.put("timestamp", FieldValue.serverTimestamp());

                firebaseFirestore.collection("Candidate/" + id + "/Vote")
                        .document(uid)
                        .set(candidateMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(VotingActivity.this, ResultActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(VotingActivity.this, "Voted successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    private Object getDeviceIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface inf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = inf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}