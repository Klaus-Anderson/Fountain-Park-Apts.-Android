package com.angusgaming.fountainparkapts;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Harry Cliff on 2/1/2018.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Album> listData;
    private MediaPlayerUtility mediaPlayerUtility;

    public ExpandableListAdapter(Context context, List<Album> listData, MediaPlayerUtility mediaPlayerUtility) {
        this.context = context;
        this.listData = listData;
        this.mediaPlayerUtility = mediaPlayerUtility;
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
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.track_item, null);
        }

        String songName = mediaPlayerUtility.getMetaData(MediaMetadataRetriever.METADATA_KEY_TITLE,
                listData.get(groupPosition).getTrackList().get(childPosition).getMediaPlayerDataSource());

        TextView txtListChild = convertView.findViewById(R.id.track_name);

        txtListChild.setText(songName);

        convertView.setOnClickListener(v -> {
            Track track = listData.get(groupPosition).getTrackList()
                    .get(childPosition);
            if (mediaPlayerUtility.isCurrentSong(track) && mediaPlayerUtility.isSongPlaying()) {
                mediaPlayerUtility.pauseCurrentSong();
            } else {
                mediaPlayerUtility.playSong(track, listData.get(groupPosition));
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
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.album_header, null);
        }

        String albumName = mediaPlayerUtility.getMetaData(MediaMetadataRetriever.METADATA_KEY_ALBUM,
                listData.get(groupPosition).getTrackList().get(0).getMediaPlayerDataSource());
        TextView lblListHeader = convertView
                .findViewById(R.id.album_track);
        lblListHeader.setText(albumName);

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
