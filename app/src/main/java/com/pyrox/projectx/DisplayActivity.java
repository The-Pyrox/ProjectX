package com.pyrox.projectx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {

    String postid;
    private ArrayList<String> userid = new ArrayList<>();
    private Integer dean = 0,hod = 0,student = 0;
    private String str_dean = "",str_hod = "",str_student = "";
    private TextView txt_hod,txt_dean,txt_student,list_dean,list_student,list_hod;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        postid = getIntent().getExtras().get("postname").toString();

        txt_hod = (TextView) findViewById(R.id.count_hod);

        txt_dean = (TextView) findViewById(R.id.count_dean);

        txt_student = (TextView) findViewById(R.id.count_students);

        list_dean = (TextView) findViewById(R.id.list_dean);
        list_hod = (TextView) findViewById(R.id.list_hod);
        list_student = (TextView) findViewById(R.id.list_students);



        getdata();



    }


    public void getdata(){

        DatabaseReference postidref = FirebaseDatabase.getInstance().getReference().child("Posts").child(postid).child("seen");

        postidref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childsnap : dataSnapshot.getChildren()){
                    userid.add(childsnap.getKey());
                }
                getusers();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getusers(){

        DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("Users");
        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childsnap : dataSnapshot.getChildren()){
                    if (userid.contains(childsnap.getKey())){
                        if (childsnap.child("role").getValue().toString().equals("hod")){
                            hod = hod + 1;
                            str_hod = str_hod + childsnap.child("name").getValue().toString()+",";
                        }else if (childsnap.child("role").getValue().toString().equals("student")){
                            student = student + 1;
                            str_student = str_student + childsnap.child("name").getValue().toString()+",";
                        }else {
                            dean = dean + 1;
                            str_dean = str_dean + childsnap.child("name").getValue().toString()+",";

                        }

                    }
                }

                txt_student.setText(String.valueOf(student));
                txt_hod.setText(String.valueOf(hod));
                txt_dean.setText(String.valueOf(dean));

                list_student.setText(str_student);
                list_dean.setText(str_dean);
                list_hod.setText(str_hod);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
