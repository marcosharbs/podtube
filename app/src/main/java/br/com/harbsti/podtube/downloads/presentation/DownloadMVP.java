package br.com.harbsti.podtube.downloads.presentation;

import java.util.List;

import br.com.harbsti.podtube.downloads.presentation.model.Download;
import rx.Observable;

/**
 * Created by marcosharbs on 02/08/17.
 */

public interface DownloadMVP {

    interface Model {
        Observable<List<Download>> getDownloads();
        Observable<Void> deleteDownload(String id);
    }

    interface Presenter {
        void setView(DownloadMVP.View view);
        void getDownloads();
        void deleteDownload(Download download);
    }

    interface View {
        void onError(Throwable throwable);
        void onDownloadsLoaded(List<Download> downloads);
        void onDownloadRemoved();
    }

}
