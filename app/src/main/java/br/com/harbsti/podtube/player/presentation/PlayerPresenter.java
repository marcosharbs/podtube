package br.com.harbsti.podtube.player.presentation;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.widget.SeekBar;

import br.com.harbsti.podtube.downloads.presentation.model.Download;

/**
 * Created by marcosharbs on 29/08/17.
 */

public class PlayerPresenter implements PlayerMVP.Presenter {

    private MediaPlayer mediaPlayer;
    private CountDownTimer trackMediaPlayer;
    private Download download;
    private PlayerMVP.View view;

    @Override
    public void setView(PlayerMVP.View view) {
        this.view = view;
    }

    @Override
    public void play(Download download) {
        this.download = download;
        stopMediaPlayer();
        cancelTracker();
        createMediaPlayer();
        createTracker();
        view.onShowPlayer();
    }

    @Override
    public void pausePlayer() {
        if(mediaPlayer != null){
            mediaPlayer.pause();
        }
        cancelTracker();
    }

    @Override
    public void playPlayer() {
        if(mediaPlayer == null){
            play(download);
        }else{
            mediaPlayer.start();
            mediaPlayer.seekTo(view.getPlayerTrack().getProgress());
            cancelTracker();
            createTracker();
        }
    }

    @Override
    public void stopPlayer() {
        stopMediaPlayer();
        cancelTracker();
        mediaPlayer = null;
        view.onHidePlayer();
    }

    private void createMediaPlayer() {
        mediaPlayer = MediaPlayer.create(view.getContext(), Uri.parse(download.mp3File()));
        mediaPlayer.setOnCompletionListener(getOnCompletionListener());
        mediaPlayer.start();
        view.getPlayerTrack().setMax(mediaPlayer.getDuration());
        view.getPlayerTrack().setOnSeekBarChangeListener(getSeekBarListener());
    }

    private void cancelTracker() {
        if(trackMediaPlayer != null){
            trackMediaPlayer.cancel();
        }
    }

    private void stopMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
    }

    private void createTracker() {
        trackMediaPlayer = new CountDownTimer(mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition(), 200) {
            @Override
            public void onTick(long l) {
                if(mediaPlayer != null) {
                    view.getPlayerTrack().setProgress((int) (mediaPlayer.getDuration() - l));
                }
            }

            @Override
            public void onFinish() {}
        }.start();
    }

    private MediaPlayer.OnCompletionListener getOnCompletionListener() {
        return new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayerIn) {
                mediaPlayer = null;
                cancelTracker();
            }
        };
    }

    private SeekBar.OnSeekBarChangeListener getSeekBarListener() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mediaPlayer != null && mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(seekBar.getProgress());
                    cancelTracker();
                    createTracker();
                }
            }
        };
    }

}
