package com.angusgaming.fountainparkapts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.angusgaming.fountainparkapts.fragment.PlayerFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MediaPlayerUtility mediaPlayerUtility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayerUtility = new MediaPlayerUtility(this,
                ((PlayerFragment)getSupportFragmentManager().findFragmentById(R.id.player_fragment_id)));
    }

    public MediaPlayerUtility getMediaPlayerUtility() {
        return mediaPlayerUtility;
    }
}
