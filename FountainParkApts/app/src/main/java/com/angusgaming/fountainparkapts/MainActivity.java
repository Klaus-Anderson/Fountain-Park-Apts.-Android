package com.angusgaming.fountainparkapts;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private static final int NOTIFICATION_ID = 1111;
    private static final String ACTION_NEXT = "next";
    private static final String ACTION_PREV = "prev";
    private static final String ACTION_PLAY = "play";
    private static final String ACTION_PAUSE = "pause";
    private static final String NOTIFICATION_ACTION = "notification_action";

    private int currentAlbum, currentSong;
    private MusicService musicSrv;
    private Intent playerIntent;
    private boolean musicBound, started;
    private List<Album> albumList;
    private View playerView;

    private ExpandableListAdapter expandableListAdapter;
    private BroadcastReceiver myReciever;

    private ImageView previousButtton, playPauseButton, nextButton;
    private TextView songName, albumName;
    private MediaController mediaController;
    private MediaMetadata mediaMetadata;
    private PlaybackState playbackState;
    private MediaSession mediaSession;
    private NotificationManager notificationManager;

    private final MediaController.Callback callback = new MediaController.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackState state) {
            playbackState = state;
            if (state.getState() == PlaybackState.STATE_STOPPED ||
                    state.getState() == PlaybackState.STATE_NONE) {
                stopNotification();
            } else {
                Notification notification = createNotification();
                if (notification != null) {
                    notificationManager.notify(NOTIFICATION_ID, notification);
                }
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onMetadataChanged(MediaMetadata metadata) {
            mediaMetadata = metadata;
            Notification notification = createNotification();
            if (notification != null) {
                notificationManager.notify(NOTIFICATION_ID, notification);
            }
        }

        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
            updateSessionToken();
        }
    };

    private void stopNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private void updateSessionToken() {
    }

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

        myReciever = new MyReceiver();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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
        if(playerIntent ==null){
            playerIntent = new Intent(this, MusicService.class);
            bindService(playerIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playerIntent);
        }
    }

    public void start() {
        musicSrv.go();
        playerView.setVisibility(View.VISIBLE);
        playPauseButton.setImageResource(R.drawable.ic_pause_black_24dp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaSession.setPlaybackState(new PlaybackState.Builder().setState(
                    PlaybackState.STATE_PLAYING,0,1).setActions(
                    PlaybackState.ACTION_SKIP_TO_PREVIOUS |
                            PlaybackState.ACTION_SKIP_TO_NEXT | (long)PlaybackState.STATE_PLAYING ).build());
        }
    }

    public void pause() {
        musicSrv.pausePlayer();
        playPauseButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaSession.setPlaybackState(new PlaybackState.Builder().setState(
                    PlaybackState.STATE_PAUSED,0,1).setActions(
                    PlaybackState.ACTION_SKIP_TO_PREVIOUS |
                            PlaybackState.ACTION_SKIP_TO_NEXT | (long)PlaybackState.STATE_PLAYING ).build());
        }
    }

    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    public void seekTo(int pos) {

    }

    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    public int getAudioSessionId() {
        return 0;
    }

    public void songPicked(int album, int song){
        playerView.setVisibility(View.VISIBLE);
        playPauseButton.setImageResource(R.drawable.ic_pause_black_24dp);
        musicSrv.setSong(album, song);
        musicSrv.playSong();
        currentAlbum = album;
        currentSong = song;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            playbackState = new PlaybackState.Builder().setState(
            PlaybackState.STATE_PLAYING,0,1).setActions(
                    PlaybackState.ACTION_SKIP_TO_PREVIOUS |
                    PlaybackState.ACTION_SKIP_TO_NEXT | (long)PlaybackState.STATE_PLAYING ).build();

            mediaMetadata = new MediaMetadata.Builder()
                    .putString(MediaMetadata.METADATA_KEY_ALBUM, MediaPlayerUtility
                            .getMetaData(MediaMetadataRetriever.METADATA_KEY_ALBUM,
                                    albumList.get(currentAlbum).getTrackList().get(currentSong).getMediaPlayerDataSource(),
                                    this))
                    .putString(MediaMetadata.METADATA_KEY_ARTIST, MediaPlayerUtility
                            .getMetaData(MediaMetadataRetriever.METADATA_KEY_ARTIST,
                                    albumList.get(currentAlbum).getTrackList().get(currentSong).getMediaPlayerDataSource(),
                                    this))
                    .putString(MediaMetadata.METADATA_KEY_TITLE, MediaPlayerUtility
                            .getMetaData(MediaMetadataRetriever.METADATA_KEY_TITLE,
                                    albumList.get(currentAlbum).getTrackList().get(currentSong).getMediaPlayerDataSource(),
                                    this)).build();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaSession = new MediaSession(this, "MusicService");
                mediaSession.setCallback(new MainActivity.MediaSessionCallback());
                mediaSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

                Context context = getApplicationContext();
                Intent intent = new Intent(context, MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(context, 99 /*request code*/,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mediaSession.setSessionActivity(pi);
                mediaSession.setPlaybackState(playbackState);

                Bundle mediaSessionExtras = new Bundle();
                mediaSession.setExtras(mediaSessionExtras);

                mediaController = new MediaController(this, mediaSession.getSessionToken());

                createNotifcationCallback();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createNotifcationCallback() {
        Notification notification = createNotification();
        if (notification != null) {
            mediaController.registerCallback(callback);
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_NEXT);
            filter.addAction(ACTION_PAUSE);
            filter.addAction(ACTION_PLAY);
            filter.addAction(ACTION_PREV);
            musicSrv.registerReceiver(myReciever, filter);

            musicSrv.startForeground(NOTIFICATION_ID, notification);
            notificationManager.notify(NOTIFICATION_ID, notification);
            started = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Notification createNotification() {
        if (mediaMetadata == null || playbackState == null) {
            return null;
        }

        Notification.Builder notificationBuilder = new Notification.Builder(musicSrv);
        int playPauseButtonPosition = 0;

        // If skip to previous action is enabled
        if ((playbackState.getActions() & PlaybackState.ACTION_SKIP_TO_PREVIOUS) != 0) {
            //Yes intent
            Intent previousIntent = new Intent();
            previousIntent.setAction(ACTION_PREV);
            PendingIntent pendingPreviousIntent = PendingIntent.getBroadcast(this, 12345,
                    previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.addAction(R.drawable.ic_skip_previous_black_24dp, "Yes", pendingPreviousIntent);

            // If there is a "skip to previous" button, the play/pause button will
            // be the second one. We need to keep track of it, because the MediaStyle notification
            // requires to specify the index of the buttons (actions) that should be visible
            // when in compact view.
            playPauseButtonPosition = 1;
        }

        addPlayPauseAction(notificationBuilder);

        // If skip to next action is enabled
        if ((playbackState.getActions() & PlaybackState.ACTION_SKIP_TO_NEXT) != 0) {
            Intent nextIntent = new Intent();
            nextIntent.setAction(ACTION_NEXT);
            PendingIntent pendingNextIntent = PendingIntent.getBroadcast(this, 12345,
                    nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.addAction(R.drawable.ic_skip_next_black_24dp,
                    "next", pendingNextIntent);
        }

        MediaDescription mediaDescription = mediaMetadata.getDescription();

        notificationBuilder
                .setStyle(new Notification.MediaStyle()
                        .setShowActionsInCompactView(new int[]{playPauseButtonPosition})  // show only play/pause in compact view
                        .setMediaSession(mediaSession.getSessionToken()))
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.fpa_cover)
                .setContentIntent(createPeningIntent(mediaDescription)) // Create an intent that would open the UI when user clicks the notification
                .setContentTitle(mediaDescription.getTitle())
                .setContentText(MediaPlayerUtility
                        .getMetaData(MediaMetadataRetriever.METADATA_KEY_ALBUM,
                                albumList.get(currentAlbum).getTrackList().get(currentSong).getMediaPlayerDataSource(),
                                this));

        setNotificationPlaybackState(notificationBuilder);

        return notificationBuilder.build();
    }

    private PendingIntent createPeningIntent(MediaDescription description) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 99 /*request code*/,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pi;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addPlayPauseAction(Notification.Builder builder) {
        String label;
        int icon;
        PendingIntent intent;
        if (playbackState.getState() == PlaybackState.STATE_PLAYING) {
            label = "pause";
            icon = R.drawable.ic_pause_black_24dp;
            Intent pauseIntent = new Intent();
            pauseIntent.setAction(ACTION_PAUSE);
            intent = PendingIntent.getBroadcast(this, 12345,
                    pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        } else {
            label = "play";
            icon = R.drawable.ic_play_arrow_black_24dp;
            Intent playIntent = new Intent();
            playIntent.setAction(ACTION_PLAY);
            intent = PendingIntent.getBroadcast(this, 12345,
                    playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        builder.addAction(new Notification.Action(icon, label, intent));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setNotificationPlaybackState(Notification.Builder builder) {
        if (playbackState == null || !started) {
            musicSrv.stopForeground(true);
            return;
        }

        // Make sure that the notification can be dismissed by the user when we are not playing:
        builder.setOngoing(playbackState.getState() == PlaybackState.STATE_PLAYING);
    }

    @Override
    protected void onDestroy() {
        stopService(playerIntent);
        musicSrv=null;
        super.onDestroy();
    }

    private void playNext(){
        musicSrv.playNext();
        currentSong++;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                mediaSession.setMetadata(new MediaMetadata.Builder()
                        .putString(MediaMetadata.METADATA_KEY_ALBUM, MediaPlayerUtility
                                .getMetaData(MediaMetadataRetriever.METADATA_KEY_ALBUM,
                                        albumList.get(currentAlbum).getTrackList().get(currentSong).getMediaPlayerDataSource(),
                                        this))
                        .putString(MediaMetadata.METADATA_KEY_ARTIST, MediaPlayerUtility
                                .getMetaData(MediaMetadataRetriever.METADATA_KEY_ARTIST,
                                        albumList.get(currentAlbum).getTrackList().get(currentSong).getMediaPlayerDataSource(),
                                        this))
                        .putString(MediaMetadata.METADATA_KEY_TITLE, MediaPlayerUtility
                                .getMetaData(MediaMetadataRetriever.METADATA_KEY_TITLE,
                                        albumList.get(currentAlbum).getTrackList().get(currentSong).getMediaPlayerDataSource(),
                                        this)).build());
                mediaSession.setPlaybackState(new PlaybackState.Builder().setState(
                        PlaybackState.STATE_PLAYING, 0, 1).setActions(
                        PlaybackState.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackState.ACTION_SKIP_TO_NEXT | (long) PlaybackState.STATE_PLAYING).build());
            } catch (Exception e) {}
        }
    }

    private void playPrev(){
        musicSrv.playPrev();
        currentSong--;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                mediaSession.setMetadata(new MediaMetadata.Builder()
                        .putString(MediaMetadata.METADATA_KEY_ALBUM, MediaPlayerUtility
                                .getMetaData(MediaMetadataRetriever.METADATA_KEY_ALBUM,
                                        albumList.get(currentAlbum).getTrackList().get(currentSong).getMediaPlayerDataSource(),
                                        this))
                        .putString(MediaMetadata.METADATA_KEY_ARTIST, MediaPlayerUtility
                                .getMetaData(MediaMetadataRetriever.METADATA_KEY_ARTIST,
                                        albumList.get(currentAlbum).getTrackList().get(currentSong).getMediaPlayerDataSource(),
                                        this))
                        .putString(MediaMetadata.METADATA_KEY_TITLE, MediaPlayerUtility
                                .getMetaData(MediaMetadataRetriever.METADATA_KEY_TITLE,
                                        albumList.get(currentAlbum).getTrackList().get(currentSong).getMediaPlayerDataSource(),
                                        this)).build());
                mediaSession.setPlaybackState(new PlaybackState.Builder().setState(
                        PlaybackState.STATE_PLAYING, 0, 1).setActions(
                        PlaybackState.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackState.ACTION_SKIP_TO_NEXT | (long) PlaybackState.STATE_PLAYING).build());
            } catch (Exception e){}
        }
    }

    private void getMusic(){
        albumList = new ArrayList<>();
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

        albumList.add(fpa);
        albumList.add(tmsiym);
    }

    public void stopSong() {
        if(playerView!=null) {
            playerView.setVisibility(View.GONE);
        }
        musicSrv.stopForeground(true);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public void setSongInfo(Track playSong) {
        if(songName != null)
            songName.setText(MediaPlayerUtility.getMetaData(MediaMetadataRetriever.METADATA_KEY_TITLE,
                    playSong.getMediaPlayerDataSource(), this));
        if(albumName != null)
            albumName.setText(MediaPlayerUtility.getMetaData(MediaMetadataRetriever.METADATA_KEY_ALBUM,
                    playSong.getMediaPlayerDataSource(), this));
    }
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(ACTION_NEXT.equals(action)) {
                playNext();
            } else if(ACTION_PREV.equals(action)) {
                playPrev();
            } else if(ACTION_PLAY.equals(action)) {
                start();
            } else if(ACTION_PAUSE.equals(action)) {
                pause();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private final class MediaSessionCallback extends MediaSession.Callback {
        @Override
        public void onPlay() {
            start();
        }

        @Override
        public void onSkipToQueueItem(long queueId) {
        }

        @Override
        public void onSeekTo(long position) {
        }

        @Override
        public void onPause() {
            pause();
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
