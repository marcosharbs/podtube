package br.com.harbsti.podtube.downloads.domain.service.presenter;

import br.com.harbsti.podtube.downloads.presentation.model.Download;
import br.com.harbsti.podtube.channels.presentation.model.Video;

/**
 * Created by marcosharbs on 06/12/16.
 */

public interface DownloadServicePresenter {

    void persistDownload(Video video);

    void downloadVideo(Download download, String dirToSave);

    void deleteDownload(String id);

}
