package com.viper.team.encampus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class TutorScreenActivity extends Activity implements View.OnClickListener {

    ArrayList<Tutor>list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_screen);
        findViewById(R.id.button15).setOnClickListener(this);
        findViewById(R.id.button16).setOnClickListener(this);
        //Get datasnapshot at your "users" root node
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Tutors");
        final Intent intent=new Intent(this, TutorViewActivity.class);

        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        for (DataSnapshot dsp:dataSnapshot.getChildren())
                        {
                            final Tutor tutor=dsp.getValue(Tutor.class);

                            TextView view= new TextView(getApplicationContext());
                            view.setText(tutor.toString());
                            view.setBackgroundColor(Color.GRAY);

                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    setContentView(R.layout.tutor_view);
                                    EditText text=findViewById(R.id.editText14);
                                    text.setText(tutor.rate+" Lessons\n"+tutor.getFirstName()+" "+tutor.getLastName()+"\nR"+tutor.subject +"Lessons"+"\n Enrolled in"+tutor.getCourse());
                                    //startActivity(intent);
                                }
                            });
                            LinearLayout display =findViewById(R.id.linearLayout5);
                            display.addView(view);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button15:
                startActivity(new Intent(this, BeTutorActivity.class));
                break;
            case R.id.button16:
                startActivity(new Intent(this, TutorViewActivity.class));
                break;


        }

    }
}
