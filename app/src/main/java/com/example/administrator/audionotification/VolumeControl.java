package com.example.administrator.audionotification;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.IBinder;
import android.widget.RemoteViews;

/**
 * Created by Administrator on 7/25/2016.
 */
public class VolumeControl extends Service {
    String mode="Music";
    int heightNoti;
    BroadcastReceiver checkChange;
    int valueMusic=0,valueBell=0,valueAlarm=0;
    Bitmap p1,p2,p3;
    Notification noti;
    RemoteViews views;
    AudioManager myAudioManager;

        @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction().equals("StartVolumeControl"))    {
            heightNoti=intent.getIntExtra("height",60);
            Bitmap p = BitmapFactory.decodeResource(getResources(),R.drawable.bell);
            p1=Bitmap.createScaledBitmap(p,heightNoti,heightNoti,true);
            p = BitmapFactory.decodeResource(getResources(),R.drawable.alarm);
            p2=Bitmap.createScaledBitmap(p,heightNoti,heightNoti,true);
            p = BitmapFactory.decodeResource(getResources(),R.drawable.music);
            p3=Bitmap.createScaledBitmap(p,heightNoti,heightNoti,true);
            myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            showNotification();
        }
        switch (intent.getAction()){
            case "IconVolume":
                if(mode.equals("Music")){
                    mode = "Bell";
                    views.setImageViewBitmap(R.id.mode,p1);
                    valueBell=myAudioManager.getStreamVolume(AudioManager.STREAM_RING);
                    views.setTextViewText(R.id.text,"Bell: "+valueVolume(myAudioManager.getStreamVolume(AudioManager.STREAM_RING))+"%");
                }
                else{
                    if(mode.equals("Bell")){
                        mode = "Alarm";
                        Bitmap p = BitmapFactory.decodeResource(getResources(),R.drawable.alarm);
                        views.setImageViewBitmap(R.id.mode,p2);

                        valueAlarm=myAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
                        views.setTextViewText(R.id.text,"Alarm: "+valueVolume(myAudioManager.getStreamVolume(AudioManager.STREAM_ALARM))+"%");
                    }
                    else {
                        mode = "Music";
                        views.setImageViewBitmap(R.id.mode,p3);
                        valueMusic=myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        views.setTextViewText(R.id.text,"Music: "+valueVolume(myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC))+"%");
                    }
                }
                break;
            case "UpVolume":
                if(mode.equals("Music")){
                    valueMusic = Math.min(valueMusic+1,15);
                    myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,valueMusic,0);
                    views.setTextViewText(R.id.text,"Music: "+valueVolume(myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC))+"%");
                }
                if(mode.equals("Alarm")){
                    valueAlarm = Math.min(valueAlarm+1,15);
                    myAudioManager.setStreamVolume(AudioManager.STREAM_ALARM,valueAlarm,0);
                    views.setTextViewText(R.id.text,"Alarm: "+valueVolume(myAudioManager.getStreamVolume(AudioManager.STREAM_ALARM))+"%");
                }
                if(mode.equals("Bell")){
                    valueBell = Math.min(valueBell+1,15);
                    myAudioManager.setStreamVolume(AudioManager.STREAM_RING,valueBell,0);
                    views.setTextViewText(R.id.text,"Bell: "+valueVolume(myAudioManager.getStreamVolume(AudioManager.STREAM_RING))+"%");
                }
                break;
            case "DownVolume":
                if(mode.equals("Music")){
                    valueMusic = Math.max(valueMusic-1,0);
                    myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,valueMusic,0);
                    views.setTextViewText(R.id.text,"Music: "+valueVolume(myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC))+"%");
                }
                if(mode.equals("Alarm")){
                    valueAlarm = Math.max(valueAlarm-1,0);
                    myAudioManager.setStreamVolume(AudioManager.STREAM_ALARM,valueAlarm,0);
                    views.setTextViewText(R.id.text,"Alarm: "+valueVolume(myAudioManager.getStreamVolume(AudioManager.STREAM_ALARM))+"%");
                }
                if(mode.equals("Bell")){
                    valueBell = Math.max(valueBell-1,0);
                    myAudioManager.setStreamVolume(AudioManager.STREAM_RING,valueBell,0);
                    views.setTextViewText(R.id.text,"Bell: "+valueVolume(myAudioManager.getStreamVolume(AudioManager.STREAM_RING))+"%");
                }                break;
        }
        startForeground(10,noti);
        return START_STICKY;
    }
    public void showNotification(){

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction("Demo");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);


        views = new RemoteViews(getPackageName(),R.layout.services_volumecontrol);
        //image
        Intent intentIcon = new Intent(this, VolumeControl.class);
        intentIcon.setAction("IconVolume");
        PendingIntent pIntentIcon = PendingIntent.getService(this, 0,
                intentIcon, 0);
        //up
        Intent intentUp = new Intent(this, VolumeControl.class);
        intentUp.setAction("UpVolume");
        PendingIntent pIntentUp = PendingIntent.getService(this, 0,
                intentUp, 0);
        //down
        Intent intentDown = new Intent(this, VolumeControl.class);
        intentDown.setAction("DownVolume");
        PendingIntent pIntentDown = PendingIntent.getService(this, 0,
                intentDown, 0);
        //Text
        Intent intentText = new Intent(this, VolumeControl.class);
        intentText.setAction("IconVolume");
        PendingIntent pIntentText = PendingIntent.getService(this, 0,
                intentText, 0);

        views.setOnClickPendingIntent(R.id.mode, pIntentIcon);
        views.setOnClickPendingIntent(R.id.Up, pIntentUp);
        views.setOnClickPendingIntent(R.id.Down, pIntentDown);
        views.setOnClickPendingIntent(R.id.mode,pIntentIcon);
        valueMusic=myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        views.setTextViewText(R.id.text,"Music: "+valueVolume(valueMusic)+"%");


        views.setImageViewBitmap(R.id.mode,p3);

        noti = new Notification.Builder(this).build();
        noti.contentView = views;
        noti.flags = Notification.FLAG_ONGOING_EVENT;
        noti.icon = R.mipmap.ic_launcher;
        noti.contentIntent = pendingIntent;

        startForeground(10, noti);


        //bat sk tang giam am luong
        IntentFilter filter = new IntentFilter("android.media.VOLUME_CHANGED_ACTION");
        checkChange = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                    valueMusic=myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    if(mode.equals("Music"))
                        views.setTextViewText(R.id.text,"Music: "+valueVolume(myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC))+"%");

                    valueAlarm=myAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
                    if(mode.equals("Alarm"))
                        views.setTextViewText(R.id.text,"Alarm: "+valueVolume(myAudioManager.getStreamVolume(AudioManager.STREAM_ALARM))+"%");

                    valueBell=myAudioManager.getStreamVolume(AudioManager.STREAM_RING);
                    if(mode.equals("Bell"))
                        views.setTextViewText(R.id.text,"Bell: "+valueVolume(myAudioManager.getStreamVolume(AudioManager.STREAM_RING))+"%");
                startForeground(10, noti);
            }
        };
        registerReceiver(checkChange,filter);
    }
    public int valueVolume(int value){
        return (int) ((value*1.0/15.0)*100.0);
    }


}
