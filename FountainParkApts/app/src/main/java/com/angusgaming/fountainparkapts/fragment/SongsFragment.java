package com.angusgaming.fountainparkapts.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.angusgaming.fountainparkapts.Album;
import com.angusgaming.fountainparkapts.ExpandableListAdapter;
import com.angusgaming.fountainparkapts.MainActivity;
import com.angusgaming.fountainparkapts.MediaPlayerUtility;
import com.angusgaming.fountainparkapts.R;
import com.angusgaming.fountainparkapts.Track;

import java.util.ArrayList;

/**
 * Created by Harry Cliff on 2/6/2018.
 */

public class SongsFragment extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<Album> listData;

    Album sk, fpa, tmsiym;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.songs_fragment, container, false);

        expListView = view.findViewById(R.id.expandable_list_view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getMusic();
        prepareListData();

        listAdapter = new ExpandableListAdapter((MainActivity)getActivity(), listData);

        expListView.setAdapter(listAdapter);
    }

    private void getMusic(){
        sk = new Album();
        sk.setAlbumCoverResInt(R.drawable.sk2_cover);
        sk.setBandcampLink("https://fountainparkapts.bandcamp.com/album/sk-2");

        sk.addTrack(new Track(getActivity(), R.raw.one_one));
        sk.addTrack(new Track(getActivity(), R.raw.one_two));
        sk.addTrack(new Track(getActivity(), R.raw.one_three));

        fpa = new Album();
        fpa.setAlbumCoverResInt(R.drawable.fpa_cover);
        fpa.setBandcampLink("https://fountainparkapts.bandcamp.com/album/fountain-park-apts");

        fpa.addTrack(new Track(getActivity(), R.raw.two_one));

        fpa.addTrack(new Track(getActivity(), R.raw.two_two));
        fpa.addTrack(new Track(getActivity(), R.raw.two_three));
        fpa.addTrack(new Track(getActivity(), R.raw.two_four));
        fpa.addTrack(new Track(getActivity(), R.raw.two_five));
        fpa.addTrack(new Track(getActivity(), R.raw.two_six));
        fpa.addTrack(new Track(getActivity(), R.raw.two_seven));
        fpa.addTrack(new Track(getActivity(), R.raw.two_eight));
        fpa.addTrack(new Track(getActivity(), R.raw.two_nine));
        fpa.addTrack(new Track(getActivity(), R.raw.two_ten));

        tmsiym = new Album();
        tmsiym.setAlbumCoverResInt(R.drawable.tmsiym_cover);
        tmsiym.setBandcampLink("https://fountainparkapts.bandcamp.com/album/the-morning-sky-is-yesterdays-mess");

        tmsiym.addTrack(new Track(getActivity(), R.raw.three_one));
        tmsiym.addTrack(new Track(getActivity(), R.raw.three_two));
        tmsiym.addTrack(new Track(getActivity(), R.raw.three_three));
        tmsiym.addTrack(new Track(getActivity(), R.raw.three_four));
        tmsiym.addTrack(new Track(getActivity(), R.raw.three_five));
        tmsiym.addTrack(new Track(getActivity(), R.raw.three_six));
        tmsiym.addTrack(new Track(getActivity(), R.raw.three_seven));
        tmsiym.addTrack(new Track(getActivity(), R.raw.three_eight));
        tmsiym.addTrack(new Track(getActivity(), R.raw.three_nine));
        tmsiym.addTrack(new Track(getActivity(), R.raw.three_ten));
        tmsiym.addTrack(new Track(getActivity(), R.raw.three_eleven));
        tmsiym.addTrack(new Track(getActivity(), R.raw.three_twelve));
        tmsiym.addTrack(new Track(getActivity(), R.raw.three_thirteen));
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listData = new ArrayList<>();

        listData.add(sk);
        listData.add(fpa);
        listData.add(tmsiym);
    }
}
