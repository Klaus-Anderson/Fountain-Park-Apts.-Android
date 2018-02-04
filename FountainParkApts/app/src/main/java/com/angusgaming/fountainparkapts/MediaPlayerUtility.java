package com.angusgaming.fountainparkapts;

import android.media.MediaPlayer;

/**
 * Created by Harry Cliff on 2/4/2018.
 */

public class MediaPlayerUtility {

    private MediaPlayer currentlyPlayingSong;

    public void playSong(MediaPlayer mediaPlayer){
        if (isSongPlaying() && mediaPlayer != currentlyPlayingSong) {
            stopCurrentSong();
        }
        startSong(mediaPlayer);
    }

    public void pauseCurrentSong(){
        currentlyPlayingSong.pause();
    }

    private void stopCurrentSong() {
        currentlyPlayingSong.stop();
    }

    private void startSong(MediaPlayer mediaPlayer) {
        currentlyPlayingSong = mediaPlayer;
        mediaPlayer.start();
    }

    public boolean isSongPlaying() {
        return currentlyPlayingSong != null && currentlyPlayingSong.isPlaying();
    }

    public boolean isCurrentSong(MediaPlayer mediaPlayer){
        return mediaPlayer == currentlyPlayingSong;
    }
}
