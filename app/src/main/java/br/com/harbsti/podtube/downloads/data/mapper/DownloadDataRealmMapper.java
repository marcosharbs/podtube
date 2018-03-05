package br.com.harbsti.podtube.downloads.data.mapper;

import br.com.harbsti.podtube.downloads.data.model.Download;
import br.com.harbsti.podtube.downloads.data.model.realm.DownloadRealm;
import br.com.harbsti.podtube.utils.Mapper;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class DownloadDataRealmMapper extends Mapper<Download, DownloadRealm> {

    @Override
    public DownloadRealm transform(Download download) {
        DownloadRealm downloadRealm = new DownloadRealm();
        downloadRealm.setId(download.id());
        downloadRealm.setImage(download.image());
        downloadRealm.setMp3File(download.mp3File());
        downloadRealm.setPusblishDate(download.pusblishDate());
        downloadRealm.setTitle(download.title());
        return downloadRealm;
    }

}
