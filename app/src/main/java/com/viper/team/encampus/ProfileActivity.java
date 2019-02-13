package com.viper.team.encampus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProfileActivity extends Activity implements View.OnClickListener {
    EditText e, n, u, c, cl, y;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        findViewById(R.id.button40).setOnClickListener(this);
        final DatabaseReference userName = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User user=dataSnapshot.getValue(User.class);
                e = findViewById(R.id.editText30);
                e.setText(user.getEmail());
                u = findViewById(R.id.editText31);
                u.setText(user.getUsername());
                n = findViewById(R.id.editText32);
                n.setText(user.getFb());
                c = findViewById(R.id.editText33);
                c.setText(user.getCell());
                cl = findViewById(R.id.editText34);
                cl.setText(user.getClas());
                y = findViewById(R.id.editText35);
                y.setText(user.getYear());
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();

                StorageReference ImagesRef = storageRef.child("userPic/" + FirebaseAuth.getInstance().getCurrentUser().getUid()+ ".jpg");




                File localFile = null;
                try {
                    localFile = File.createTempFile(FirebaseAuth.getInstance().getCurrentUser().getUid(), "jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }


                final File finalLocalFile = localFile;
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
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
                            ImageView pic = findViewById(R.id.imageView8);
                            pic.setImageBitmap(image);

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
                });}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        e = findViewById(R.id.editText30);
        u = findViewById(R.id.editText31);
        n = findViewById(R.id.editText32);
        c = findViewById(R.id.editText33);
        cl = findViewById(R.id.editText34);
        y = findViewById(R.id.editText35);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button40) {
            if(image==null)
            {
                Toast.makeText(this, "Please change your profile picture", Toast.LENGTH_SHORT).show();
                return;
            }

            final String email = e.getText().toString().trim();
            final String cell = c.getText().toString().trim();
            final String fb = n.getText().toString().trim();
            final String userN = u.getText().toString().trim();
            final String clas = cl.getText().toString().trim();
            final String year = y.getText().toString().trim();
            final DatabaseReference userName = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            userName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (!email.isEmpty()) {
                        user.setEmail(email);
                    }
                    if (!cell.isEmpty()) {
                        user.setCell(cell);
                    }
                    if (!fb.isEmpty()) {
                        user.setFb(fb);
                    }
                    if (!userN.isEmpty()) {
                        user.setUsername(userN);
                    }
                    if (!clas.isEmpty()) {
                        user.setEmail(email);
                    }
                    if(!year.isEmpty())
                    {
                        user.setYear(year);
                    }
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                            }
                            else Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();

                    StorageReference ImagesRef = storageRef.child("userPic/" + FirebaseAuth.getInstance().getCurrentUser().getUid()+ ".jpg");


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

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
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
            ImageView pic = findViewById(R.id.imageView8);
            pic.setImageBitmap(image);
        }
    }
}
