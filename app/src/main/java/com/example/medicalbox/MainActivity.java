package com.example.medicalbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.medicalbox.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;


import static com.example.medicalbox.Common.currentUserFb;
import static com.example.medicalbox.Common.currentUser;


public class MainActivity extends AppCompatActivity {

    MaterialEditText edtEmail, edtPassword;
    Button btnSignUp, btnSignIn;

    FirebaseDatabase database;
    DatabaseReference users;
    private FirebaseAuth mAuth;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = (MaterialEditText) findViewById(R.id.edtEmail);
        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);

        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,SignUp.class);
                startActivity(i);
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(edtEmail.getText().toString(), edtPassword.getText().toString());
            }
        });

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
    }

    private void signIn(final String email, final String pwd)
    {
        if (email.isEmpty() || pwd.isEmpty())
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
        else
        {

            btnSignIn.setClickable(false);

            mAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                currentUserFb=mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "Welcome " + currentUserFb.getDisplayName(), Toast.LENGTH_SHORT).show();

                                users.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                    {
                                        currentUser=dataSnapshot.child(currentUserFb.getDisplayName()).getValue(User.class);
                                        sp=getSharedPreferences("savedata", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor=sp.edit();
                                        Gson gson=new Gson();
                                        String json=gson.toJson(currentUser);
                                        editor.putString("xxx",json);
                                        editor.apply();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        btnSignIn.setClickable(true);
                                    }
                                });

                                Intent homeActivity = new Intent(MainActivity.this, Home.class);
                                startActivity(homeActivity);
                                finish();
                            }
                            else {
                                btnSignIn.setClickable(true);
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            //btnSignIn.setClickable(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUserFb = mAuth.getCurrentUser();
        if(currentUserFb!=null)
        {
            Toast.makeText(MainActivity.this, "Welcome " + currentUserFb.getDisplayName(), Toast.LENGTH_SHORT).show();
            Intent homeActivity = new Intent(MainActivity.this, Home.class);
            startActivity(homeActivity);
            finish();
        }
    }

}
