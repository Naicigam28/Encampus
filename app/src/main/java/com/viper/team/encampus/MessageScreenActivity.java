package com.viper.team.encampus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessageScreenActivity extends Activity implements View.OnClickListener {


    Button compose;
    private DatabaseReference mDatabase;
    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_screen);
        compose=(Button)findViewById(R.id.button14);
        compose.setOnClickListener(this);
        Spinner sort=findViewById(R.id.spinner3);

        search=findViewById(R.id.searchView);
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                String target=search.getQuery()+"";
                LinearLayout display = (LinearLayout) findViewById(R.id.linearLayout4);
                if(!target.isEmpty()) {


                    for (int i = 0; i < display.getChildCount(); i++) {
                        if (!((TextView)display.getChildAt(i)).getText().toString().contains(target))
                        {
                            display.getChildAt(i).setVisibility(View.GONE);
                        }
                    }
                }
                else{
                    for (int i = 0; i < display.getChildCount(); i++) {

                        display.getChildAt(i).setVisibility(View.VISIBLE);
                    }}

                return false;
            }
        });


        final DatabaseReference userName= FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final LinearLayout display=(LinearLayout) findViewById(R.id.linearLayout4);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                final User user = dataSnapshot.getValue(User.class);
                mDatabase = FirebaseDatabase.getInstance().getReference("Messages").child(user.getStudentID().toLowerCase()).child("inbox");
                ValueEventListener post= new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot data) {
                        // Get Post object and use the values to update the UI

                        final Iterable<DataSnapshot> children = data.getChildren();
                        display.removeAllViews();
                        for (DataSnapshot snap:children)
                        {
                            final Message msg=snap.getValue(Message.class);

                            if(msg!=null){
                            TextView view1 = new TextView(getApplicationContext());
                            if (msg.getData().length()<20){
                            view1.setText(msg.getSender()+":"+msg.getData().trim()+"...");}
                            else view1.setText(msg.getSender()+":"+msg.getData().substring(0,20).trim()+"...");
                            view1.setBackgroundColor(Color.LTGRAY);
                            view1.setTextSize(18.5f);
                            view1.setClickable(true);
                            view1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setContentView(R.layout.message_view);
                                    TextView sender=findViewById(R.id.textView32);
                                    TextView time=findViewById(R.id.textView33);
                                    TextView data=findViewById(R.id.textView62);
                                    Button delete=findViewById(R.id.button38);
                                    Button reply=findViewById(R.id.button38);
                                    reply.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent= new Intent(getApplicationContext(),ComposeMsgActivity.class);
                                            Bundle info=new Bundle();
                                            info.putString("Recv",msg.getSender());
                                            intent.putExtras(info);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    delete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            FirebaseDatabase.getInstance().getReference("Messages").child(user.getStudentID()).child("inbox").child(msg.getTimestamp()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(getApplicationContext(),"Message Deleted",Toast.LENGTH_SHORT);
                                                    }
                                                    else Toast.makeText(getApplicationContext(),"Message not Deleted",Toast.LENGTH_SHORT);
                                                }
                                            });
                                        }
                                    });
                                    sender.setText("From: "+msg.getSender());
                                    time.setText("Date: "+msg.getTimestamp());
                                    //Toast.makeText(getApplicationContext(),msg.getData().substring(0,20).trim(),Toast.LENGTH_LONG);
                                    data.setText(msg.getData());
                                    msg.setRead(true);
                                    Log.i("Bug!",msg.getTimestamp());
                                    FirebaseDatabase.getInstance().getReference("Messages").child(user.getStudentID()).child("inbox").child(msg.getTimestamp()).setValue(msg);
                                }
                            });
                            display.addView(view1);}
                        }

                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(getApplicationContext(), databaseError.getMessage(),Toast.LENGTH_SHORT);

                    }
                };
                mDatabase.addValueEventListener(post);



                // ...
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(getApplicationContext(), databaseError.getMessage(),Toast.LENGTH_SHORT);
                // ...
            }
        };
        userName.addValueEventListener(postListener);


    }

    @Override
    public void onClick(View view) {

switch (view.getId())
{
    case R.id.button14:
        startActivity(new Intent(this,ComposeMsgActivity.class));
        break;
}

    }
}
