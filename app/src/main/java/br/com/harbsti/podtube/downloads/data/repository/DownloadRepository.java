package br.com.harbsti.podtube.downloads.data.repository;

import java.util.List;

import br.com.harbsti.podtube.downloads.data.model.Download;
import rx.Observable;

/**
 * Created by marcosharbs on 06/12/16.
 */

public interface DownloadRepository {

    Observable<List<Download>> getDownloads();

    Observable<Download> persist(Download download);

    Observable<Download> getDownload(String id);

    Observable<Void> deleteDownload(String id);

}
