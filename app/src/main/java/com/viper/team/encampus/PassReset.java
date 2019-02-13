package com.viper.team.encampus;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PassReset extends Activity implements View.OnClickListener{
    EditText email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass);
        email=findViewById(R.id.editText20);
        findViewById(R.id.button37).setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View view)
    {
        String email=this.email.getText().toString().trim();
        if (email.isEmpty())
        {
            this.email.setError("Please enter your email");
            return;
        }
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(PassReset.this, "Check Your email", Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    Toast.makeText(PassReset.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

    }
}
