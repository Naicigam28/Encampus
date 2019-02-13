package com.viper.team.encampus;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ComposeMsgActivity extends Activity implements View.OnClickListener {

    EditText reciever;
    EditText msg;
    Button send;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_msg);
        reciever=findViewById(R.id.editText24);
        Bundle info=getIntent().getExtras();
        if (info!=null)
        {
            reciever.setText(info.getString("recv"));
        }
        send=findViewById(R.id.button23);
        send.setOnClickListener(this);
        msg=findViewById(R.id.editText23);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    public void onClick(View view)
    {
        final String data=msg.getText().toString();
        final String r=reciever.getText().toString().trim();
        if (data.isEmpty())
        {
            msg.setError("Can not Send blank Messages");
            return;
        }
        if (r.isEmpty())
        {
            reciever.setError("Needs Reciever");
            return;
        }
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User user = dataSnapshot.getValue(User.class);
                Message msg=new Message(data,user.getStudentID(),r);
                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                FirebaseDatabase.getInstance().getReference("Messages").child(user.getStudentID()).child("outbox").child(date).setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Message saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                FirebaseDatabase.getInstance().getReference("Messages").child(r.toLowerCase()+ "").child("inbox").child(date).setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(getApplicationContext(), databaseError.getMessage(),Toast.LENGTH_SHORT);
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);

    }
}
