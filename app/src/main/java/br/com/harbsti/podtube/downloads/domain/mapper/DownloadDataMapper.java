package br.com.harbsti.podtube.downloads.domain.mapper;

import br.com.harbsti.podtube.utils.Mapper;
import br.com.harbsti.podtube.downloads.domain.model.Download;

/**
 * Created by marcosharbs on 06/12/16.
 */

public class DownloadDataMapper extends Mapper<Download, br.com.harbsti.podtube.downloads.data.model.Download> {

    @Override
    public br.com.harbsti.podtube.downloads.data.model.Download transform(Download download) {
        return br.com.harbsti.podtube.downloads.data.model.Download.builder()
                .id(download.id())
                .image(download.image())
                .mp3File(download.mp3File())
                .pusblishDate(download.pusblishDate())
                .title(download.title())
                .build();
    }

}
