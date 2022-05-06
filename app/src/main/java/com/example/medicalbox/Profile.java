package com.example.medicalbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.medicalbox.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import static com.example.medicalbox.Common.currentUser;

public class Profile extends AppCompatActivity
{
    TextView uname,email,age,number,gender;
    Button signout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        uname=(TextView)findViewById(R.id.uname);
        email=(TextView)findViewById(R.id.email);
        age=(TextView)findViewById(R.id.age);
        number=(TextView)findViewById(R.id.number);
        gender=(TextView)findViewById(R.id.gender);

        signout=(Button)findViewById(R.id.logout);

        if(currentUser==null)
        {
            SharedPreferences res=getSharedPreferences("savedata", Context.MODE_PRIVATE);
            String op=res.getString("xxx","");
            Gson gson=new Gson();
            currentUser=gson.fromJson(op, User.class);
        }

        uname.append(currentUser.getName());
        email.append(currentUser.getEmail());
        age.append(currentUser.getAge());
        number.append(currentUser.getNumber());
        gender.append(currentUser.getGender());

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                finishAffinity();
                startActivity(new Intent(Profile.this,MainActivity.class));
            }
        });

    }
}
