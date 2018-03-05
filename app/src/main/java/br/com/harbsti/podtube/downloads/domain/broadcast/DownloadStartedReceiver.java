package br.com.harbsti.podtube.downloads.domain.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by marcosharbs on 07/12/16.
 */

public class DownloadStartedReceiver extends BroadcastReceiver {

    public static final String FILTER = "br.com.harbsti.podtube.DOWNLOAD_STARTED";

    private OnDownloadStarted onDownloadStarted;

    public DownloadStartedReceiver(){}

    public DownloadStartedReceiver(OnDownloadStarted onDownloadStarted) {
        this.onDownloadStarted = onDownloadStarted;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        onDownloadStarted.onDownloadStarted();
    }

    public interface OnDownloadStarted {
        void onDownloadStarted();
    }

}
