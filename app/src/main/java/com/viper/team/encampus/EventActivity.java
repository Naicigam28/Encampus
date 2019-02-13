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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class EventActivity extends Activity implements View.OnClickListener {

    EditText name, date, start, end, notes;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event);
        name = findViewById(R.id.editText25);
        date = findViewById(R.id.editText40);
        date.setText(getIntent().getExtras().getString("date"));
        start = findViewById(R.id.editText27);
        end = findViewById(R.id.editText28);
        notes = findViewById(R.id.editText29);
        save=findViewById(R.id.button24);
        save.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {



        String name = this.name.getText().toString().trim(), date = this.date.getText().toString().trim(), start = this.start.getText().toString().trim(), end = this.end.getText().toString().trim(), notes = this.notes.getText().toString().trim();
        if (name.isEmpty())
        {
            this.name.setError("Cannot be blank");
            return;
        }
        if (date.isEmpty())
        {
            this.date.setError("Cannot be blank");
            return;
        }
        if (date.isEmpty())
        {
            this.start.setError("Cannot be blank");
            return;
        }
        if (start.isEmpty())
        {
            this.start.setError("Cannot be blank");
            return;
        }
        if (end.isEmpty())
        {
            this.end.setError("Cannot be blank");
            return;
        }
        //EventData data= new EventData(start,end,notes,name);
        SimpleDateFormat formater=new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        final Event event = new Event(start,end,notes,name);
        event.setColor(1);
       // event.setData(data);
        try {
            event.setTimeInMillis(formater.parse(date).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final DatabaseReference userName= FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userName.addValueEventListener(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            User user=dataSnapshot.getValue(User.class);
                                               FirebaseDatabase.getInstance().getReference().child("Messages").child(user.getStudentID().toLowerCase()).child("Event").child(event.name+" "+event.getTimeInMillis()).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<Void> task) {
                                                       Toast.makeText(getApplicationContext(), "Event saved", Toast.LENGTH_SHORT);
                                                   }

                                           });}

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError databaseError) {
                                               Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT);

                                           }
                                       });
        finish();

    }
}
