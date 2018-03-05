package br.com.harbsti.podtube.downloads.domain.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by marcosharbs on 07/12/16.
 */

public class DownloadCompletedReceiver extends BroadcastReceiver {

    public static final String FILTER = "br.com.harbsti.podtube.DOWNLOAD_COMPLETED";

    private OnDownloadCompleted onDownloadCompleted;

    public DownloadCompletedReceiver(){}

    public DownloadCompletedReceiver(OnDownloadCompleted onDownloadCompleted) {
        this.onDownloadCompleted = onDownloadCompleted;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        onDownloadCompleted.onDownloadCompleted();
    }

    public interface OnDownloadCompleted {
        void onDownloadCompleted();
    }

}
