package br.com.harbsti.podtube.downloads.domain.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by marcosharbs on 07/12/16.
 */

public class DownloadDeletedReceiver extends BroadcastReceiver {

    public static final String FILTER = "br.com.harbsti.podtube.DOWNLOAD_DELETED";

    private OnDownloadDeleted onDownloadDeleted;

    public DownloadDeletedReceiver(){}

    public DownloadDeletedReceiver(OnDownloadDeleted onDownloadDeleted) {
        this.onDownloadDeleted = onDownloadDeleted;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        onDownloadDeleted.onDownloadDeleted();
    }

    public interface OnDownloadDeleted {
        void onDownloadDeleted();
    }

}
