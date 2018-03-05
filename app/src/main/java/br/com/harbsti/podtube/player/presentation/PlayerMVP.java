package br.com.harbsti.podtube.player.presentation;

import android.content.Context;
import android.widget.SeekBar;

import br.com.harbsti.podtube.downloads.presentation.model.Download;

/**
 * Created by marcosharbs on 29/08/17.
 */

public interface PlayerMVP {

    interface Model {}

    interface View {
        void playVideo(Download download);
        Context getContext();
        SeekBar getPlayerTrack();
        void onShowPlayer();
        void onHidePlayer();
        void onError(Throwable throwable);
    }

    interface Presenter {
        void setView(View view);
        void play(Download download);
        void pausePlayer();
        void playPlayer();
        void stopPlayer();
    }

}
