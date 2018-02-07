package com.angusgaming.fountainparkapts;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Harry Cliff on 2/1/2018.
 */

public class Track {

    MediaPlayer mediaPlayer;
    int mediaPlayerDataSource;
    Context context;
    String lyrics;

    public Track(Context context, int mediaPlayerDataSource, String lyrics) {
        this.context = context;
        this.mediaPlayerDataSource = mediaPlayerDataSource;
        this.mediaPlayer = MediaPlayer.create(context, mediaPlayerDataSource);
        this.lyrics = lyrics;
    }

    public Track(Context context, int mediaPlayerDataSource) {
        this(context, mediaPlayerDataSource, null);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public int getMediaPlayerDataSource() {
        return mediaPlayerDataSource;
    }

    public String getLyrics() {
        return lyrics;
    }

    public boolean hasLyrics(){
        return lyrics != null && lyrics.length() != 0;
    }
}
