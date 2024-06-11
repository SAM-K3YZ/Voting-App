package com.finalYearProject.votingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Create_Candidate_Activity extends AppCompatActivity {

    private CircleImageView candidateImg;
    private EditText candidateName,candidateParty;
    private Spinner candidateSpinner;
    private  String [] candPost={"President","vice-President"};
    private Button submitBtn;
    StorageReference reference;

    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_candidate);

        reference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();

        candidateName=findViewById(R.id.candidate_name);
        candidateParty=findViewById(R.id.candidate_party_name);
        candidateSpinner=findViewById(R.id.candidate_spinner);
        submitBtn=findViewById(R.id.candidate_submit_btn );

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,candPost);
        candidateSpinner.setAdapter(adapter);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=candidateName.getText().toString().trim();
                String party=candidateParty.getText().toString().trim();
                String post=candidateSpinner.getSelectedItem().toString();
                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(party) && !TextUtils.isEmpty(post)){
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    //StorageReference imagePath=reference.child("image_profile").child(uid+".jpg");
                    //imagePath.putFile(Uri.parse(image)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    // @Override
                    //public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    //if(task.isSuccessful()){
                    //imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    //@Override
                    // public void onSuccess(Uri uri) {


                    Map<String,Object> map= new HashMap<>();
                    map.put("name",name);
                    map.put("party",party);
                    map.put("post",post);
                    //map.put("image",uri.toString());
                    map.put("timestamp", FieldValue.serverTimestamp());
                    map.put("uid",uid);

                    firebaseFirestore.collection("Candidate")
                            .add(map)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful()){


                                        startActivity(new Intent(Create_Candidate_Activity.this,HomeActivity.class));
                                        finish();

                                    }else{
                                        Toast.makeText(Create_Candidate_Activity.this,"Data not stored",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                    //});
                    //}else{
                    //Toast.makeText(LoginActivity.this,""+task.getException(),Toast.LENGTH_LONG).show();
                    //}
                    //}
                    //});
                    //}
                }else{
                    Toast.makeText(Create_Candidate_Activity.this, "Enter Details", Toast.LENGTH_SHORT).show();
                }


            }
        });




    }
}