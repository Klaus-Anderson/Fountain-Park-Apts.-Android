package com.angusgaming.fountainparkapts;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
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

            Notification.Builder builder = new Notification.Builder(this);

            builder.setContentIntent(pendInt)
                    .setSmallIcon(R.drawable.fpa_cover)
                    .setTicker("song title")
                    .setOngoing(true)
                    .setContentTitle("Playing")
                    .setContentText("song title");
            Notification not = builder.build();

            startForeground(NOTIFY_ID, not);
        }


    }

    public void playSong(){
        //play a song
        player.reset();

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
        player.release();
        mainActivity.stopSong();
    }

    //skip to next
    public void playNext(){
        currentSong++;
        if(currentSong == albums.get(currentAlbum).getTrackList().size()) stopSong();
        else playSong();
    }
}
