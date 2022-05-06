package com.example.medicalbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.medicalbox.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;


public class SignUp extends AppCompatActivity {

    MaterialEditText edtNewUser, edtNewPassword, edtNewEmail, edtNewAge, edtNewNumber;
    RadioGroup rg;
    RadioButton rb;
    Button btnSignUp,btnCancel;

    FirebaseDatabase database;
    DatabaseReference users;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        btnSignUp=(Button)findViewById(R.id.signUp);
        btnCancel=(Button)findViewById(R.id.cancel);


        btnSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                database = FirebaseDatabase.getInstance();
                users = database.getReference("Users");
                mAuth = FirebaseAuth.getInstance();


                edtNewUser = (MaterialEditText)findViewById(R.id.edtNewUserName);
                edtNewEmail = (MaterialEditText)findViewById(R.id.edtNewEmail);
                edtNewPassword = (MaterialEditText)findViewById(R.id.edtNewPassword);
                edtNewAge = (MaterialEditText)findViewById(R.id.edtNewAge);
                edtNewNumber = (MaterialEditText)findViewById(R.id.edtNewNumber);
                rg = (RadioGroup)findViewById(R.id.gender);
                int rbid=rg.getCheckedRadioButtonId();
                rb = (RadioButton)findViewById(rbid);

                final String U = edtNewUser.getText().toString();
                final String E = edtNewEmail.getText().toString();
                final String P = edtNewPassword.getText().toString();
                final String N = edtNewNumber.getText().toString();
                final String A = edtNewAge.getText().toString();
                final String G = rb.getText().toString();

                if (U.isEmpty() || E.isEmpty() || P.isEmpty() || N.isEmpty() || A.isEmpty() || G.isEmpty())
                    Toast.makeText(SignUp.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                else
                {
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {

                            if(dataSnapshot.child(U).exists())
                                Toast.makeText(SignUp.this, "User Name is already occupied", Toast.LENGTH_SHORT).show();
                            else
                            {
                                mAuth.createUserWithEmailAndPassword(E, P)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(U)
                                                            .build();
                                                    mAuth.getCurrentUser().updateProfile(profileUpdates)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>()
                                                            {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task)
                                                                {
                                                                    if (task.isSuccessful())
                                                                    {
                                                                        Toast.makeText(SignUp.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                                                        User user = new User(U, E, G, A, N, P);
                                                                        users.child(U).setValue(user);
                                                                        mAuth.signOut();
                                                                    }
                                                                }
                                                            });
                                                    mAuth.signOut();
                                                    finish();
                                                }
                                                else
                                                    Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
            }

        });
        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                        finish();
                    }
        });
    }
}
