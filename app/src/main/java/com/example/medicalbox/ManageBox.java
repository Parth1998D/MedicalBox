package com.example.medicalbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.example.medicalbox.Common.currentUserFb;

public class ManageBox extends AppCompatActivity implements View.OnClickListener {

    Button b1,b2,b3,b4;

    String boxNo;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(currentUserFb==null)
        {
            Intent i=new Intent(ManageBox.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        else
        {
            setContentView(R.layout.activity_manage_box);

            b1=(Button)findViewById(R.id.box1);
            b2=(Button)findViewById(R.id.box2);
            b3=(Button)findViewById(R.id.box3);
            b4=(Button)findViewById(R.id.box4);

            b1.setOnClickListener(this);
            b2.setOnClickListener(this);
            b3.setOnClickListener(this);
            b4.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.box1:
                boxNo="1"; break;
            case R.id.box2:
                boxNo="2"; break;
            case R.id.box3:
                boxNo="3"; break;
            case R.id.box4:
                boxNo="4";
        }
        i=new Intent(ManageBox.this,AddMedicine.class);
        i.putExtra("boxNo",boxNo);
        startActivity(i);
    }
}
