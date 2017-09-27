package com.pyrox.projectx;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Spinner faculty,department,signuptype,semester;
    private Button btn_signup;
    private EditText email,password,confirmpassword,firstname,lastname;
    private ImageButton upload_photo;
    private CheckBox checkBox ;
    private ProgressDialog progressDialog;

    final CharSequence[] items = { "Take Photo", "Choose from Library",
            "Cancel" }; // for asking profile picture

    private String userChoosenTask;
    private static final int REQUEST_CAMERA = 1888;
    private static final int SELECT_FILE  = 1;
    private Uri uri;

    private StorageReference mStorageRef ;

    private String TAG;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        faculty = (Spinner) findViewById(R.id.faculty_spinner);
        department = (Spinner) findViewById(R.id.department_spinner);
        signuptype = (Spinner)findViewById(R.id.signuptype_spinner);
        semester = (Spinner) findViewById(R.id.semester_spinner);
        firstname = (EditText) findViewById(R.id.editText_signup_firstname);
        lastname = (EditText) findViewById(R.id.editText_signup_lastname);
        email  = (EditText)findViewById(R.id.editText_signup_email);
        password = (EditText) findViewById(R.id.editText_signup_password);
        confirmpassword = (EditText) findViewById(R.id.editText_signup_confirmpassword);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.faculty_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        faculty.setAdapter(adapter1);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.department_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department.setAdapter(adapter2);


        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.signuptype_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        signuptype.setAdapter(adapter3);


        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.semester_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester.setAdapter(adapter4);




        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (firstname.getText().toString().isEmpty() || lastname.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please fill all the details",Toast.LENGTH_SHORT).show();

                }else {
                    if (checkBox.isChecked()) {

                        if (password.getText().toString().equals(confirmpassword.getText().toString())) {
                            progressDialog = new ProgressDialog(SignupActivity.this);
                            progressDialog.setTitle("Signing up & Uploading Photo");
                            progressDialog.setMessage("Please wait");
                            progressDialog.show();

                            createAccount(email.getText().toString(), password.getText().toString());

                        } else {
                            Toast.makeText(SignupActivity.this, "Passwords don't match", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Accept our Terms and Conditions", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });




    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount( final  String emailid, final String pass) {
        mAuth.createUserWithEmailAndPassword(emailid, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(getApplicationContext(), "Weak Password , Atleast 6 Characters", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(getApplicationContext(), "User Already Exist", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.d("TAG", e.getMessage());
                                Toast.makeText(getApplicationContext(), "Weak Password , Atleast 6 Characters", Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();
                        }
                        else {
                            Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());
                            signIn(emailid,pass);
                        }



                    }
                });

    }

    private void signIn(String email, String password ){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "signInWithEmail", task.getException());

                        }else {
                            Log.d("TAG", "signInWithEmail:onComplete:Signup" + task.isSuccessful());

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            inputDetails();
                            progressDialog.dismiss();




                        }


                    }
                });
    }



    private void inputDetails(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Userdetails userdetails = new Userdetails(email.getText().toString(),password.getText().toString(),faculty.getSelectedItem().toString(),department.getSelectedItem().toString(),signuptype.getSelectedItem().toString(),semester.getSelectedItem().toString(),firstname.getText().toString(),lastname.getText().toString());


        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).setValue(userdetails);

        Toast.makeText(getApplicationContext(),"Signup Successful",Toast.LENGTH_LONG).show();


        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);


    }







}
