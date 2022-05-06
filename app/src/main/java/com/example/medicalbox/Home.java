package com.example.medicalbox;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.medicalbox.Common.currentUserFb;


public class Home extends AppCompatActivity {

    Button profile,signout,exit,manageBox;//medicine

//    FirebaseDatabase database;
//    DatabaseReference boxUser;
//    NotificationCompat.Builder notificationBuilder;
//    Intent i;
//    PendingIntent contentIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        currentUserFb=FirebaseAuth.getInstance().getCurrentUser();

        stopService(new Intent(Home.this,FirebaseBackgroundService.class));
        startService(new Intent(Home.this,FirebaseBackgroundService.class));
//        database = FirebaseDatabase.getInstance();
//        boxUser = database.getReference("Boxes/"+currentUserFb.getDisplayName());

        profile=(Button)findViewById(R.id.profile);
        manageBox=(Button)findViewById(R.id.manageBox);
        signout=(Button)findViewById(R.id.signOut);
        exit=(Button)findViewById(R.id.exit);

//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        i=new Intent(Home.this,ManageBox.class);
//        contentIntent=PendingIntent.getActivity(getBaseContext(),0,i,0);
//
//        notificationBuilder= new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ico2)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ico12))
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(contentIntent);
//
//        //boxUser.addListenerForSingleValueEvent(new ValueEventListener() {
//        boxUser.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Box box = snapshot.getValue(Box.class);
//                    if(box.getAvailable()<6)
//                    {
//                        notificationBuilder.setContentTitle(String.valueOf("Box "+box.getBoxNumber()))
//                                .setContentText("Only "+box.getAvailable()+" pills of "+box.getIllness()+" left.");
//                        Notification n=notificationBuilder.build();
//                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                        notificationManager.notify(box.getBoxNumber(), n);
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {}
//        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i=new Intent(Home.this,Profile.class);
                startActivity(i);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(Home.this,MainActivity.class));
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder b;
                b = new AlertDialog.Builder(Home.this);
                b.setMessage("Are you sure you want to exit?")
                        .setPositiveButton("exit", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface di, int i)
                            {
                                finish();
                            }
                        })
                        .setNegativeButton("cancel",null);
                AlertDialog ad=b.create();
                ad.show();
            }
        });

        manageBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Home.this,ManageBox.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder b;
        b = new AlertDialog.Builder(Home.this);
        b.setMessage("Are you sure you want to exit?")
                .setPositiveButton("exit", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface di, int i)
                    {
                        finish();
                    }
                })
                .setNegativeButton("cancel",null);
        AlertDialog ad=b.create();
        ad.show();
    }
}
