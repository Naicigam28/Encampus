package com.viper.team.encampus;

import android.app.ActionBar;
import android.app.Activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CalenderActivity extends Activity implements View.OnClickListener {

    CompactCalendarView cal;
    final Bundle bun=new Bundle();
    final Date select=new Date();
    final ArrayList<String>eve=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender);
        cal= (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        cal.setCurrentDate(new Date());
        findViewById(R.id.button39).setOnClickListener(this);
        ActionBar actionbar=getActionBar();
        actionbar.setTitle(new SimpleDateFormat("MMMM YYYY").format(new Date()));

        cal.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked)
            {
                Toast.makeText(getApplicationContext(),dateClicked.toString(), Toast.LENGTH_SHORT).show();
                select.setTime(dateClicked.getTime());
                bun.putString("date",new SimpleDateFormat("EEE, d MMM yyyy, HH:mm").format(dateClicked));

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                ActionBar actionBar=getActionBar();
                actionBar.setTitle(new SimpleDateFormat("EEE, d MMM yyyy, HH:mm").format(firstDayOfNewMonth));
            }
        });
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        findViewById(R.id.button25).setOnClickListener(this);
        findViewById(R.id.button26).setOnClickListener(this);
        final DatabaseReference userName= FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                Log.i("TEST",user.getFirstName());

                FirebaseDatabase.getInstance().getReference().child("Messages").child(user.getStudentID().toLowerCase()).child("Event").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot data:dataSnapshot.getChildren())
                        {
                            Event event= data.getValue(Event.class);
                            eve.add(event.toString());
                            cal.addEvent(event,true);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button26:
                Intent intent=new Intent(this,EventActivity.class);
                intent.putExtras(bun);
                startActivity(intent);
                break;
            case R.id.button25:
                Intent intent2=new Intent(this,ReminderActivity.class);
                intent2.putExtras(bun);
                startActivity(intent2);
                break;
            case R.id.button39:
                Intent intent1=new Intent(this,ViewEventsActivity.class);
                ArrayList<String>listEvs=new ArrayList<>();
                Bundle events= new Bundle();

                for (int i=0;i<eve.size();i++)
                {
                    if(eve.get(i).contains(select.getTime()+""))
                    {
                        listEvs.add(eve.get(i).replace(select.getTime()+"",""));
                    }
                }
                events.putStringArrayList("Events",listEvs);
                intent1.putExtras(events);
                if(listEvs.isEmpty())Toast.makeText(getApplicationContext(),"No events",Toast.LENGTH_SHORT).show();
                else startActivity(intent1);
                break;
        }

    }
}
