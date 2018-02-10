package com.angusgaming.fountainparkapts;

/**
 * Created by Harry Cliff on 2/1/2018.
 */

public class Track {

    private String mediaPlayerDataSource;

    public Track(String mediaPlayerDataSource) {
        this.mediaPlayerDataSource = mediaPlayerDataSource;
    }

    public String getMediaPlayerDataSource() {
        return mediaPlayerDataSource;
    }

    public boolean hasLyrics() {
        return false;
    }
}
