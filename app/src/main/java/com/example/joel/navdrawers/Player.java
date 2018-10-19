package com.example.joel.navdrawers;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class Player {
    MediaPlayer mediaPlayer = new MediaPlayer();
    public static Player player;
    String url ="";


    public Player() {
        this.player = this;
    }
    public void playStream(String url){

        if (mediaPlayer!= null){
            try {
                mediaPlayer.stop();
            }catch (Exception e){}

        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try{
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
                    MessagePlayerActivity.flipPlayPauseButton(false);
                }
            });
            mediaPlayer.prepareAsync();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void pausePlayer(){
        try{
            mediaPlayer.pause();
            MessagePlayerActivity.flipPlayPauseButton(false);
        }catch (Exception e){
            Log.d("EXCEPTION","Failed to pause Media Player");
        }
    }
    public void playPlayer(){
        try{
            mediaPlayer.start();
            MessagePlayerActivity.flipPlayPauseButton(true);
        }catch (Exception e){
            Log.d("EXCEPTION","Failed to play Media Player");
        }
    }
    public void togglePlayer(){
        try {
            if (mediaPlayer.isPlaying())
                pausePlayer();
            else
                playPlayer();

        }catch(Exception e){
        Log.d("EXCEPTION ", "Failed to toggle media player");
        }
    }
}
