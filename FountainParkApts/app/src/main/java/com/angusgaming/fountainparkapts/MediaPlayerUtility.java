package com.angusgaming.fountainparkapts;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;

import com.angusgaming.fountainparkapts.fragment.PlayerFragment;

import java.io.IOException;

/**
 * Created by Harry Cliff on 2/4/2018.
 */

public class MediaPlayerUtility {

    private Track currentlyPlayingTrack;
    private Album currentlyPlayingAlbum;
    private PlayerFragment playerFragment;
    private Context context;

    public MediaPlayerUtility(Context context, PlayerFragment playerFragment){
        this.context = context;
        this.playerFragment = playerFragment;
    }

    public void playSong(Track track, Album album){
        if (track.getMediaPlayer() != null) {
            if (isSongPlaying() && track != currentlyPlayingTrack) {
                currentlyPlayingTrack.getMediaPlayer().stop();
            }
            startSong(track, album);
            currentlyPlayingTrack.getMediaPlayer().setOnCompletionListener(
                    mp -> playNextSong());

            playerFragment.getNextButton().setVisibility(View.VISIBLE);
            playerFragment.getPlayPauseButton().setVisibility(View.VISIBLE);
            playerFragment.getPreviousButtton().setVisibility(View.VISIBLE);
            playerFragment.getPlayPauseButton().setImageDrawable(
                    context.getResources().getDrawable(R.drawable.ic_pause_black_24dp));

            playerFragment.getSongName().setText(getMetaData(MediaMetadataRetriever.METADATA_KEY_TITLE,
                    currentlyPlayingTrack.getMediaPlayerDataSource()));
            playerFragment.getAlbumName().setText(getMetaData(MediaMetadataRetriever.METADATA_KEY_ALBUM,
                    currentlyPlayingTrack.getMediaPlayerDataSource()));
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
            playerFragment.getPreviousButtton().setVisibility(View.INVISIBLE);
            playerFragment.getNextButton().setVisibility(View.INVISIBLE);
            playerFragment.getPlayPauseButton().setVisibility(View.INVISIBLE);
            playerFragment.getSongName().setText("");
            playerFragment.getAlbumName().setText("");
            currentlyPlayingTrack.getMediaPlayer().stop();
            Log.e("FPA", e.getMessage());
        }
    }

    public void pauseCurrentSong(){
        currentlyPlayingTrack.getMediaPlayer().pause();
        playerFragment.getPlayPauseButton().setImageDrawable(
                context.getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
    }

    private void startSong(Track track, Album album) {
        currentlyPlayingTrack = track;
        currentlyPlayingAlbum = album;
        track.getMediaPlayer().stop();
        try {
            track.getMediaPlayer().prepare();
            track.getMediaPlayer().start();
        } catch (IOException e) {
            track.getMediaPlayer().setOnPreparedListener(MediaPlayer::start);
            track.getMediaPlayer().prepareAsync();
        }
    }

    public boolean isSongPlaying() {
        try {
            return currentlyPlayingTrack != null && currentlyPlayingTrack.getMediaPlayer().isPlaying();
        } catch (IllegalStateException e){
            return false;
        }
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

    public void playPrevious(){
        playPrevious(currentlyPlayingTrack, currentlyPlayingAlbum);
    }

    public void playPrevious(Track track, Album album) {
        if(currentlyPlayingTrack.getMediaPlayer().getCurrentPosition() > 3000){
            currentlyPlayingTrack.getMediaPlayer().stop();
            playSong(currentlyPlayingTrack, currentlyPlayingAlbum);
        } else {
            try {
                playSong(
                        album.getTrackList().get(
                                Integer.parseInt(getMetaData(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER,
                                        track.getMediaPlayerDataSource()))-2),
                        album);
            } catch (IndexOutOfBoundsException e){
                playerFragment.getNextButton().setVisibility(View.INVISIBLE);
                playerFragment.getPreviousButtton().setVisibility(View.INVISIBLE);
                playerFragment.getPlayPauseButton().setVisibility(View.INVISIBLE);
                currentlyPlayingTrack.getMediaPlayer().stop();
                playerFragment.getSongName().setText("");
                playerFragment.getAlbumName().setText("");
                Log.e("FPA", e.getMessage());
            }
        }
    }

    public void playNextSong() {
        playNextSong(currentlyPlayingTrack, currentlyPlayingAlbum);

    }

    public void resumeSong() {
        currentlyPlayingTrack.getMediaPlayer().start();
        playerFragment.getPlayPauseButton().setImageDrawable(
                context.getResources().getDrawable(R.drawable.ic_pause_black_24dp));
    }
}
