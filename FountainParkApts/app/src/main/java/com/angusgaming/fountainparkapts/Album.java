package com.angusgaming.fountainparkapts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harry Cliff on 2/1/2018.
 */

public class Album {

    private List<Track> trackList;
    private int albumCoverResInt;
    private String bandcampLink;

    public Album(){
        trackList = new ArrayList<>();
    };

    public void addTrack(Track track){
        if(trackList != null) {
            trackList.add(track);
        }
    }

    public List<Track> getTrackList(){
        return trackList;
    }

    public void setAlbumCoverResInt(int albumCoverResInt) {
        this.albumCoverResInt = albumCoverResInt;
    }

    public int getAlbumCoverResInt() {
        return albumCoverResInt;
    }

    public void setBandcampLink(String bandcampLink) {
        this.bandcampLink = bandcampLink;
    }

     public String getBandcampLink(){
        return bandcampLink;
     }
}
