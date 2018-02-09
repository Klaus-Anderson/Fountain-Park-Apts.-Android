package com.angusgaming.fountainparkapts.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.angusgaming.fountainparkapts.MainActivity;
import com.angusgaming.fountainparkapts.MediaPlayerUtility;
import com.angusgaming.fountainparkapts.R;

/**
 * Created by Harry Cliff on 2/6/2018.
 */

public class PlayerFragment extends Fragment {
    private ImageView previousButtton, playPauseButton, nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player_fragment, container, false);

        previousButtton = view.findViewById(R.id.previousButton);
        playPauseButton = view.findViewById(R.id.playButton);
        nextButton = view.findViewById(R.id.nextButton);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MediaPlayerUtility mediaPlayerUtility = ((MainActivity)getActivity()).getMediaPlayerUtility();

        previousButtton.setOnClickListener(v -> mediaPlayerUtility.playPrevious());
        nextButton.setOnClickListener(v -> mediaPlayerUtility.playNextSong());
        playPauseButton.setOnClickListener(v -> {
            if (mediaPlayerUtility.isSongPlaying()) {
                mediaPlayerUtility.pauseCurrentSong();
            } else {
                mediaPlayerUtility.resumeSong();
            }
        });
    }

    public ImageView getPreviousButtton() {
        return previousButtton;
    }

    public ImageView getPlayPauseButton() {
        return playPauseButton;
    }

    public ImageView getNextButton() {
        return nextButton;
    }
}
