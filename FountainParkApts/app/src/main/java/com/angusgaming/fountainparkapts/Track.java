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

    public Track(Context context, int mediaPlayerDataSource) {
        this.context = context;
        this.mediaPlayerDataSource = mediaPlayerDataSource;
        this.mediaPlayer = MediaPlayer.create(context, mediaPlayerDataSource);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public int getMediaPlayerDataSource() {
        return mediaPlayerDataSource;
    }
}
