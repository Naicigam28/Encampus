package com.viper.team.encampus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MarketActivity extends Activity implements View.OnClickListener {
    Button menu, sell;
    private DatabaseReference mDatabase;
    Book[] books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market);
        menu = (Button) findViewById(R.id.menuButton);
        menu.setOnClickListener(this);
        sell = findViewById(R.id.button8);
        sell.setOnClickListener(this);


        FirebaseDatabase.getInstance().getReference("Books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count;
                count = dataSnapshot.getChildrenCount();
                if (count > 0) {
                    books = new Book[(int) (count)];
                    for (int i = 1; i <= count; i++) {
                        mDatabase = FirebaseDatabase.getInstance().getReference("Books").child(i + "");
                        final int index = i - 1;
                        ValueEventListener postListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get Post object and use the values to update the UI
                                books[index] = dataSnapshot.getValue(Book.class);
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageRef = storage.getReference();

                                StorageReference Ref = storageRef.child("bookCovers").child((index + 1) + ".jpg");
                                Toast.makeText(getApplicationContext(), "bookCovers/" + (index + 1) + ".jpg", Toast.LENGTH_SHORT).show();


                                File localFile = null;
                                try {
                                    localFile = File.createTempFile(index + books[index].getTitle(), "jpg");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                final File finalLocalFile = localFile;
                                Ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override

                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        // Local temp file has been created

                                        Toast.makeText(getApplicationContext(), "DONE!", Toast.LENGTH_SHORT).show();
                                        try {
                                            Bitmap image;

                                            BitmapFactory.Options options = new BitmapFactory.Options();
                                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                            image = BitmapFactory.decodeStream(new FileInputStream(finalLocalFile), null, options);
                                            image = Bitmap.createScaledBitmap(image, 500, 500, false);
                                            LinearLayout linearLayout = findViewById(R.id.marketC);
                                            TextView view1 = new TextView(getApplicationContext());
                                            ImageView view2 = new ImageView(getApplicationContext());
                                            view1.setText(books[index].toString());
                                            view1.setBackgroundColor(Color.GRAY);

                                            view1.setTextSize(18.5f);
                                            view2.setImageBitmap(image);
                                            linearLayout.addView(view2);
                                            linearLayout.addView(view1);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                        exception.printStackTrace();
                                    }
                                });


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

                }
                Toast.makeText(getApplicationContext(), "Book count is " + count, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void showMenu(View view) {

        PopupMenu pop = new PopupMenu(MarketActivity.this, view);
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
                        final AlertDialog logout = new AlertDialog.Builder(MarketActivity.this)
                                .setTitle("Log out?")
                                .setMessage("Are you sure?")

                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        FirebaseAuth.getInstance().signOut();
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
            case R.id.button8:
                startActivity(new Intent(this, SellBooksActivity.class));
                break;
            case R.id.menuButton:
                showMenu(findViewById(R.id.menuButton));


        }


    }
}
