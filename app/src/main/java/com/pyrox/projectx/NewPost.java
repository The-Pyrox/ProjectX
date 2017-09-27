package com.pyrox.projectx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewPost extends AppCompatActivity {

    private Button btn_send;
    private EditText edit_string;


    private String target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);


        edit_string = (EditText) findViewById(R.id.edit_string);
        btn_send = (Button) findViewById(R.id.btn_submit);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (target !=null){
                    createNotice();

                }else{
                    Toast.makeText(getApplicationContext(),"Please select atleast one",Toast.LENGTH_LONG).show();
                }



            }
        });

        target = null;


    }


    public void createNotice(){
        String key = FirebaseDatabase.getInstance().getReference().push().getKey();
        FirebaseDatabase.getInstance().getReference().child("Posts").child(key).child("title").setValue(edit_string.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("Posts").child(key).child("target").setValue(target);

        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        if (target!=null){
            Log.d("Target:",target );

        }


        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_hod:
                if (checked){
                    target = target + "hod";
                }

            else{
                   target = target.replace("hod","");

                }
                break;
            case R.id.checkbox_dean:
                if (checked){
                    target = target + "dean";

                }

            else{
                    target =target.replace("dean","");

                }
                break;
            case R.id.checkbox_tsm:
                if (checked){
                    target = target + "tsm";

                }

                else{
                    target = target.replace("tsm","");

                }
                break;
            case R.id.checkbox_ntsm:
                if (checked){
                    target = target + "ntsm";

                }

                else{
                    target = target.replace("ntsm","");

                }
                break;
            case R.id.checkbox_student:
                if (checked){
                    target = target + "student";

                }

                else{
                   target =  target.replace("student","");

                }
                break;

        }
    }

}
