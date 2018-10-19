package com.example.joel.navdrawers;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

public class PlayerService extends Service {
    MediaPlayer mediaPlayer = new MediaPlayer();
    private final IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder{
        PlayerService getService(){
            return PlayerService.this;
        }
    }


    public PlayerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getStringExtra("url")!= null)
        playStream(intent.getStringExtra("url"));

        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i("info", "start foreground Service");
            showNotification();
        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)){
            Log.i("info", "Prev pressed");
        }else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)){
            Log.i("info", "Play pressed");
            togglePlayer();
        }else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)){
            Log.i("info", "Next pressed");
        }else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)){
            Log.i("info", "Stop Foreground received");
            stopForeground(true);
            stopSelf();
        }

        return START_REDELIVER_INTENT;
    }
    private void showNotification(){

        Intent notificationIntent = new Intent(this,MessagePlayerActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
      //  notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);


        Intent previousIntent = new Intent(this,PlayerService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getActivity(this,0,previousIntent,0);

        Intent playIntent = new Intent(this,PlayerService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getActivity(this,0,playIntent,0);


        Intent nextIntent = new Intent(this,PlayerService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getActivity(this,0,nextIntent,0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.message_pic);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Music Player")
                .setTicker("Playing Music")
                .setContentText("Message")
                .setSmallIcon(R.drawable.message_pic)
                .setLargeIcon(Bitmap.createScaledBitmap(icon,128,128,false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_previous,"Previous",ppreviousIntent)
                .addAction(android.R.drawable.ic_media_play,"Play",pplayIntent)
                .addAction(android.R.drawable.ic_media_next,"Next",pnextIntent)
                .build();

        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,notification);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    public void playStream(String url) {

        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
            } catch (Exception e) {
            }

        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    playPlayer();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    flipPlayPauseButton(false);
                }
            });
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pausePlayer() {
        try {
            mediaPlayer.pause();
            flipPlayPauseButton(false);
        } catch (Exception e) {
            Log.d("EXCEPTION", "Failed to pause Media Player");
        }
    }

    public void playPlayer() {
        try {
            mediaPlayer.start();
            flipPlayPauseButton(true);
        } catch (Exception e) {
            Log.d("EXCEPTION", "Failed to play Media Player");
        }
    }

    public void flipPlayPauseButton(boolean isPlaying){
        Intent intent = new Intent("changePlayButton");
        intent.putExtra("isPlaying",isPlaying);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void togglePlayer() {
        try {
            if (mediaPlayer.isPlaying())
                pausePlayer();
            else
                playPlayer();

        } catch (Exception e) {
            Log.d("EXCEPTION ", "Failed to toggle media player");
        }
    }
}
