package com.example.joel.navdrawers;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MessagePlayerActivity extends AppCompatActivity {


    /**
     * using static for now because this is going to be the only playpausebutton in our app
     **/
    static FloatingActionButton playPauseButton;
    private String MUSIC_URL = "https://pastoroti.org/audiodevotional/wp-content/uploads/sites/4/2018/09/21st-September-2018-My-Prayer-For-You-5-Mins-with-Pastor-Oti-Love-Economy.mp3";
    PlayerService mBoundService;
    boolean mServiceBound = false;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            PlayerService.MyBinder myBinder = (PlayerService.MyBinder) service;
            mBoundService = myBinder.getService();
            mServiceBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mServiceBound = false;
        }
    };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isPlaying = intent.getBooleanExtra("isPlaying", false);
            flipPlayPauseButton(isPlaying);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        playPauseButton = findViewById(R.id.audioStreamBtn);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mServiceBound) {
                    mBoundService.togglePlayer();
                }
            }
        });

        startStreamingService(MUSIC_URL);
    }

    private void startStreamingService(String url) {
        Intent i = new Intent(this, PlayerService.class);
        i.putExtra("url", MUSIC_URL);
        i.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(i);
        bindService(i, mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBound) {
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("changePlayButton"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    public static void flipPlayPauseButton(boolean isPlaying) {
        if (isPlaying) {
            playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            playPauseButton.setImageResource(android.R.drawable.ic_media_play);
        }
    }
}


//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!playPause) {
//                    btn.setText("Pause Streaming");
//
//                    if (initialStage) {
//                        new Player().execute(MUSIC_URL);
//                    } else {
//                        if (!mediaPlayer.isPlaying())
//                            mediaPlayer.start();
//                    }
//
//                    playPause = true;
//
//                } else {
//                    btn.setText("Launch Streaming");
//
//                    if (mediaPlayer.isPlaying()) {
//                        mediaPlayer.pause();
//                    }
//
//                    playPause = false;
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        if (mediaPlayer != null) {
//            mediaPlayer.reset();
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//    }
//
//    class Player extends AsyncTask<String, Void, Boolean> {
//        @Override
//        protected Boolean doInBackground(String... strings) {
//            Boolean prepared = false;
//
//            try {
//                mediaPlayer.setDataSource(strings[0]);
//                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mediaPlayer) {
//                        initialStage = true;
//                        playPause = false;
//                        btn.setText("Launch Streaming");
//                        mediaPlayer.stop();
//                        mediaPlayer.reset();
//                    }
//                });
//
//                mediaPlayer.prepare();
//                prepared = true;
//
//            } catch (Exception e) {
//                Log.e("MyAudioStreamingApp", e.getMessage());
//                prepared = false;
//            }
//
//            return prepared;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//
//            if (progressDialog.isShowing()) {
//                progressDialog.cancel();
//            }
//
//            mediaPlayer.start();
//            initialStage = false;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            progressDialog.setMessage("Buffering...");
//            progressDialog.show();
//        }
//    }
//}