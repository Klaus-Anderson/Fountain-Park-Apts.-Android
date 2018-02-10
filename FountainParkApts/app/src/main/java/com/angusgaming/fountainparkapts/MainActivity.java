package com.angusgaming.fountainparkapts;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {

    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    private List<Album> albumList;
    private View playerView;

    private ExpandableListAdapter expandableListAdapter;

    private ImageView previousButtton, playPauseButton, nextButton;
    private TextView songName, albumName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getMusic();

        ExpandableListView expandableListView = findViewById(R.id.expandable_list_view);

        expandableListAdapter = new ExpandableListAdapter(this, albumList);

        expandableListView.setAdapter(expandableListAdapter);

        playerView = findViewById(R.id.player_view);

        previousButtton = findViewById(R.id.previousButton);
        playPauseButton = findViewById(R.id.playButton);
        nextButton = findViewById(R.id.nextButton);
        songName = findViewById(R.id.song_name_textview);
        albumName = findViewById(R.id.album_name_textview);

        previousButtton.setOnClickListener(v -> playPrev());
        nextButton.setOnClickListener(v -> playNext());
        playPauseButton.setOnClickListener(v -> {
            if(isPlaying()) pause();
            else start();
        });
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setAlbums(albumList);
            musicBound = true;

            musicSrv.setMainActivity(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }


    @Override
    public void start() {
        musicSrv.go();
        playerView.setVisibility(View.VISIBLE);
        playPauseButton.setImageResource(R.drawable.ic_pause_black_24dp);
    }

    @Override
    public void pause() {
        musicSrv.pausePlayer();
        playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }

    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public void songPicked(int album, int song){
        playerView.setVisibility(View.VISIBLE);
        playPauseButton.setImageResource(R.drawable.ic_pause_black_24dp);
        musicSrv.setSong(album, song);
        musicSrv.playSong();
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }

    private void playNext(){
        musicSrv.playNext();
    }

    private void playPrev(){
        musicSrv.playPrev();
    }

    private void getMusic(){
        albumList = new ArrayList<>();
        Album sk = new Album();
        sk.setAlbumCoverResInt(R.drawable.sk2_cover);
        sk.setBandcampLink("https://fountainparkapts.bandcamp.com/album/sk-2");

        sk.addTrack(new Track("one_one"));
        sk.addTrack(new Track("one_two"));
        sk.addTrack(new Track("one_three"));

        Album fpa = new Album();
        fpa.setAlbumCoverResInt(R.drawable.fpa_cover);
        fpa.setBandcampLink("https://fountainparkapts.bandcamp.com/album/fountain-park-apts");

        fpa.addTrack(new Track("two_one"));
        fpa.addTrack(new Track("two_two"));
        fpa.addTrack(new Track("two_three"));
        fpa.addTrack(new Track("two_four"));
        fpa.addTrack(new Track("two_five"));
        fpa.addTrack(new Track("two_six"));
        fpa.addTrack(new Track("two_seven"));
        fpa.addTrack(new Track("two_eight"));
        fpa.addTrack(new Track("two_nine"));
        fpa.addTrack(new Track("two_ten"));

        Album tmsiym = new Album();
        tmsiym.setAlbumCoverResInt(R.drawable.tmsiym_cover);
        tmsiym.setBandcampLink("https://fountainparkapts.bandcamp.com/album/the-morning-sky-is-yesterdays-mess");

        tmsiym.addTrack(new Track("three_one"));
        tmsiym.addTrack(new Track("three_two"));
        tmsiym.addTrack(new Track("three_three"));
        tmsiym.addTrack(new Track("three_four"));
        tmsiym.addTrack(new Track("three_five"));
        tmsiym.addTrack(new Track("three_six"));
        tmsiym.addTrack(new Track("three_seven"));
        tmsiym.addTrack(new Track("three_eight"));
        tmsiym.addTrack(new Track("three_nine"));
        tmsiym.addTrack(new Track("three_ten"));
        tmsiym.addTrack(new Track("three_eleven"));
        tmsiym.addTrack(new Track("three_twelve"));
        tmsiym.addTrack(new Track("three_thirteen"));

        albumList.add(sk);
        albumList.add(fpa);
        albumList.add(tmsiym);
    }

    public void stopSong() {
        if(playerView!=null) {
            playerView.setVisibility(View.GONE);
        }
    }

    public void setSongInfo(Track playSong) {
        if(songName != null)
            songName.setText(MediaPlayerUtility.getMetaData(MediaMetadataRetriever.METADATA_KEY_TITLE,
                    playSong.getMediaPlayerDataSource(), this));
        if(albumName != null)
            albumName.setText(MediaPlayerUtility.getMetaData(MediaMetadataRetriever.METADATA_KEY_ALBUM,
                    playSong.getMediaPlayerDataSource(), this));
    }
}
