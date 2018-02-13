package com.angusgaming.fountainparkapts;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.List;

/**
 * Created by Harry Cliff on 2/9/2018.
 */

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private String songTitle="";
    private static final int NOTIFY_ID=1;
    //media player
    private MediaPlayer player;
    //song list
    private List<Album> albums;
    //current position
    private int currentAlbum;
    private int currentSong;
    private final IBinder musicBind = new MusicBinder();
    private MainActivity mainActivity;

    private MediaSessionManager mediaSessionManager;
    private MediaSession mediaSession;

    public void onCreate(){
        //create the service
        //create the service
        super.onCreate();
        //initialize position
        currentAlbum=0;
        currentSong=0;
        //create player
        player = new MediaPlayer();

        initMusicPlayer();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaSession = new MediaSession(this, "MusicService");
            mediaSession.setCallback(new MediaSessionCallback());
            mediaSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

            Context context = getApplicationContext();
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 99 /*request code*/,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mediaSession.setSessionActivity(pi);

            Bundle mediaSessionExtras = new Bundle();
            mediaSession.setExtras(mediaSessionExtras);
        }
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setAlbums(List<Album> albums){
        this.albums = albums;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition()>0){
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }
    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            Intent notIntent = new Intent(this, MainActivity.class);
            notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                    notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }


    }

    public void playSong(){
        //play a song
        try {
            player.reset();
        } catch (IllegalStateException ise){}

        //get song
        Track playSong = albums.get(currentAlbum).getTrackList().get(currentSong);
        //get id
        String currSong = playSong.getMediaPlayerDataSource();
        //set uri
        Uri trackUri = Uri.parse("android.resource://"+getPackageName()+"/raw/"+ currSong);

        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        mainActivity.setSongInfo(playSong);
        player.prepareAsync();
    }

    public void setSong(int albumIndex, int songIndex){
        currentAlbum = albumIndex;
        currentSong = songIndex;
    }

    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }

    public void playPrev(){
        currentSong--;
        if(currentSong<0) stopSong();
        else playSong();

    }

    private void stopSong() {
        player.stop();
        mainActivity.stopSong();
    }

    //skip to next
    public void playNext(){
        currentSong++;
        if(currentSong == albums.get(currentAlbum).getTrackList().size()) stopSong();
        else playSong();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private final class MediaSessionCallback extends MediaSession.Callback {
        @Override
        public void onPlay() {
            go();
        }

        @Override
        public void onSkipToQueueItem(long queueId) {
        }

        @Override
        public void onSeekTo(long position) {
        }

        @Override
        public void onPause() {
            pausePlayer();
        }

        @Override
        public void onStop() {
        }

        @Override
        public void onSkipToNext() {
            playNext();
        }

        @Override
        public void onSkipToPrevious() {
            playPrev();
        }
    }
}
