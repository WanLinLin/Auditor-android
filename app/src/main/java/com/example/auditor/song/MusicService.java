package com.example.auditor.song;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.auditor.SlidingTabActivity;
import com.example.auditor.Song;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Wan Lin on 15/7/2.
 * MusicService
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    private static final String LOG_TAG = "MusicService";

    private String wavDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Auditor/wav/";
    private MediaPlayer player;
    private ArrayList<Song> songs;
    private int songPosition; // current song position
    private final IBinder musicBind = new MusicBinder();
    private Song curPlaySong;
    private Random rand;
    private Intent intent;

    private boolean shuffle = false;

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void onCreate(){
        super.onCreate();

        intent = new Intent("Player");
        songPosition = 0;
        player = new MediaPlayer();
        rand = new Random();

        initMusicPlayer();
    }

    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setVolume(1, 1);
    }

    public void setList(ArrayList<Song> theSongs){
        songs = theSongs;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        if (player.isPlaying()) {
            player.stop();
            player.release();
        }

        // Return true if you would like to have the service's
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // start playback
        mp.start();

        Intent notIntent = new Intent(this, SlidingTabActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // send intent to activity
        intent.putExtra("action", "prepared");
        intent.putExtra("song time", player.getDuration());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, "Music service on destroy!");
        player = null;

        stopForeground(true);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        Log.e(LOG_TAG, "Music service on error and reset!");
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(mp.getCurrentPosition() > 0){
            intent.putExtra("action", "complete");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    public void playSong(){
        player.reset();

        curPlaySong = songs.get(songPosition);
        String songPath = wavDir + curPlaySong.getTitle();

        try{
            player.setDataSource(songPath);
            player.prepareAsync();
            intent.putExtra("action", "play");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
    }

    public void setSong(int songIndex){
        songPosition = songIndex;
    }

    public int getPosition(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPlaying(){
        boolean isPlaying = false;

        try {
            isPlaying = player.isPlaying();
        }
        catch (IllegalStateException e) {
            Log.e(LOG_TAG, "isPlaying illegal");
            player = null;
            player = new MediaPlayer();
        }

        return isPlaying;
    }

    public void pausePlayer(){
        intent.putExtra("action", "pause");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        player.pause();
    }

    public void seek(int position){
        player.seekTo(position);
    }

    /**
     * play
     */
    public void go(){
        intent.putExtra("action", "go");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        player.start();
    }

    public void setShuffle() {
        if(shuffle) shuffle = false;
        else shuffle = true;
    }

    /**
     * skip to previous
     */
    public void playPrev(){
        songPosition--;
        if(songPosition < 0) songPosition = songs.size() - 1;
        playSong();
    }

    /**
     * skip to next
     */
    public void playNext(){
        if(shuffle){
            int newSong = songPosition;
            while(newSong == songPosition){
                newSong = rand.nextInt(songs.size());
            }
            songPosition = newSong;
        }
        else{
            songPosition++;
            if(songPosition >= songs.size()) songPosition = 0;
        }
        playSong();
    }

    public boolean isShuffle(){
        return shuffle;
    }
}
