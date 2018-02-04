package com.angusgaming.fountainparkapts;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import android.util.Log;

/**
 * Created by Harry Cliff on 2/4/2018.
 */

public class MediaPlayerUtility {

    private Track currentlyPlayingTrack;
    private Context context;

    public MediaPlayerUtility(Context context){
        this.context =context;
    }

    public void playSong(Track track, Album album){
        if (track.getMediaPlayer() != null) {
            if (isSongPlaying() && track != currentlyPlayingTrack) {
                stopCurrentSong();
            }
            startSong(track);
            currentlyPlayingTrack.getMediaPlayer().setOnCompletionListener(
                    mp -> playNextSong(track, album));
        }
    }

    private void playNextSong(Track track, Album album) {
        try {
            playSong(
                    album.getTrackList().get(
                            Integer.parseInt(getMetaData(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER,
                                    track.getMediaPlayerDataSource()))),
                    album);
        } catch (IndexOutOfBoundsException e){
            Log.e("FPA", e.getMessage());
        }
    }

    public void pauseCurrentSong(){
        currentlyPlayingTrack.getMediaPlayer().pause();
    }

    private void stopCurrentSong() {
        currentlyPlayingTrack.getMediaPlayer().stop();
    }

    private void startSong(Track track) {
        currentlyPlayingTrack = track;
        track.getMediaPlayer().start();
    }

    public boolean isSongPlaying() {
        return currentlyPlayingTrack != null && currentlyPlayingTrack.getMediaPlayer().isPlaying();
    }

    public boolean isCurrentSong(Track track){
        return track == currentlyPlayingTrack;
    }
    public String getMetaData(int metadataKey, int mediaDataSource) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        AssetFileDescriptor afd= context.getResources()
                .openRawResourceFd(mediaDataSource);
        mmr.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());

        return mmr.extractMetadata(metadataKey);
    }
}
