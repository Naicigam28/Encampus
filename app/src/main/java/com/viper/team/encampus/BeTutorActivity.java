package com.viper.team.encampus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BeTutorActivity extends Activity implements View.OnClickListener {
    Bitmap image;
    EditText sub, rate;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.become_tutor);
        sub = findViewById(R.id.editText36);

        rate = findViewById(R.id.editText10);

        findViewById(R.id.BTmenu).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
        findViewById(R.id.button12).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.BTmenu:
                showMenu(findViewById(R.id.BTmenu));
                break;
            case R.id.button9:
                Intent intent = new Intent();
                intent.setType("image/");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                break;
            case R.id.button12:
                registerTutor();
                break;
        }
    }

    void registerTutor() {
        String subject = sub.getText().toString().trim();
        if (subject.isEmpty()) {
            sub.setError("Cannot leave subject blank");
            return;
        }
        String rate = this.rate.getText().toString().trim();
        if (rate.isEmpty()) {
            this.rate.setError("Cannot leave subject blank");
            return;
        }
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //Strings for use in inner class
        final String r = rate;
        final String s = subject;
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User user = dataSnapshot.getValue(User.class);
                final Tutor tutor = new Tutor(user,r,s);
                FirebaseDatabase.getInstance().getReference("Tutors").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(tutor).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Congrats you are a tutor", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference ImagesRef = storageRef.child("tutorGrades/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = ImagesRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Picture uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message


                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView pic = findViewById(R.id.imageView3);
            pic.setImageBitmap(image);
        }
    }

    public void showMenu(View view) {

        PopupMenu pop = new PopupMenu(getApplicationContext(), view);
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
                        final AlertDialog logout = new AlertDialog.Builder(getApplicationContext())
                                .setTitle("Log out?")
                                .setMessage("Are you sure?")

                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        FirebaseAuth.getInstance().signOut();

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
}
