package br.com.harbsti.podtube.downloads.data.mapper;

import br.com.harbsti.podtube.downloads.data.model.Download;
import br.com.harbsti.podtube.downloads.data.model.realm.DownloadRealm;
import br.com.harbsti.podtube.utils.Mapper;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class DownloadRealmDataMapper extends Mapper<DownloadRealm, Download> {

    @Override
    public Download transform(DownloadRealm downloadRealm) {
        return Download.builder()
                .id(downloadRealm.getId())
                .image(downloadRealm.getImage())
                .mp3File(downloadRealm.getMp3File())
                .pusblishDate(downloadRealm.getPusblishDate())
                .title(downloadRealm.getTitle())
                .build();
    }

}
