package com.viper.team.encampus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class ReminderActivity extends Activity implements View.OnClickListener {


    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.remind);
        EditText date = findViewById(R.id.editText26);
        date.setText(getIntent().getExtras().getString("date"));
        findViewById(R.id.button24).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        EditText name = findViewById(R.id.editText25);
        EditText date = findViewById(R.id.editText26);
        EditText time = findViewById(R.id.editText27);
        EditText data = findViewById(R.id.editText39);


        final String sName = name.getText().toString();
        final String sDate = date.getText().toString();
        final String sTime = time.getText().toString();
        final String sData = data.getText().toString();

        if (sName.isEmpty()) {
            name.setError("Cannot be blank");
            return;
        }
        if (sDate.isEmpty()) {
            date.setError("Cannot be blank");
            return;
        }
        if (sTime.isEmpty()) {
            time.setError("Cannot be blank");
            return;
        }
        if (sData.isEmpty()) {
            data.setError("Cannot be blank");
            return;
        }

        final DatabaseReference userName = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Reminder reminder = new Reminder(sDate, sData, user.getStudentID(), sTime, sName);
                FirebaseDatabase.getInstance().getReference().child("Messages").child(user.getStudentID().toLowerCase()).child("reminders").child(reminder.created + "-" + reminder.name).setValue(reminder).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(getApplicationContext(), "Reminder saved", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT);
            }
        });


    }


}


