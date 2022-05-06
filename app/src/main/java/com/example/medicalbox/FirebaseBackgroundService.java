package com.example.medicalbox;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.medicalbox.Model.Box;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.medicalbox.Common.currentUserFb;

public class FirebaseBackgroundService extends Service {
    FirebaseDatabase database;
    DatabaseReference boxUser;
    NotificationCompat.Builder notificationBuilder;
    Intent i;
    PendingIntent contentIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        currentUserFb= FirebaseAuth.getInstance().getCurrentUser();
        if(currentUserFb!=null) {

            database = FirebaseDatabase.getInstance();
            boxUser = database.getReference("Boxes/" + currentUserFb.getDisplayName());

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            i = new Intent(getApplicationContext(), ManageBox.class);
            contentIntent = PendingIntent.getActivity(getBaseContext(), 0, i, 0);

            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ico2)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ico12))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(contentIntent);

            boxUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                    {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Box box = snapshot.getValue(Box.class);
                            if (box.getAvailable() < 6) {
                                notificationBuilder.setContentTitle(String.valueOf("Box " + box.getBoxNumber()))
                                        .setContentText("Only " + box.getAvailable() + " pills of " + box.getIllness() + " left.");
                                Notification n = notificationBuilder.build();
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(box.getBoxNumber(), n);
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
}
