package com.angusgaming.fountainparkapts;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<Album> listDataHeader;
    HashMap<Album, List<Track>> listDataChild;

    Album sk, fpa, tmsiym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getMusic();

        // get the listview
        expListView = findViewById(R.id.expandable_list_view);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void getMusic(){
        sk = new Album();

        sk.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.one_one)));
        sk.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.one_two)));
        sk.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.one_three)));

        fpa = new Album();
        fpa.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.two_one)));
        fpa.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.two_two)));
        fpa.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.two_three)));
        fpa.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.two_four)));
        fpa.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.two_five)));
        fpa.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.two_six)));
        fpa.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.two_seven)));
        fpa.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.two_eight)));
        fpa.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.two_nine)));
        fpa.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.two_ten)));

        tmsiym = new Album();
        tmsiym.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.three_one)));
        tmsiym.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.three_two)));
        tmsiym.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.three_three)));
        tmsiym.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.three_four)));
        tmsiym.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.three_five)));
        tmsiym.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.three_six)));
        tmsiym.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.three_seven)));
        tmsiym.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.three_eight)));
        tmsiym.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.three_nine)));
        tmsiym.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.three_ten)));
        tmsiym.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.three_eleven)));
        tmsiym.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.three_twelve)));
        tmsiym.addTrack(new Track(MediaPlayer.create(getApplicationContext(), R.raw.three_thirteen)));
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add(sk);
        listDataHeader.add(fpa);
        listDataHeader.add(tmsiym);

        listDataChild.put(listDataHeader.get(0), listDataHeader.get(0).getTrackList()); // Header, Child data
        listDataChild.put(listDataHeader.get(1), listDataHeader.get(1).getTrackList());
        listDataChild.put(listDataHeader.get(2), listDataHeader.get(2).getTrackList());
    }
}
