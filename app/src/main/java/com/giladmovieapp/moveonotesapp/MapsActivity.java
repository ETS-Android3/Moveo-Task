package com.giladmovieapp.moveonotesapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.giladmovieapp.moveonotesapp.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    int count2 = 0;
    List<DocumentSnapshot> snapshots = new ArrayList<>();

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    FloatingActionButton mapFab;
    FloatingActionButton createNoteFab;
    FloatingActionButton listFab;
    FloatingActionButton logOut;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public synchronized void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    count2 = task.getResult().size();

                } else {
                    Log.d("TAG", "Error", task.getException());
                }

            }
        });


        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mapFab = findViewById(R.id.mapFabFromMap);
        createNoteFab = findViewById(R.id.createNoteFabFormMap);
        listFab = findViewById(R.id.listFabFromMap);
        logOut = findViewById(R.id.logOutFabFromMap);


        mapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, MapsActivity.class));
            }
        });

        createNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, CreateNotes.class));
            }
        });
//
        listFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, MainScreenActivity.class));
            }
        });
//
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(MapsActivity.this, MainActivity.class));
            }
        });


//


//
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

//


        mMap = googleMap;


        for (int i = 1; i <= count2; i++) {
//            LatLng location2 = (LatLng) snapshots.get(i).get("location");
            LatLng location = new LatLng(32.104886 + (Math.random() ), 34.889999 + (Math.random() ));
            mMap.addMarker(new MarkerOptions().position(location).title("Note Number: " + i));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        }

//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}