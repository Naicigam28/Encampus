package com.viper.team.encampus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MessageNotificationService extends Service {
    private Timer timer;
    private TimerTask timerTask;
    private DatabaseReference mDatabase;
    private static final String CHANNEL_1_ID="EncampusNotifictionChannel";
    private static final String CHANNEL_2_ID="EncampusNotifictionReminderChannel";


    public MessageNotificationService(){}

    public MessageNotificationService(Context applicationContext) {
        super();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(),"Service started",Toast.LENGTH_LONG);
       // super.onStartCommand(intent, flags, startId);

        checkMessages();
        return START_STICKY;
    }

    void checkMessages() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 1000, 60000); //
    }

    private void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                final DatabaseReference userName = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                final  NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(getApplicationContext(),CHANNEL_1_ID);
                final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setSmallIcon(R.drawable.icon);
                mBuilder.setContentTitle("Encampus");
                mBuilder.setContentText("You might have new messages please login to check : ");
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI
                        final User user = dataSnapshot.getValue(User.class);
                        DatabaseReference rDatabase=FirebaseDatabase.getInstance().getReference().child("Messages").child(user.getStudentID().toLowerCase()).child("reminders");
                        rDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                for (DataSnapshot data:dataSnapshot.getChildren())
                                {
                                    Reminder msg=data.getValue(Reminder.class);
                                    Date date=new Date();
                                    SimpleDateFormat time= new SimpleDateFormat("HH:mm");
                                    SimpleDateFormat dateformat= new SimpleDateFormat("DD MM YYYY");
                                    try {
                                        if(date.getTime()>=time.parse(msg.time).getTime())
                                        {
                                           NotificationCompat.Builder rBuilder =new NotificationCompat.Builder(getApplicationContext(),CHANNEL_2_ID);
                                           Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                           PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                                           rBuilder.setContentIntent(pendingIntent);
                                           rBuilder.setSmallIcon(R.drawable.icon);
                                           rBuilder.setContentTitle("Encampus Reminder: "+msg.name);
                                           rBuilder.setContentText(msg.time+"\n"+msg.data);
                                           NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                           mNotificationManager.notify(1, rBuilder.build());
                                           data.getRef().removeValue();
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        mDatabase = FirebaseDatabase.getInstance().getReference("Messages").child(user.getStudentID().toLowerCase()).child("inbox");
                        mDatabase.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                for (DataSnapshot snap:dataSnapshot.getChildren())
                                {
                                    Message msg=snap.getValue(Message.class);
                                    if(!msg.isRead())
                                    {
                                        mBuilder.setContentText("You might Have new messages ");
                                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                        mNotificationManager.notify(1, mBuilder.build());
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });


                        // ...
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT);
                        // ...
                    }
                };
                userName.addValueEventListener(postListener);
            }
        };
    }
}
