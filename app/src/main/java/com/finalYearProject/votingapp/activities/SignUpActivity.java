package com.finalYearProject.votingapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.finalYearProject.votingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    private CircleImageView userProfile;
    private EditText userName, userPassword, userEmail, userNationalId;
    private Button signUpBtn;
    private Uri mainUri = null;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    public static final String PREFERENCES = "prefkey";
    public static final String Name = "namekey";
    public static final String Email = "emailkey";
    public static final String Password = "passwordkey";
    public static final String NationalId = "nationalIdkey";
    public static final String Image = "imagekey";

    SharedPreferences sharedPreferences;
    String name, email, password, nationalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setContentView(R.layout.activity_sign_up);

        sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);

        findViewById(R.id.have_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        //userProfile=findViewById(R.id.profile_image);
        userName = findViewById(R.id.user_name);
        userPassword = findViewById(R.id.user_password);
        userEmail = findViewById(R.id.user_email);
        userNationalId = findViewById(R.id.studentRegNumber);
        signUpBtn = findViewById(R.id.Signup_btn);

        mAuth = FirebaseAuth.getInstance();

       /* userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(SignUpActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(SignUpActivity.this,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                    else{
                        cropImage();
                        Toast.makeText(SignUpActivity.this,"cropped image required 1",Toast.LENGTH_LONG).show();
                    }
                }

                else{
                    cropImage();
                    Toast.makeText(SignUpActivity.this,"cropped image required 2",Toast.LENGTH_LONG).show();
                }
            }
        });*/

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = userName.getText().toString().trim();
                password = userPassword.getText().toString().trim();
                email = userEmail.getText().toString().trim();
                nationalId = userNationalId.getText().toString().trim();


                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)
                        && !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                        !TextUtils.isEmpty(nationalId)) {
                    createUser(email, password);
                } else {
                    Toast.makeText(SignUpActivity.this, "Please enter your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "successfull created", Toast.LENGTH_LONG).show();
                    verifyEmail();

                } else {
                    Toast.makeText(SignUpActivity.this, "Fail try again", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "some thing went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void verifyEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        SharedPreferences.Editor pref = sharedPreferences.edit();
                        pref.putString(Name, name);
                        pref.putString(Password, password);
                        pref.putString(Email, email);
                        pref.putString(NationalId, nationalId);
                        //pref.putString(Image,mainUri.toString());
                        pref.commit();

                        //email sent
                        Toast.makeText(SignUpActivity.this, "email sent", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    } else {
                        mAuth.signOut();
                    }
                    finish();

                }
            });
        }

    }

    private void cropImage() {

    }
}