package com.angusgaming.fountainparkapts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harry Cliff on 2/1/2018.
 */

public class Album {

    private List<Track> trackList;

    public Album(){
        trackList = new ArrayList<Track>();
    };

    public void addTrack(Track track){
        if(trackList != null) {
            trackList.add(track);
        }
    }

    public List<Track> getTrackList(){
        return trackList;
    }

}
