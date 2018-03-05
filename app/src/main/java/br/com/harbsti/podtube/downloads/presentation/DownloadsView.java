package br.com.harbsti.podtube.downloads.presentation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.List;

import javax.inject.Inject;

import br.com.harbsti.podtube.di.App;
import br.com.harbsti.podtube.BuildConfig;
import br.com.harbsti.podtube.R;
import br.com.harbsti.podtube.di.components.DaggerDownloadComponent;
import br.com.harbsti.podtube.di.modules.DownloadModule;
import br.com.harbsti.podtube.downloads.domain.broadcast.DownloadCompletedReceiver;
import br.com.harbsti.podtube.downloads.domain.broadcast.DownloadDeletedReceiver;
import br.com.harbsti.podtube.downloads.domain.broadcast.DownloadStartedReceiver;
import br.com.harbsti.podtube.player.presentation.PlayerMVP;
import br.com.harbsti.podtube.utils.ExceptionHelper;
import br.com.harbsti.podtube.downloads.presentation.model.Download;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by marcosharbs on 07/12/16.
 */

public class DownloadsView extends Fragment implements DownloadMVP.View,
                                                        DownloadsAdapter.OnDownloadsAdapter,
                                                        DownloadCompletedReceiver.OnDownloadCompleted,
                                                        DownloadStartedReceiver.OnDownloadStarted,
                                                        DownloadDeletedReceiver.OnDownloadDeleted {

    @InjectView(R.id.downloads_recyclerview) RecyclerView downloadsRecyclerView;
    @Inject DownloadMVP.Presenter presenter;

    private DownloadsAdapter downloadsAdapter;

    private DownloadCompletedReceiver downloadCompletedReceiver;
    private IntentFilter downloadCompletedIntentFilter;
    private DownloadStartedReceiver downloadStartedReceiver;
    private IntentFilter downloadStartedIntentFilter;
    private DownloadDeletedReceiver downloadDeletedReceiver;
    private IntentFilter downloadDeletedIntentFilter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.downloads, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        DaggerDownloadComponent.builder()
                .applicationComponent(App.get(getActivity()).getComponent())
                .downloadModule(new DownloadModule(this))
                .build()
                .inject(this);

        downloadsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        downloadsAdapter = null;

        downloadCompletedReceiver = new DownloadCompletedReceiver(this);
        downloadCompletedIntentFilter = new IntentFilter(DownloadCompletedReceiver.FILTER);
        downloadStartedReceiver = new DownloadStartedReceiver(this);
        downloadStartedIntentFilter = new IntentFilter(DownloadStartedReceiver.FILTER);
        downloadDeletedReceiver = new DownloadDeletedReceiver(this);
        downloadDeletedIntentFilter = new IntentFilter(DownloadDeletedReceiver.FILTER);

        getActivity().registerReceiver(downloadCompletedReceiver, downloadCompletedIntentFilter);
        getActivity().registerReceiver(downloadStartedReceiver, downloadStartedIntentFilter);
        getActivity().registerReceiver(downloadDeletedReceiver, downloadDeletedIntentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.getDownloads();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(downloadCompletedReceiver);
        getActivity().unregisterReceiver(downloadStartedReceiver);
        getActivity().unregisterReceiver(downloadDeletedReceiver);
    }

    @Override
    public void onDownloadsLoaded(List<Download> downloads) {
        downloadsAdapter = new DownloadsAdapter(getActivity(), downloads, this);
        downloadsRecyclerView.setAdapter(downloadsAdapter);
    }

    @Override
    public void onDownloadRemoved() {
        presenter.getDownloads();
    }

    @Override
    public void onError(Throwable throwable) {
        ExceptionHelper.handleThrowable(getActivity(), throwable);
    }

    @Override
    public void onDownloadCompleted() {
        presenter.getDownloads();
    }

    @Override
    public void onDownloadStarted() {
        presenter.getDownloads();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onPlay(Download download) {
        PlayerMVP.View playerView = (PlayerMVP.View) getActivity();
        playerView.playVideo(download);
    }

    @Override
    public void onDelete(final Download download) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.label_delete)
                .setMessage(R.string.label_delete_msg)
                .setPositiveButton(R.string.label_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteDownload(download);
                    }
                })
                .setNegativeButton(R.string.label_no, null)
                .show();
    }

    @Override
    public void onViewMovie(Download download) {
        Intent intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(),
                BuildConfig.YOUTUBE_KEY,
                download.id(),
                0,
                true,
                true);
        startActivity(intent);
    }

    @Override
    public void onDownloadDeleted() {
        presenter.getDownloads();
    }
}
