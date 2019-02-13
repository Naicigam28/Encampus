package com.viper.team.encampus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainMenuActivity extends Activity implements View.OnClickListener{
    private DatabaseReference mDatabase;
    Button menu, market;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_menu);
        final TextView wel = findViewById(R.id.welMsg);
        menu = (Button) findViewById(R.id.popUpMenu);
        menu.setOnClickListener(this);
        market = (Button) findViewById(R.id.button3);
        market.setOnClickListener(this);

        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User user = dataSnapshot.getValue(User.class);
                wel.setText("Welcome, " + user.getFirstName());
                findViewById(R.id.progressBar3).setVisibility(View.GONE);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    public void showMenu(View view) {

        PopupMenu pop = new PopupMenu(MainMenuActivity.this, view);
        pop.getMenuInflater().inflate(R.menu.menu, pop.getMenu());
        pop.show();
        final Intent intent = new Intent(this, ProfileActivity.class);
        final Intent intentHelp = new Intent(this, HelpActivity.class);
        final Intent intentReport = new Intent(this, ReportIssue.class);
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profileButton:
                        startActivity(intent);
                        break;
                    case R.id.helpMenu:
                        startActivity(intentHelp);
                        break;
                    case R.id.report:
                        startActivity(intentReport);
                        break;
                    case R.id.logout:
                        AlertDialog logout = new AlertDialog.Builder(MainMenuActivity.this)
                                .setTitle("Log out?")
                                .setMessage("Are you sure?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //FirebaseAuth.getInstance().signOut();
                                        setContentView(R.layout.activity_main);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                }).show();
                }
                return false;
            }
        });

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.popUpMenu:
                showMenu((View) findViewById(R.id.popUpMenu));
                break;
            case R.id.button3:
                startActivity(new Intent(this, MarketActivity.class));
                break;
            case R.id.button4:
                startActivity(new Intent(this, TutorScreenActivity.class));
                break;
            case R.id.button5:
                startActivity(new Intent(this,CalenderActivity.class));
                break;
            case R.id.button6:
                startActivity(new Intent(this, MessageScreenActivity.class));
                break;



        }
    }
}
