package com.angusgaming.fountainparkapts;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;

/**
 * Created by Harry Cliff on 2/10/2018.
 */

class MediaPlayerUtility {
    public static String getMetaData(int metadataKeyTitle, String mediaPlayerDataSource, Context context) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        AssetFileDescriptor afd= context.getResources().openRawResourceFd(
                context.getResources().getIdentifier(mediaPlayerDataSource,
                        "raw", context.getPackageName()));

        mmr.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());

        return mmr.extractMetadata(metadataKeyTitle);
    }
}
