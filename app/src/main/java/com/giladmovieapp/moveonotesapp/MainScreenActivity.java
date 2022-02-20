package com.giladmovieapp.moveonotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MainScreenActivity extends AppCompatActivity {

    FloatingActionButton createNoteFab;
    FloatingActionButton logOutFab;
    FloatingActionButton mapFab;
    FloatingActionButton listFab;
    private FirebaseAuth firebaseAuth;



    RecyclerView mrecyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;


    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


        createNoteFab = findViewById(R.id.createNoteFab);
        logOutFab = findViewById(R.id.logOutFab);
        mapFab = findViewById(R.id.mapFab);
        listFab = findViewById(R.id.listFab);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        mapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainScreenActivity.this, MapsActivity.class));
            }
        });


        logOutFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(MainScreenActivity.this, MainActivity.class));
            }
        });


        createNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainScreenActivity.this, CreateNotes.class));

            }
        });


        listFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainScreenActivity.this, MainScreenActivity.class));
            }
        });






        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("date", Query.Direction.DESCENDING);





        FirestoreRecyclerOptions<firebasemodel> allUserNotes = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query, firebasemodel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allUserNotes) {

            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull firebasemodel firebasemodel) {


                noteViewHolder.noteDate.setText(firebasemodel.getDate());
                noteViewHolder.noteTitle.setText(firebasemodel.getTitle());
                noteViewHolder.noteBody.setText(firebasemodel.getBody());

                String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();


                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//
                        Intent intent = new Intent(view.getContext(), noteDetails.class);
//
                        intent.putExtra("date", firebasemodel.getDate());
                        intent.putExtra("title", firebasemodel.getTitle());
                        intent.putExtra("body", firebasemodel.getBody());
                        intent.putExtra("noteId", docId);
                        view.getContext().startActivity(intent);


                    }
                });


            }


            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
                return new NoteViewHolder(view);
            }
        };

        mrecyclerView = findViewById(R.id.recyclerview);
        mrecyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mrecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerView.setAdapter(noteAdapter);


    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView noteDate;
        private TextView noteTitle;
        private TextView noteBody;
        LinearLayout mnote;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteDate = itemView.findViewById(R.id.noteDate);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteBody = itemView.findViewById(R.id.noteBody);
            mnote = itemView.findViewById(R.id.note);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter != null) {
            noteAdapter.stopListening();
        }
    }

}