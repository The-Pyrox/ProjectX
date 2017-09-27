package com.pyrox.projectx;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FloatingActionButton fab ;

    private ListView listView;

    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> post = new ArrayList<>();
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        listView = (ListView) findViewById( R.id.mainListView );

        listAdapter = new ArrayAdapter<String>(this, R.layout.row_layout, post);
        listView.setAdapter(listAdapter);




        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                role = dataSnapshot.child("role").getValue().toString();
                getPosts();
                if (role.equals("dean") || role.equals("hod")){
                    fab.setVisibility(View.VISIBLE);
                    fab.bringToFront();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),NewPost.class);
                startActivity(intent);

            }
        });


    }



    public void getPosts(){

        DatabaseReference postref = FirebaseDatabase.getInstance().getReference().child("Posts");
        postref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childsnap : dataSnapshot.getChildren()){
                    try {
                        if (childsnap.child("target").getValue().toString().contains(role)) {
                            post.add(childsnap.child("title").getValue().toString());


                        }
                    }catch (NullPointerException e){

                    }
                }

                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
