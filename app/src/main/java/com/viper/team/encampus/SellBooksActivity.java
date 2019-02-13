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
import android.widget.Button;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SellBooksActivity extends Activity implements View.OnClickListener {
    EditText title, author, edition, year, price;
    Bitmap image;
    Button choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_book);
        title = (EditText) findViewById(R.id.editText15);
        author = (EditText) findViewById(R.id.editText16);
        edition = (EditText) findViewById(R.id.editText17);
        year = (EditText) findViewById(R.id.editText18);
        price = (EditText) findViewById(R.id.editText19);
        choose = (Button) findViewById(R.id.button22);
        choose.setOnClickListener(this);
        findViewById(R.id.menuButton2).setOnClickListener(this);
        findViewById(R.id.button20).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button22:
                Intent intent = new Intent();
                intent.setType("image/");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                break;
            case R.id.button20:

                upload();
                break;
            case R.id.menuButton2:
                showMenu(findViewById(R.id.menuButton2));


        }
    }

    void upload() {

        String title = this.title.getText().toString().trim();
        String author = this.author.getText().toString().trim();
        String edition = this.edition.getText().toString().trim();
        String year = this.year.getText().toString().trim();
        String price = this.price.getText().toString().trim();
        long bookID = getbookCount();

        if (title.isEmpty()) {
            this.title.setError("Cant be Empty");
            return;
        }
        if (author.isEmpty()) {
            this.author.setError("Cant be Empty");
            return;
        }
        if (edition.isEmpty()) {
            this.edition.setError("Cant be Empty");
            return;
        }
        if (price.isEmpty()||Integer.parseInt(price)<0) {
            this.price.setError("Cant be Empty or negative");
            return;
        }
        bookID++;


        Book book = new Book(title, author, edition, year, price, bookID);

        FirebaseDatabase.getInstance().getReference("Books").child(bookID + "").setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Book uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference ImagesRef = storageRef.child("bookCovers/" + bookID + ".jpg");


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView pic = findViewById(R.id.imageView7);
            pic.setImageBitmap(image);
        }
    }

    public void showMenu(View view) {

        PopupMenu pop = new PopupMenu(SellBooksActivity.this, view);
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
                        final AlertDialog logout = new AlertDialog.Builder(SellBooksActivity.this)
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

    long getbookCount() {
        final long[] count = new long[1];
        FirebaseDatabase.getInstance().getReference("Books").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count[0] = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return count[0];
    }
}
