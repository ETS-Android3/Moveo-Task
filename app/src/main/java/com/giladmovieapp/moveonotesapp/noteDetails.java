package com.giladmovieapp.moveonotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class noteDetails extends AppCompatActivity  {

    Intent data;

    private EditText mdateDetails;
    private EditText mtitleDetails;
    private EditText mbodyDetails;
    FloatingActionButton mgoToEditNote;
    FloatingActionButton mgoToDeleteNote;
    FloatingActionButton backButton;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        mdateDetails = findViewById(R.id.dateDetails);
        mtitleDetails = findViewById(R.id.titleDetails);
        mbodyDetails = findViewById(R.id.bodyDetails);
        mgoToEditNote = findViewById(R.id.goToEditNote);
        mgoToDeleteNote = findViewById(R.id.goToDeleteNote);
        backButton = findViewById(R.id.backToMainScreen);

        data = getIntent();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(noteDetails.this , MainScreenActivity.class));
            }
        });

        mgoToDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(view.getContext(), "note is Deleted" , Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(noteDetails.this , MainScreenActivity.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), "Failed To Delete" , Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });

        mgoToEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext() , "savebutton clicl " , Toast.LENGTH_SHORT).show();

                String newDate = mdateDetails.getText().toString();
                String newTitle = mtitleDetails.getText().toString();
                String newBody = mbodyDetails.getText().toString();

                if(newDate.isEmpty() || newTitle.isEmpty() || newBody.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Some field is empty" , Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                    Map<String , Object> note = new HashMap<>();
                    note.put("date" , newDate);
                    note.put("title" , newTitle);
                    note.put("body" , newBody);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "note is updated" , Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(noteDetails.this , MainScreenActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed To updated" , Toast.LENGTH_SHORT).show();
                        }
                    });

                }


            }
        });

        String noteDate = data.getStringExtra("date");
        String noteTitle = data.getStringExtra("title");
        String noteBody = data.getStringExtra("body");


        mdateDetails.setText(noteDate);
        mtitleDetails.setText(noteTitle);
        mbodyDetails.setText(noteBody);





        mdateDetails.setText(data.getStringExtra("date"));
        mtitleDetails.setText(data.getStringExtra("title"));
        mbodyDetails.setText(data.getStringExtra("body"));


    }
}