package br.com.harbsti.podtube.downloads.domain.service;

import br.com.harbsti.podtube.downloads.presentation.model.Download;

/**
 * Created by marcosharbs on 06/12/16.
 */

public interface DownloadService {

    void onError(Throwable throwable);

    void onDownloadPersisted(Download download);

    void onDownloadCompleted(Download download);

    void onDownloadDeleted();

}
