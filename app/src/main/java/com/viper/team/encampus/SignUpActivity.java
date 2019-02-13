package com.viper.team.encampus;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignUpActivity extends Activity implements View.OnClickListener {

    EditText username, pass, firstName, lastName, studID, email;
    Spinner course, campus;
    private FirebaseAuth mAuth;
    Button signup;
    ProgressBar bar;
    String[] courses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.sign_up);
        username = (EditText) findViewById(R.id.editText4);
        pass = (EditText) findViewById(R.id.editText5);
        firstName = (EditText) findViewById(R.id.editText6);
        lastName = (EditText) findViewById(R.id.editText7);
        studID = (EditText) findViewById(R.id.editText8);
        email = (EditText) findViewById(R.id.editText9);
        course = (Spinner) findViewById(R.id.courseList);
        campus = (Spinner) findViewById(R.id.campus_spinner);
        signup = (Button) findViewById(R.id.signUp);
        bar=(ProgressBar) findViewById(R.id.progressBar);
        signup.setOnClickListener(this);



    }
    long getbookCount() {
        final long[] count = new long[1];
        FirebaseDatabase.getInstance().getReference("Books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count[0] = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return count[0];
    }

    private void registerUser() {

        final String usernameS = username.getText().toString();
        final String passS = pass.getText().toString();
        final String firstNameS = firstName.getText().toString();
        final String lastNameS = lastName.getText().toString();
        final String studIDS = studID.getText().toString();
        final String emailS = email.getText().toString();
        final String courseS = course.getSelectedItem().toString();
        final String campusS = campus.getSelectedItem().toString();

        if (emailS.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        } else if (passS.isEmpty()) {
            email.setError("Password is required");
            email.requestFocus();
            return;
        } else if (usernameS.isEmpty()) {
            username.setError("Username is required");
            username.requestFocus();
            return;
        } else if (firstNameS.isEmpty()) {
            firstName.setError("First is required");
            firstName.requestFocus();
            return;
        } else {
            bar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(emailS, passS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        User user = new User(usernameS, firstNameS, lastNameS, studIDS.toLowerCase(), emailS, courseS, campusS);
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Details entered into system", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        bar.setVisibility(View.GONE);
                        finish();
                        //startActivity(new Intent(SignUpActivity.this, ProfileActivity.class));
                    } else {

                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });


        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUp:
                registerUser();
                break;


        }


    }
}
