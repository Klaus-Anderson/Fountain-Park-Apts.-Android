package com.angusgaming.fountainparkapts;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Harry Cliff on 2/1/2018.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private MainActivity mainActivity;
    private List<Album> listData;
    private MediaPlayerUtility mediaPlayerUtility;

    public ExpandableListAdapter(MainActivity mainActivity, List<Album> listData) {
        this.mainActivity = mainActivity;
        this.listData = listData;
        this.mediaPlayerUtility = mainActivity.getMediaPlayerUtility();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listData.get(groupPosition).getTrackList().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mainActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.track_item, null);
        }
        Album album = listData.get(groupPosition);
        Track track =  album.getTrackList().get(childPosition);

        String songName = mediaPlayerUtility.getMetaData(MediaMetadataRetriever.METADATA_KEY_TITLE,
                track.getMediaPlayerDataSource());

        TextView txtListChild = convertView.findViewById(R.id.track_name);
        TextView lyricsTextview = convertView.findViewById(R.id.lyrics_textview);

        txtListChild.setText(songName);

        if(track.hasLyrics()){
            lyricsTextview.setVisibility(View.VISIBLE);
            lyricsTextview.setOnClickListener(v -> {
                //@todo implement lyrics fragment
            });
        } else {
            lyricsTextview.setVisibility(View.INVISIBLE);
        }

        convertView.setOnClickListener(v -> {
            if (mediaPlayerUtility.isCurrentSong(track) && mediaPlayerUtility. isSongPlaying()) {
                mediaPlayerUtility.pauseCurrentSong();
            } else {
                mediaPlayerUtility.playSong(track, album);
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listData.get(groupPosition).getTrackList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listData.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mainActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.album_header, null);
        }
        Album album = listData.get(groupPosition);

        TextView albumTitleTextView = convertView.findViewById(R.id.album_title);
        TextView bandCampLink = convertView.findViewById(R.id.bandcamp_link);
        ImageView imageView = convertView.findViewById(R.id.album_cover_imageview);

        String albumName = mediaPlayerUtility.getMetaData(MediaMetadataRetriever.METADATA_KEY_ALBUM,
                album.getTrackList().get(0).getMediaPlayerDataSource());

        imageView.setImageDrawable(mainActivity.getResources().getDrawable(listData.get(groupPosition).getAlbumCoverResInt()));
        albumTitleTextView.setText(albumName);

        bandCampLink.setOnClickListener(v -> {
            String url = album.getBandcampLink();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            mainActivity.startActivity(i);
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
