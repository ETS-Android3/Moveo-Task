package com.giladmovieapp.moveonotesapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateNotes extends AppCompatActivity {



List<DocumentSnapshot> snapshots = new ArrayList<>();

    EditText createTitleNote, createBodyNote, createDateNote;
    FloatingActionButton msaveNote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FloatingActionButton backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());


        msaveNote = findViewById(R.id.saveNote);
        createDateNote = findViewById(R.id.addDate);
        createTitleNote = findViewById(R.id.addTitle);
        createBodyNote = findViewById(R.id.addBody);
        createDateNote.setText(currentDate);
        backButton = findViewById(R.id.backToMainScreen);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//////////


//////////
        LatLng location =  new LatLng(32.104886*Math.random(), 34.889999*Math.random());




        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateNotes.this , MainScreenActivity.class));
            }
        });

         msaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String date = createDateNote.getText().toString();
            String title = createTitleNote.getText().toString();
            String body = createBodyNote.getText().toString();

            if(title.isEmpty() || body.isEmpty() || date.isEmpty() ){

                Toast.makeText(getApplicationContext() , "All field are Require!" , Toast.LENGTH_SHORT).show();

            }
            else{

                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();

                Map<String , Object> note = new HashMap<>();
                note.put("date" , date);
                note.put("title" , title);
                note.put("body" , body);
                note.put("location" , location);



                documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext() , "Note Created Successfully!" , Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(CreateNotes.this , MainScreenActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext() , "Failed To Create Note!" , Toast.LENGTH_SHORT).show();
                    }
                });

            }

            }
        });




    }
}