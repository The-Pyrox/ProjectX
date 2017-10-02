package com.pyrox.projectx;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    FloatingActionButton fab ;

    private ListView listView;

    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> post = new ArrayList<>();
    private ArrayList<String> postid = new ArrayList<>();
    private String role,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        listView = (ListView) findViewById( R.id.mainListView );

        listAdapter = new ArrayAdapter<String>(this, R.layout.row_layout, post);
        listView.setAdapter(listAdapter);
        date = new SimpleDateFormat("yyyyMMdd").format(new Date());
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

        setid();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(),DisplayActivity.class);
                intent.putExtra("postname",postid.get(i));
                startActivity(intent);

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
                post.clear();
                for (DataSnapshot childsnap : dataSnapshot.getChildren()){
                    try {
                        if (childsnap.child("target").getValue().toString().contains(role)) {
                            if (Integer.valueOf(date)>=Integer.valueOf(childsnap.child("from").getValue().toString()) && Integer.valueOf(date)<Integer.valueOf(childsnap.child("to").getValue().toString())) {
                                post.add(childsnap.child("title").getValue().toString());
                                postid.add(childsnap.getKey());
                            }
                        }
                    }catch (NullPointerException e){
                    }
                }
                Collections.reverse(post);
                listAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void setid(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("Posts");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childsnap : dataSnapshot.getChildren()){
                    childsnap.getRef().child("seen").child(user.getUid()).setValue("seen");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
