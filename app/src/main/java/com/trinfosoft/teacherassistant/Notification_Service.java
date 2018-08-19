package com.trinfosoft.teacherassistant;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static java.lang.Thread.sleep;

/**
 * Created by Trilokynath Wagh on 02/03/2018.
 */

public class Notification_Service extends Service {
    private Looper mServiceLooper;
    // database
    DatabaseHelper DatabaseHelper;

    // contains our listview items
    ArrayList<Time_Data> listItems;

    // list of todo titles
    ArrayList<Integer> ID;
    ArrayList<String> data_start_h;
    ArrayList<String> data_start_m;
    ArrayList<String> data_subject;


    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
      //  HandlerThread thread = new HandlerThread("ServiceStartArguments",
      //          THREAD_PRIORITY_BACKGROUND);
       // thread.start();


        // Creating a new instance of our DatabaseHelper, which we've created earlier

        DatabaseHelper = new DatabaseHelper(this);


        // This returns a list of all our current available notes
        listItems = DatabaseHelper.getAllNotes_Tuesday();

        ID = new ArrayList<Integer>();
        data_start_h = new ArrayList<String>();
        data_start_m = new ArrayList<String>();
        data_subject = new ArrayList<String>();


    }

    public void check(){

        int notification_id = 0;
        Calendar rightNow = Calendar.getInstance();
        int day = rightNow.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                listItems = DatabaseHelper.getAllNotes_Monday();
                notification_id = 10000;
                break;

            case Calendar.TUESDAY:
                listItems = DatabaseHelper.getAllNotes_Tuesday();
                notification_id = 20000;
                break;

            case Calendar.WEDNESDAY:
                listItems = DatabaseHelper.getAllNotes_Wednesday();
                notification_id = 30000;
                break;

            case Calendar.THURSDAY:
                listItems = DatabaseHelper.getAllNotes_Thursday();
                notification_id = 40000;
                break;

            case Calendar.FRIDAY:
                listItems = DatabaseHelper.getAllNotes_Friday();
                notification_id = 50000;
                break;

            case Calendar.SATURDAY:
                listItems = DatabaseHelper.getAllNotes_Saturday();
                notification_id = 60000;
                break;
        }

        for (Time_Data note : listItems) {
            ID.add(note.Id+notification_id);
            data_start_h.add(note.start_h);
            data_start_m.add(note.start_m);
            data_subject.add(note.subject);
        }
        //Toast.makeText(Notification_Service.this,".", Toast.LENGTH_LONG).show();

        notification_manager(data_start_h,data_start_m, data_subject, ID);
        data_start_h.clear();
        data_start_m.clear();
        data_subject.clear();
        ID.clear();
    }


    public void notification_manager( ArrayList<String> start, ArrayList<String> stop,
                                      ArrayList<String> subject, ArrayList<Integer> ID){

        Calendar rightNow = Calendar.getInstance();

        int current_hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int current_min = rightNow.get(Calendar.MINUTE);

        int hour_data;
        int min_data;

        //  Toast.makeText(Notification_Service.this,".", Toast.LENGTH_LONG).show();



        for(int i=0;i<start.size();i++) {

            hour_data = Integer.parseInt(start.get(i));
            min_data = Integer.parseInt(stop.get(i));

            int save_hour = hour_data;
            int save_min = min_data;

            int h,m;

            if(min_data==0){
                h=hour_data-1;
                m=(60+(min_data-10));
                min_data = min_data+60;
               // current_min = current_min;
            }
            else{
                h=hour_data;
                m=min_data-10;
            }

            int status = 1;

            SharedPreferences prefs = getSharedPreferences("notification", MODE_PRIVATE);

            if(prefs!=null)
                status = prefs.getInt("noti", 0);


            if (min_data >= current_min && m <= current_min && hour_data >= current_hour && h <= current_hour) {
                if (status != ID.get(i)) {
                    //display the notification
                    displaynotification(subject.get(i), save_hour, save_min);

                    //save the status to the database that the notification has been send to the user
                    SharedPreferences.Editor editor = getSharedPreferences("notification", MODE_PRIVATE).edit();
                    editor.putInt("noti", ID.get(i));
                    editor.apply();

                    break;
                }
            }else {
                if (status == ID.get(i)) {

                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(1);

                    //save the status to the database that the notification has been send to the user
                    SharedPreferences.Editor editor = getSharedPreferences("notification", MODE_PRIVATE).edit();
                    editor.putInt("noti", 0);
                    editor.apply();

                }
            }

        }
    }


    public void displaynotification(String subject , int start, int stop){


        String aMpM = "AM";

        if(start >11) {
            aMpM = "PM";
        }
        //Make the 24 hour time format to 12 hour time format
        int currentHour;

        if(start>11) {
            currentHour = start - 12;
        }
        else {
            currentHour = start;
        }

        if(currentHour==0){
            currentHour = 12;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            builder.setSmallIcon(android.R.drawable.ic_popup_reminder);
    /*      Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"));
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
            builder.setContentIntent(pendingIntent);*/
            builder.setContentTitle("SUBJECT: " + subject);
            builder.setContentText("TIME: " + String.format("%02d", currentHour) + ":" + String.format("%02d", stop) + " "+aMpM);
            builder.setSubText("Tap to view the detail.");
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic));
            builder.setOngoing(true); //notification is not dissmissble
/*
            Intent notificationIntent = new Intent(this, MainActivity.class);

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(contentIntent);
*/
            builder.setAutoCancel(true);
            builder.setLights(Color.BLUE, 500, 500);
//        long[] pattern = {500,500,500,500,500,500,500,500,500};
//        builder.setVibrate(pattern);
            builder.setStyle(new NotificationCompat.InboxStyle());

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Will display the notification in the notification bar
            notificationManager.notify(1, builder.build());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int status = 0;

        SharedPreferences prefs = getSharedPreferences("status", MODE_PRIVATE);
        if(prefs!=null)
            status = prefs.getInt("st", 0);

        if (status != 0) {
            check();
        }

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent alarmIntent = new Intent(Notification_Service.this, Notification_Service.class);

            PendingIntent pendingIntent = PendingIntent.getService(Notification_Service.this, 1, alarmIntent, 0);

            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);
        // If we get killed, after returning from here, restart

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
 //       Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();

    }

}