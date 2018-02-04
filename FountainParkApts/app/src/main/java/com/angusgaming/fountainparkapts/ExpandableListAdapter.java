package com.angusgaming.fountainparkapts;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.util.List;

/**
 * Created by Harry Cliff on 2/1/2018.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Album> listData;

    public ExpandableListAdapter(Context context, List<Album> listData) {
        this.context = context;
        this.listData = listData;
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

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        AssetFileDescriptor afd= context.getResources()
                .openRawResourceFd(listData.get(groupPosition).getTrackList().get(childPosition).getMediaPlayerDataSource());
        mmr.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());

        String songName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

        TextView txtListChild = convertView.findViewById(R.id.track_name);

        txtListChild.setText(songName);
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

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        AssetFileDescriptor afd= context.getResources()
                .openRawResourceFd(listData.get(groupPosition).getTrackList().get(0).getMediaPlayerDataSource());
        mmr.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());

        String albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
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
